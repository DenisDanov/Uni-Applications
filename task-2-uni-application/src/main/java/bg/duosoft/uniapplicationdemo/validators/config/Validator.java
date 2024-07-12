package bg.duosoft.uniapplicationdemo.validators.config;

import java.util.ArrayList;
import java.util.List;

public interface Validator<T> extends DefaultValidation {
    List<ValidationError> validate(T obj, Object... args);

    default List<ValidationError> initErrorList() {
        return new ArrayList<>();
    }

    default void validate(List<ValidationError> errors, T obj, Object... args) {
        errors.addAll(validate(obj, args));
    }

}
