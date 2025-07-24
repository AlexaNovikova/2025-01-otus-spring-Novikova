package ru.otus.inventory.lib;


import java.util.List;

public interface InventoryServiceClient {

    String BASE_INTERNAL_URL = "/api/internal/inventory";

    String BALANCE_URL = "/balance";

    ProductsBalanceResponseDto getActualBalance(List<Long> productIds);

    ProductAvailableQuantityResponseDto getActualQuantity(Long newProductId);
}
