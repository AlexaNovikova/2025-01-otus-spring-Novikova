package ru.otus.common.error;

import lombok.Getter;

@Getter
public enum ShopExceptionCode {

    UNAUTHORIZED("unauthorized", "Ошибка при авторизации"),
    ACCESS_DENIED("access.denied", "Доступ запрещен"),
    NOT_FOUND("not.found", "Ресурс не найден"),
    ENTITY_ALREADY_EXISTS("entity.exists", "Ресурс уже существует"),
    UNPROCESSABLE_ENTITY_EXCEPTION("unprocessable.entity", "Ошибка при обработке введенных данных"),
    INTERNAL_SYSTEM_ERROR("internal.system.error", "Сервис недоступен. Попробуйте позднее"),
    BAD_REQUEST("bad.request", "Неверно введенные данные");

    private final String code;

    private final String text;

    ShopExceptionCode(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
