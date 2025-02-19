package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        IntStream.range(1, questions.size() + 1)
                .mapToObj(k -> questionConverter.convertQuestionToString(k, questions.get(k - 1)))
                .forEach(ioService::printLine);
    }
}