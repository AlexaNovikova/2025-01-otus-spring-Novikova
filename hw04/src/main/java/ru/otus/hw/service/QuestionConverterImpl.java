package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionConverterImpl implements QuestionConverter {

    private static final String FORMATTED_QUESTION = " %d: %s\n";

    private static final String FORMATTED_ANSWER = "%d. %s \n";

    private final LocalizedMessagesService localizedMessagesService;

    public QuestionConverterImpl(@Qualifier(value = "localizedMessagesServiceImpl")
                                 LocalizedMessagesService localizedMessagesService) {
        this.localizedMessagesService = localizedMessagesService;
    }

    @Override
    public String convertQuestionToString(int number, Question question) {
        var formattedQuestionText = localizedMessagesService.getMessage("QuestionConverter.question") +
                String.format(FORMATTED_QUESTION, number, question.text());
        var answers = question.answers();
        var formattedAnswersList = IntStream.range(1, answers.size() + 1)
                .mapToObj(k -> String.format(FORMATTED_ANSWER, k, answers.get(k - 1).text()))
                .collect(Collectors.joining());
        return formattedQuestionText + formattedAnswersList;
    }
}
