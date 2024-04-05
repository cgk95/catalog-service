package com.polarbookshop.catalogservice.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public record Book(
        @NotBlank(message = "ISBN 은 필수입니다")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "ISBN 은 10자리 또는 13자리 숫자여야 합니다 (ISBN-10 또는 ISBN-13 형식)")
        String isbn,

        @NotBlank(message = "책 제목은 필수입니다")
        String title,

        @NotBlank(message = "저자는 필수입니다")
        String author,

        @NotNull(message = "가격은 필수입니다")
        @Positive(message = "가격은 0보다 커야 합니다")
        Double price
) {
}
