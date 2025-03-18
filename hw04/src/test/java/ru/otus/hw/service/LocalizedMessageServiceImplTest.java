package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Locale;

import static org.mockito.Mockito.when;

@DisplayName("Тестирование класса LocalizedIOService")
@SpringBootTest
public class LocalizedMessageServiceImplTest {

    @MockitoBean
    private AppProperties localeConfig;

    @Autowired
    private LocalizedMessagesServiceImpl localizedMessagesService;


    @Test
    @DisplayName("Проверка, что метод вернет сообщение на английском при установленной английской локали")
    void returnEnglishStringFoEnLocale() {
        when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("en-US"));
        String result = localizedMessagesService.getMessage("ResultService.passed.test");
        assertThat(result).isEqualTo("Congratulations! You passed test!");
    }

    @Test
    @DisplayName("Проверка, что метод вернет сообщение на русском языке при установленной русской локали")
    void returnRussianStringFoRuLocale() {
        when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("ru-RU"));
        String result = localizedMessagesService.getMessage("ResultService.passed.test");
        assertThat(result).isEqualTo("Поздравляем! Вы прошли тест!");
    }
}
