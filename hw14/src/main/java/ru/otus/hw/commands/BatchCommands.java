package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final Job libraryMigrationJob;

    private final JobLauncher jobLauncher;


    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigration", key = "sm")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution execution = jobLauncher.run(libraryMigrationJob,
                new JobParametersBuilder().toJobParameters());
        System.out.println(execution);
    }

}