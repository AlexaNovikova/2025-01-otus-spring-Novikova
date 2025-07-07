package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Cocoon;
import ru.otus.hw.services.CocoonToButterflyTransformerService;
import ru.otus.hw.services.EggToCaterpillarTransformerService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> eggChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> butterflyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(1000).maxMessagesPerPoll(5);
    }

    @Bean
    public IntegrationFlow butterflyCreationFlow(EggToCaterpillarTransformerService
                                                         eggToCaterpillarTransformerService,
                                                 CocoonToButterflyTransformerService
                                                         cocoonToButterflyTransformerService) {
        return IntegrationFlow.from(eggChannel())
                .split()
                .handle(eggToCaterpillarTransformerService, "transform")
                .<Caterpillar, Cocoon>transform(c -> new Cocoon(c.getFutureButterflyColor()))
                .handle(cocoonToButterflyTransformerService, "transform")
                .aggregate()
                .channel(butterflyChannel())
                .get();
    }
}

