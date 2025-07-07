package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Egg;

import java.util.random.RandomGenerator;


@Service
@Slf4j
public class EggToCaterpillarTransformerServiceImpl implements EggToCaterpillarTransformerService {

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    @Override
    public Caterpillar transform(Egg egg) {
        log.info("Egg transforming {}", egg.getFutureButterflyColor());
        delay(egg.getMaturationTime());
        log.info("Caterpillar {} created", egg.getFutureButterflyColor());
        return new Caterpillar(egg.getFutureButterflyColor(), randomGenerator.nextInt(1, 5));
    }

    private static void delay(int time) {
        try {
            Thread.sleep(100L * time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
