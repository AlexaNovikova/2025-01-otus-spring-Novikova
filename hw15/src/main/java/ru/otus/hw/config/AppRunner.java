package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.EggsGenerationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
	private final EggsGenerationService eggsGenerationService;

	@Override
	public void run(String... args) {
		eggsGenerationService.startGenerateEggsLoop();
	}
}
