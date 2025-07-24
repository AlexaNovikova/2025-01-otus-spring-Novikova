package ru.otus.inventory.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.inventory.lib.ProductAvailableQuantityResponseDto;
import ru.otus.inventory.lib.ProductsBalanceResponseDto;
import ru.otus.inventory.service.entity.Inventory;
import ru.otus.inventory.service.entity.Product;
import ru.otus.inventory.service.entity.ReservedProduct;
import ru.otus.inventory.service.entity.ReservedProductId;
import ru.otus.inventory.service.repository.InventoryRepository;
import ru.otus.inventory.service.repository.ReservedProductRepository;
import ru.otus.lib.kafka.model.ReservationResultModel;
import ru.otus.lib.kafka.model.ReservationProcessModel;
import ru.otus.lib.kafka.service.BusinessTopics;
import ru.otus.lib.kafka.service.KafkaProducerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ReservedProductRepository reservedProductRepository;

    private final KafkaProducerService kafkaProducerService;

    public ProductsBalanceResponseDto getBalance(List<Long> productIds) {

        if (CollectionUtils.isEmpty(productIds)) {
            log.warn("No products found for ids: {}", productIds);
            return new ProductsBalanceResponseDto(Map.of());
        }

        var values = inventoryRepository.findAllById(productIds);
        if (CollectionUtils.isEmpty(values)) {
            log.warn("No product balance found for ids: {}", productIds);
            return new ProductsBalanceResponseDto(Map.of());
        }

        var reservedProductsList = reservedProductRepository.findAllByProductIds(productIds);

        var reservedProductMap = createReservedProductsMap(reservedProductsList);

        var productsBalanceMap = values.stream().collect(Collectors.toMap(
                Inventory::getProductId,
                i -> i.getQuantity() - reservedProductMap.getOrDefault(i.getProductId(), 0),
                Integer::sum
        ));
        return new ProductsBalanceResponseDto(productsBalanceMap);
    }

    private Map<Long, Integer> createReservedProductsMap
            (List<ReservedProduct> reservedProducts) {
        return reservedProducts.stream()
                .collect(Collectors.toMap(
                        rp -> rp.getId().getProductId(),
                        ReservedProduct::getQuantity,
                        Integer::sum)
                );
    }

    @Transactional
    public void processReservation(ReservationProcessModel model) {

        var productQuantityMap = model.getProductQuantityMap();
        var productIds = new ArrayList<>(productQuantityMap.keySet());
        var orderId = model.getOrderId();

        var inventories = inventoryRepository.findAllById(productIds);

        if (CollectionUtils.isEmpty(inventories)) {
            log.error("Unable to reserve all products with ids: {}. " +
                    "Inventory is empty for order with id: {}", productIds, orderId);
            kafkaProducerService.send(
                    BusinessTopics.ORDER_RESERVATION_CONFIRMATION,
                    ReservationResultModel.error(orderId,
                            "Inventory is empty")
            );
            return;
        }

        var reservedProducts = reservedProductRepository.findAllByProductIds(productIds);
        var reservedProductMap = createReservedProductsMap(reservedProducts);

        var listShortageIds = createShortageListProductsIds(inventories,
                productQuantityMap, reservedProductMap);

        if (!CollectionUtils.isEmpty(listShortageIds)) {
            log.error("Unable to reserve products, there is lack of products with ids:" +
                    " {} for the order with id: {}", listShortageIds, orderId);
            kafkaProducerService.send(
                    BusinessTopics.ORDER_RESERVATION_CONFIRMATION,
                    ReservationResultModel.error(orderId,
                            "Some inventory for products is empty or are not enough")
            );
            return;
        }

        log.debug("Products are available, reserving products...");

        var inventoryMap = inventories
                .stream()
                .collect(Collectors.toMap(Inventory::getProductId,
                        Inventory::getProduct));

        var reservedProductListForOrder =
                createReservedProductsListForOrder(productQuantityMap,
                        orderId, inventoryMap);

        reservedProductRepository.saveAll(reservedProductListForOrder);

        kafkaProducerService.send(
                BusinessTopics.ORDER_RESERVATION_CONFIRMATION,
                ReservationResultModel.success(orderId)
        );
    }

    private List<ReservedProduct> createReservedProductsListForOrder
            (Map<Long, Integer> productQuantityMap, Long orderId,
             Map<Long, Product> inventoryMap) {
        return productQuantityMap.entrySet().stream()
                .map(entry -> {
                    var id = ReservedProductId.builder()
                            .orderId(orderId)
                            .build();
                    return ReservedProduct.builder()
                            .id(id)
                            .quantity(entry.getValue())
                            .product(inventoryMap.get(entry.getKey()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<Long> createShortageListProductsIds(List<Inventory> inventories, Map<Long,
            Integer> productQuantityMap, Map<Long, Integer> reservedProductMap) {
        return inventories.stream()
                .filter(i -> {
                    var productId = i.getProductId();
                    var productTotalQuantity = i.getQuantity();
                    var requestedQuantity = productQuantityMap.get(productId);
                    var reservedQuantity = reservedProductMap.getOrDefault(productId, 0);
                    var availableQuantity = productTotalQuantity - reservedQuantity;
                    return requestedQuantity == null ||
                            requestedQuantity > availableQuantity;
                })
                .map(Inventory::getProductId)
                .toList();
    }

    public ProductAvailableQuantityResponseDto getBalanceForProduct(Long productId) {
        var inventory = inventoryRepository.findByProductId(productId);
        var response = new ProductAvailableQuantityResponseDto();
        response.setQuantity(inventory.getQuantity());
        return response;
    }
}
