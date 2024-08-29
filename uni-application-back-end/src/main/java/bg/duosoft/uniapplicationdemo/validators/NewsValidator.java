package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.NewsDTO;
import bg.duosoft.uniapplicationdemo.repositories.NewsRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NewsValidator implements Validator<NewsDTO> {

    private final NewsRepository newsRepository;

    @Override
    public List<ValidationError> validate(NewsDTO newsDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        // Validate that the newsDTO is not null
        rejectIfEmpty(errors, newsDTO, "newsDTO", "m.validator.newsDTO.empty");

        if (errors.isEmpty()) {
            // Validate authorUsername
            rejectIfEmptyString(errors, newsDTO.getAuthorUsername(), "authorUsername", "m.validator.authorUsername.empty");
            rejectIfStringLengthBigger(errors, newsDTO.getAuthorUsername(), 255, "authorUsername", "m.validator.authorUsername.length.tooLong");

            // Validate newsHeader
            rejectIfEmptyString(errors, newsDTO.getNewsHeader(), "newsHeader", "m.validator.newsHeader.empty");
            rejectIfStringLengthBigger(errors, newsDTO.getNewsHeader(), 1000, "newsHeader", "m.validator.newsHeader.length.tooLong");

            // Validate newsText
            rejectIfEmptyString(errors, newsDTO.getNewsText(), "newsText", "m.validator.newsText.empty");
        }

        if (isCreate != null && errors.isEmpty()) {
            if (!isCreate) {
                // When updating, ID should not be null
                rejectIfTrue(errors, !Objects.nonNull(newsDTO.getId()), "id", "m.validation.id.empty");

                // Example: Check if the news item already exists in the database
                rejectIfTrue(errors, !newsRepository.existsById(newsDTO.getId()), "id", "m.validation.id.not.exists");
            }
        }

        return errors;
    }
}
