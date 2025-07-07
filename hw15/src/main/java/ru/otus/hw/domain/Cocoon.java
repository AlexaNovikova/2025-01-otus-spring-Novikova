package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cocoon {

    private String futureButterflyColor;

    private int growthTime;

    public Cocoon(String futureButterflyColor) {
        this.futureButterflyColor = futureButterflyColor;
        this.growthTime = RandomUtils.nextInt(1, 5);
    }
}
