package bg.duosoft.uniapplicationdemo.validators.config;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface DefaultValidation {

    int MAX_INPUT_LENGTH_XXS = 50;
    int MAX_INPUT_LENGTH_XS = 100;
    int MAX_INPUT_LENGTH_S = 200;
    int MAX_INPUT_LENGTH_M = 500;
    int MAX_INPUT_LENGTH_L = 1000;
    int MAX_INPUT_LENGTH_XL = 2000;
    int MAX_INPUT_LENGTH_XXL = 10000;
    int MAX_INPUT_LENGTH_255 = 255;
    int MAX_INPUT_LENGTH_20 = 20;
    int MAX_INPUT_LENGTH_4 = 4;
    
    default void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer, String message) {
        if (Objects.isNull(value) || (value instanceof Optional<?> o && o.isEmpty())) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfEmpty(List<ValidationError> errors, Object value, String pointer) {
        rejectIfEmpty(errors, value, pointer, "Empty");
    }

    default void rejectIfContentEmpty(List<ValidationError> errors, byte[] value, String pointer, String message) {
        if (Objects.isNull(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
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

    default void rejectIfMatchRegex(List<ValidationError> errors, String value, String regex, String pointer, String message) {
        if (StringUtils.hasText(value) && value.matches(regex)) {
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

    default <E> void rejectIfEmptyCollection(List<ValidationError> errors, List<E> value, String pointer) {
        if (CollectionUtils.isEmpty(value)) {
            errors.add(ValidationError.builder().pointer(pointer).message("Empty").build());
        }
    }

    default void rejectIfBothAreNotTrue(List<ValidationError> errors, Boolean first, Boolean second, String pointer, String message) {
        if ((Objects.isNull(first) || !first) && (Objects.isNull(second) || !second)) {
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

    default void rejectIfFirstDateIsAfterSecond(List<ValidationError> errors, LocalDate firstDate, LocalDate secondDate, String pointer, String message) {
        if (firstDate.isAfter(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfFirstDateIsBeforeSecond(List<ValidationError> errors, LocalDate firstDate, LocalDate secondDate, String pointer, String message) {
        if (firstDate.isBefore(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfBothDatesAreEquals(List<ValidationError> errors, LocalDate firstDate, LocalDate secondDate, String pointer, String message) {
        if (firstDate.isEqual(secondDate)) {
            errors.add(ValidationError.builder().pointer(pointer).message(message).build());
        }
    }

    default void rejectIfFirstDateIsAfterSecond(List<ValidationError> errors, Date firstDate, Date secondDate, String pointer, String message) {
        if (firstDate.after(secondDate)) {
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
