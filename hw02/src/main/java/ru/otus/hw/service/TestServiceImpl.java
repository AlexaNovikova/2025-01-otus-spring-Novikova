package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

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
            var answer = printQuestionAndGetAnswer(question, number++);
            isAnswerValid = checkAnswer(answer, question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private int printQuestionAndGetAnswer(Question question, int number) {
        ioService.printLine(questionConverter.convertQuestionToString(number, question));
        return ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                "Input your answer: ",
                "Error input. Please input number from 1 to " + question.answers().size());
    }

    private boolean checkAnswer(int answerNumber, Question question) {
        return question.answers().get(answerNumber - 1).isCorrect();
    }
}
