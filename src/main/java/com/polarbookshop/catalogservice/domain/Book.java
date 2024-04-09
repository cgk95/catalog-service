package com.polarbookshop.catalogservice.domain;

import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "books")
public record Book(
        @Id
        Long id,

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
        Double price,

        String publisher,

        @CreatedDate
        Instant createdDate, // 엔티티가 생성된 날짜와 시간

        @LastModifiedDate
        Instant lastModifiedDate, // 엔티티가 마지막으로 수정된 날짜와 시간

        @Version
        int version // 낙관적 락을 위해 사용되는 엔티티 버전 번호
) {
    public static Book of(String isbn, String title, String author, Double price, String publisher
    ) {
        return new Book(null, isbn, title, author, price, publisher,
                null, null,
                0); // ID 가 null 이고 버전이 0이면 Spring Data JDBC 는 새로운 엔티티로 인식함.
    }

}
