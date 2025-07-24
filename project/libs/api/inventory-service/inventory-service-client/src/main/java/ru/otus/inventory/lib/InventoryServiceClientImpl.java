package ru.otus.inventory.lib;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class InventoryServiceClientImpl implements InventoryServiceClient {

    @Value("${webclient.inventoryService.url}")
    private String baseUrl;
    private RestClient client;


    @PostConstruct
    public void init() {
        client = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public ProductsBalanceResponseDto getActualBalance(List<Long> productIds) {
        return client.get()
                .uri(BASE_INTERNAL_URL + BALANCE_URL,
                        uriBuilder -> uriBuilder
                                .queryParam("productIds", productIds.toArray()).build()
                )
                .retrieve()
                .body(ProductsBalanceResponseDto.class);
    }

    @Override
    public ProductAvailableQuantityResponseDto getActualQuantity(Long productId) {
        return client.get()
                .uri(BASE_INTERNAL_URL,
                        uriBuilder -> uriBuilder
                                .queryParam("productId", productId).build()
                )
                .retrieve()
                .body(ProductAvailableQuantityResponseDto.class);
    }
}
