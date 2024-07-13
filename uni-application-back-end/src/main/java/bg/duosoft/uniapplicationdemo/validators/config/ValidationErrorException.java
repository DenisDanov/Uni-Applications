package bg.duosoft.uniapplicationdemo.validators.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class ValidationErrorException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationErrorException(List<ValidationError> errors) {
        super("Validation errors: " + errors.stream().map(ValidationError::toString).collect(Collectors.joining("\n")));
        this.errors = errors;
    }

    public boolean hasValidationErrorPointer(String pointer) {
        return errors.stream().anyMatch(r -> r.getPointer().equals(pointer));
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "errors=\n" + (errors == null ? "" : errors.stream().map(ValidationError::toString).collect(Collectors.joining("\n"))) +
                "\n}";
    }
}
