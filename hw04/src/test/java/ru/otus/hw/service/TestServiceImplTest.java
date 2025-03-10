package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование класса TestServiceImpl")
@SpringBootTest
class TestServiceImplTest {

    @Autowired
    private TestServiceImpl testService;

    @MockitoBean
    private LocalizedIOService localizedIOService;

    @MockitoBean
    private QuestionDao questionDao;

    private List<Question> questions;

    @BeforeEach
    void prepare() {
        var question = new Question("How should resources be loaded form jar in Java?", List.of(
                new Answer("ClassLoader#geResourceAsStream or ClassPathResource#getInputStream", true),
                new Answer("ClassLoader#geResource#getFile + FileReader", false),
                new Answer("Wingardium Leviosa", false)));

        questions = List.of(question);
        when(questionDao.findAll()).thenReturn(questions);
    }

    @Test
    @DisplayName("Проверяем, что результат тестов верно сохраняет имя и фамилию студента")
    void executeTestForStudent_andTestResultHaveCorrectNameAndSurname() {
        var student = new Student("Alexandra", "Novikova");

        var inputAnswer = 1;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);

        var result = testService.executeTestFor(student);
        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getStudent().firstName()).isEqualTo(student.firstName());
        assertThat(result.getStudent().lastName()).isEqualTo(student.lastName());
    }

    @Test
    @DisplayName("Проверяем, что результат тестов правильно сохраняет текст вопросов и ответов")
    void executeTestForStudent_andTestResultHaveCorrectQuestionsText() {
        var student = new Student("Alexandra", "Novikova");

        var inputAnswer = 1;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);
        var result = testService.executeTestFor(student);
        Question firstActualQuestionInTestResult = result.getAnsweredQuestions().get(0);
        Question firstExpectedQuestion = questions.get(0);
        assertThat(firstActualQuestionInTestResult.text()).isEqualTo(firstExpectedQuestion.text());
        assertThat(firstActualQuestionInTestResult.answers().size())
                .isEqualTo(firstExpectedQuestion.answers().size());
        assertThat(firstActualQuestionInTestResult.answers()).isEqualTo(firstExpectedQuestion.answers());
    }

    @Test
    @DisplayName("Проверяем, что результат тестов правильно определяет количество верных ответов")
    void executeTestForStudent_andHaveCorrectNumberOfCorrectAnswers() {
        var student = new Student("Alexandra", "Novikova");

        var inputAnswer = 1;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);
        var result = testService.executeTestFor(student);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);

        inputAnswer = 2;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);
        var result2 = testService.executeTestFor(student);
        assertThat(result2.getRightAnswersCount()).isEqualTo(0);
    }
}