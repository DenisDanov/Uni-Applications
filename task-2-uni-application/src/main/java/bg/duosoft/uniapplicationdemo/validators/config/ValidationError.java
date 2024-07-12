package bg.duosoft.uniapplicationdemo.validators.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ValidationError {
    private final String pointer;
    private final String message;

    private ValidationError(String pointer, String message) {
        this.pointer = pointer;
        this.message = message;
    }

    public static ValidationError create(String pointer, String message) {
        return new ValidationError(pointer, message);
    }
}