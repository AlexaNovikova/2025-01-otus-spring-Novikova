package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent(value = "Test App commands")
@RequiredArgsConstructor
public class TestAppCommands {

    private final TestService testService;

    private final LoginContext loginContext;

    private final StudentService studentService;

    private final ResultService resultService;

    @ShellMethod(value = "Login", key = {"l", "login"})
    public void login() {
        Student student = studentService.determineCurrentStudent();
        loginContext.login(student);
    }

    @ShellMethod(value = "Run tests", key = {"t", "test"})
    @ShellMethodAvailability(value = "isRunTestsCommandAvailable")
    public void runTests() {
        var testResult = testService.executeTestFor(loginContext.getStudent());
        resultService.showResult(testResult);
    }

    private Availability isRunTestsCommandAvailable() {
        return loginContext.isStudentLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Please login first");
    }
}
