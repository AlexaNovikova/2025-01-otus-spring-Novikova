package ru.otus.hw.services;


import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Egg;

public interface EggToCaterpillarTransformerService {

	Caterpillar transform(Egg egg);
}
