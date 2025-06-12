package ru.otus.hw.services;

import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Cocoon;

public interface CocoonToButterflyTransformerService {
    Butterfly transform (Cocoon cocoon);
}
