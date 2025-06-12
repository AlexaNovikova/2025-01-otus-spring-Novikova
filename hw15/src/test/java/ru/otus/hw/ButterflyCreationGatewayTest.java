package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Egg;
import ru.otus.hw.services.ButterflyCreationGateway;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ButterflyCreationGatewayTest {

    @Autowired
    private ButterflyCreationGateway butterflyCreationGateway;

    private final List<Egg> eggs = List.of(
            new Egg("red", 2),
            new Egg("green", 3),
            new Egg("yellow", 5),
            new Egg("yellow", 1),
            new Egg("white", 3),
            new Egg("red", 2));

    @Test
    public void shouldTransformAllEggsToButterflies(){
        Collection<Butterfly> butterflies = butterflyCreationGateway.process(eggs);
        assertThat(butterflies.size()).isEqualTo(eggs.size());
        List<String> eggsFutureColor = eggs.stream()
                .map(Egg::getFutureButterflyColor)
                .toList();
        List<String> butterflyColors = butterflies.stream()
                .map(Butterfly::getColor)
                .toList();
        assertThat(eggsFutureColor).isEqualTo(butterflyColors);
    }
}
