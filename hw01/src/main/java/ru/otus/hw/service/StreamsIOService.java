package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.io.PrintStream;
import java.util.List;

public class StreamsIOService implements IOService {
    private final PrintStream printStream;

    public StreamsIOService(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public void printQuestions(List<Question> questionList) {
        int number = 1;
        for (Question question : questionList) {
            printFormattedLine("Question %s: %s", number++, question.text());
            for (int i = 0; i < question.answers().size(); i++) {
                printFormattedLine("%s. %s.", i + 1, question.answers().get(i).text());
            }
            printLine("");
        }
    }
}
