package com.polarbookshop.catalogservice.controller;

import static org.mockito.BDDMockito.given;

import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(BookController.class)
public class BookControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("카탈로그에 책이 존재하지 않을 때 404 응답을 반환하는 테스트")
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "1919291291291";
        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
               .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
