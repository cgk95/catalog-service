package com.polarbookshop.catalogservice.controller;

import static org.mockito.BDDMockito.given;

import com.polarbookshop.catalogservice.config.SecurityConfig;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    @DisplayName("카탈로그에 책이 존재하지 않을 때 404 응답을 반환하는 테스트")
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "1919291291291";
        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void whenDeleteBookWithEmployeeRoleThenShouldReturn204() throws Exception {
        var isbn = "7373731394";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{isbn}", isbn)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void whenDeleteBookWithCustomerRoleThenShouldReturn403() throws Exception {
        var isbn = "7373731394";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{isbn}", isbn)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void whenDeleteBookNotAuthenticatedThenShouldReturn401() throws Exception {
        var isbn = "7373731394";
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
