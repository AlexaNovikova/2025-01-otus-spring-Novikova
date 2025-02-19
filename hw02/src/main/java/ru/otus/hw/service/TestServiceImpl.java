package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.CheckAnswerException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        var number = 1;
        for (var question : questions) {
            var isAnswerValid = false;
            var answer = printQuestionAndGetTheAnswer(question,number++);
            isAnswerValid = checkAnswer(answer, question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private int printQuestionAndGetTheAnswer(Question question, int number) {
        ioService.printLine(questionConverter.convertQuestionToString(number, question));
        return ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                "Input your answer: ",
                "Error input. Please input number from 1 to " + question.answers().size());
    }

    private boolean checkAnswer(int answer, Question question) {
        if (answer > question.answers().size() || answer < 1) {
            throw new CheckAnswerException("Answer number is out of range.");
        }
        return question.answers().get(answer - 1).isCorrect();
    }
}
