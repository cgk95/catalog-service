package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모두 유효하면 밸리데이션 통과")
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var book = Book.of("1234567890", "Title", "Author", 9.99,"제이펍");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("ISBN이 정의되었지만 유효하지 않은 경우 밸리데이션 실패")
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        var book = Book.of("아아~섹스하고싶다", "큰 소리로 말하지마, 등신아", "author", 9.99,"제이펍");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ISBN 은 10자리 또는 13자리 숫자여야 합니다 (ISBN-10 또는 ISBN-13 형식)");
    }
}
