package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Cocoon;

@Service
@Slf4j
public class CocoonToButterflyTransformerServiceImpl
        implements CocoonToButterflyTransformerService {

    @Override
    public Butterfly transform(Cocoon cocoon) {
        log.info("Cocoon transforming {}", cocoon.getFutureButterflyColor());
        delay(cocoon.getGrowthTime());

        Butterfly butterfly = new Butterfly(cocoon.getFutureButterflyColor());
        log.info("Butterfly color : {}  created", butterfly.getColor());
        return butterfly;
    }

    private static void delay(int time) {
        try {
            Thread.sleep(100L * time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
