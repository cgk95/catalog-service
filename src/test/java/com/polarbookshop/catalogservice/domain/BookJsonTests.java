package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> jacksonTester;

    @Test
    @DisplayName("Book 객체를 JSON으로 직렬화하는 테스트")
    void testSerialize() throws Exception {
        Book book = new Book("1234567890", "Test Book", "author", 9.99);
        // Serialize book to JSON
        var jsonContent = jacksonTester.write(book);
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                               .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                               .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                               .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                               .isEqualTo(book.price());
    }

    @Test
    @DisplayName("Json 요청 본문을 엔티티로 잘 변환하는지")
    void testDeserialize() throws Exception {
        var content = """
                {
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.90
                }
                """;

        assertThat(jacksonTester.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Book("1234567890", "Title", "Author", 9.90));
    }
}
