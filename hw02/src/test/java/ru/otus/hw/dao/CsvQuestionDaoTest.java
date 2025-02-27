package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Тестирование класса CsvQuestionDao")
@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Проверка корректного парсинга файла с вопросами")
    @Test
    void findAll() {
        var expectedQuestionList = new ArrayList<Question>();
        var fileAddress = "/questions.csv";
        expectedQuestionList.add(
                new Question("Is there life on Mars?",
                        List.of(
                                new Answer("Science doesn't know this yet", true),
                                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                                new Answer("Absolutely not", false))));
        expectedQuestionList.add(
                new Question("How should resources be loaded form jar in Java?",
                        List.of(
                                new Answer("ClassLoader#geResourceAsStream or ClassPathResource#getInputStream", true),
                                new Answer("ClassLoader#geResource#getFile + FileReader", false),
                                new Answer("Wingardium Leviosa", false))));
        given(fileNameProvider.getTestFileName())
                .willReturn(fileAddress);
        List<Question> actualQuestionList = csvQuestionDao.findAll();
        assertThat(actualQuestionList).isEqualTo(expectedQuestionList);
    }
}