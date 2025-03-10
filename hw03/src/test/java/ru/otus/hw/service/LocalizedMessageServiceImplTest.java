package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.hw.config.LocaleConfig;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Locale;

import static org.mockito.Mockito.when;

@DisplayName("Тестирование класса LocalizedIOService")
@ExtendWith(MockitoExtension.class)
public class LocalizedMessageServiceImplTest {

    @Mock
    private LocaleConfig localeConfig;

    @Spy
    private ReloadableResourceBundleMessageSource messageSource;

    @InjectMocks
    private LocalizedMessagesServiceImpl localizedMessagesService;

    @BeforeEach
    void init() {
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
    }

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
