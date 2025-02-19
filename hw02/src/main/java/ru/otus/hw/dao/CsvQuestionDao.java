package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> questions;
        try (var fileInputStream = this.getClass().getResourceAsStream(fileNameProvider.getTestFileName())) {
            if (isNull(fileInputStream)) {
                return List.of();
            }
            var inputStreamReader = new InputStreamReader(fileInputStream);
            questions = new CsvToBeanBuilder(inputStreamReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException(e.getMessage());
        }
        return questions.stream().map(QuestionDto::toDomainObject).toList();
    }
}
