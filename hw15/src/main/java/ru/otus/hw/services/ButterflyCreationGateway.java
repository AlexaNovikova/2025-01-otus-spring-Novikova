package ru.otus.hw.services;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Egg;

import java.util.Collection;

@MessagingGateway
public interface ButterflyCreationGateway {

	@Gateway(requestChannel = "eggChannel", replyChannel = "butterflyChannel")
	Collection<Butterfly> process(Collection<Egg> eggs);
}
