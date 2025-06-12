package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Caterpillar {

    private String futureButterflyColor;

    private int growthTime;
}
