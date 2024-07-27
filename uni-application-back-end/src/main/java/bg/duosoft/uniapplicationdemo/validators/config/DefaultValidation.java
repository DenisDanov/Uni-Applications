package bg.duosoft.uniapplicationdemo.validators.config;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface DefaultValidation {

    int MAX_INPUT_LENGTH_XL = 2000;
    
    default void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer, String message) {
        if (Objects.isNull(value) || (value instanceof Optional<?> o && o.isEmpty())) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer) {
        rejectIfEmpty(errors, value, pointer, "Empty");
    }

    default void rejectIfNumberIsNull(List<ValidationError> errors, Integer value, String pointer, String message) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfEmptyString(List<ValidationError> errors, String value, String pointer, String message) {
        if (!StringUtils.hasText(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfEmptyDate(List<ValidationError> errors, Date value, String pointer, String message) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfEmptyBoolean(List<ValidationError> errors, Boolean value, String pointer, String message) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default boolean rejectIfEmptyString(List<ValidationError> errors, String value, String pointer) {
        if (!StringUtils.hasText(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message("Empty").build());
            return true;
        }
        return false;
    }

    default void rejectIfNotMatchRegex(List<ValidationError> errors, String value, String regex, String pointer, String message) {
        if (StringUtils.hasText(value) && !value.matches(regex)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfNotPositiveNumber(List<ValidationError> errors, Integer number, String pointer, String message) {
        if (Objects.nonNull(number) && number < 1) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default <E> void rejectIfEmptyCollection(List<ValidationError> errors, List<E> value, String pointer, String message) {
        if (CollectionUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfTrue(List<ValidationError> errors, Boolean value, String pointer, String message) {
        if (Objects.nonNull(value) && value) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfFalse(List<ValidationError> errors, Boolean value, String pointer, String message) {
        if (Objects.nonNull(value) && !value) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfFirstDateIsBeforeSecond(List<ValidationError> errors, Date firstDate, Date secondDate, String pointer, String message) {
        if (firstDate.before(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void reject(List<ValidationError> errors, String pointer, String message) {
        errors.add(ValidationError.builder().pointer(pointer).message(message).build());
    }

    default void rejectIfStringLengthBigger(List<ValidationError> errors, String value, int maxLength, String pointer){
        if(StringUtils.hasText(value) && value.length() > maxLength){
            errors.add(ValidationError.builder().pointer(pointer).message("validation.charCount.invalid."+maxLength).build());
        }
    }

    default void rejectIfStringLengthBigger(List<ValidationError> errors, String value, int maxLength, String pointer, String message){
        if(StringUtils.hasText(value) && value.length() > maxLength){
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default boolean hasValidationErrorPointer(List<ValidationError> errors,String pointer) {
        return errors.stream().anyMatch(r -> r.getPointer().equals(pointer));
    }
}
