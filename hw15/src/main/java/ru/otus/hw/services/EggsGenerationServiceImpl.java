package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Egg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EggsGenerationServiceImpl implements EggsGenerationService {

    private static final String[] COLOR = {"green", "red", "yellow", "brown", "white"};

    private final ButterflyCreationGateway butterflyCreationGateway;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();


    @Override
    public void startGenerateEggsLoop() {
        Collection<Egg> eggs = generateEggs();
        log.info("New eggs: {}",
                eggs.stream().map(Egg::getFutureButterflyColor)
                        .collect(Collectors.joining(",")));
        Collection<Butterfly> butterflies = butterflyCreationGateway.process(eggs);
        log.info("Created Butterflies: {}", butterflies.stream()
                .map(Butterfly::getColor)
                .collect(Collectors.joining(",")));
    }

    private Egg generateEgg() {
        return new Egg(
                COLOR[randomGenerator.nextInt(0, COLOR.length)],
                randomGenerator.nextInt(1, 5));
    }

    private Collection<Egg> generateEggs() {
        List<Egg> eggs = new ArrayList<>();
        for (int i = 0; i < randomGenerator.nextInt(5, 10); ++i) {
            eggs.add(generateEgg());
        }
        return eggs;
    }

}
