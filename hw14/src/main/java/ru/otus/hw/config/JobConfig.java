package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.jpa.Comment;
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.processors.CommentItemProcessor;
import ru.otus.hw.repositories.jpa.AuthorRepository;
import ru.otus.hw.repositories.jpa.BookRepository;
import ru.otus.hw.repositories.jpa.CommentRepository;
import ru.otus.hw.repositories.jpa.GenreRepository;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.processors.AuthorItemProcessor;
import ru.otus.hw.processors.BookItemProcessor;
import ru.otus.hw.processors.GenreItemProcessor;
import ru.otus.hw.services.ClearCacheService;

import java.util.Collections;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String LIBRARY_MIGRATION_JOB = "libraryMigrationJob";

    private static final int CHUNK_SIZE = 2;

    private final Map<String, Sort.Direction> sortsMap = Collections.singletonMap("id", Sort.Direction.ASC);

    private final JobRepository jobRepository;

    private final MongoTemplate mongoTemplate;

    private final PlatformTransactionManager platformTransactionManager;

    private final AuthorRepository jpaAuthorRepository;

    private final GenreRepository jpaGenreRepository;

    private final BookRepository jpaBookRepository;

    private final CommentRepository jpaCommentRepository;

    private final ClearCacheService clearCacheService;

    @StepScope
    @Bean
    public RepositoryItemReader<Author> authorItemReader() {
        RepositoryItemReader<Author> reader = new RepositoryItemReader<>();
        reader.setName("authorReader");
        reader.setSort(sortsMap);
        reader.setRepository(jpaAuthorRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Genre> genreItemReader() {
        RepositoryItemReader<Genre> reader = new RepositoryItemReader<>();
        reader.setName("genreReader");
        reader.setSort(sortsMap);
        reader.setRepository(jpaGenreRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Book> bookItemReader() {
        RepositoryItemReader<Book> reader = new RepositoryItemReader<>();
        reader.setName("bookReader");
        reader.setSort(sortsMap);
        reader.setRepository(jpaBookRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Comment> commentItemReader() {
        RepositoryItemReader<Comment> reader = new RepositoryItemReader<>();
        reader.setName("commentReader");
        reader.setSort(sortsMap);
        reader.setRepository(jpaCommentRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    @StepScope
    @Bean
    public MongoItemWriter<MongoAuthor> authorWriter() {
        MongoItemWriter<MongoAuthor> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("authors");
        return writer;
    }

    @StepScope
    @Bean
    public MongoItemWriter<MongoGenre> genreWriter() {
        MongoItemWriter<MongoGenre> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("genres");
        return writer;
    }

    @StepScope
    @Bean
    public MongoItemWriter<MongoBook> bookWriter() {
        MongoItemWriter<MongoBook> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("books");
        return writer;
    }

    @StepScope
    @Bean
    public MongoItemWriter<MongoComment> commentWriter() {
        MongoItemWriter<MongoComment> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("comments");
        return writer;
    }

    @Bean
    public Job libraryMigrationJob(Step transformAuthorStep,
                                   Step transformGenreStep,
                                   Step transformBookStep,
                                   Step transformCommentStep) {
        return new JobBuilder(LIBRARY_MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorStep)
                .next(transformGenreStep)
                .next(transformBookStep)
                .next(transformCommentStep)
                .next(cleanCacheStep())
                .end()
                .build();
    }


    @Bean
    public Step transformAuthorStep(RepositoryItemReader<Author> reader,
                                    MongoItemWriter<MongoAuthor> writer,
                                    AuthorItemProcessor processor) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<Author, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformGenreStep(RepositoryItemReader<Genre> reader,
                                   MongoItemWriter<MongoGenre> writer,
                                   GenreItemProcessor processor) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<Genre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformBookStep(RepositoryItemReader<Book> reader,
                                  MongoItemWriter<MongoBook> writer,
                                  BookItemProcessor processor) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<Book, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformCommentStep(RepositoryItemReader<Comment> reader,
                                     MongoItemWriter<MongoComment> writer,
                                     CommentItemProcessor processor) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<Comment, MongoComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step cleanCacheStep() {
        return new StepBuilder("cleanCacheStep", jobRepository)
                .tasklet(cleanCacheTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanCacheTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(clearCacheService);
        adapter.setTargetMethod("clearAllCaches");
        return adapter;
    }

}
