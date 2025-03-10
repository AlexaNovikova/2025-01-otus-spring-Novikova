package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Тестирование класса CsvQuestionDao")
@SpringBootTest
class CsvQuestionDaoTest {

    @MockitoBean
    private AppProperties fileNameProvider;

    @Autowired
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