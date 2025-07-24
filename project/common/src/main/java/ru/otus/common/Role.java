package ru.otus.common;

import lombok.Getter;

@Getter
public enum Role {

    CLIENT("client"),
    ADMIN("admin"),
    EMPLOYEE("employee");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
