package ru.otus.inventory.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsBalanceResponseDto {

    private Map<Long, Integer> balance;

}
