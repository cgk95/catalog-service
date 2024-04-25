package com.polarbookshop.catalogservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polarbookshop.catalogservice.domain.Book;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests {
    // Customer
    private static KeycloakToken bjornTokens;
    // Customer and employee
    private static KeycloakToken isabelleTokens;

    @Autowired
    private WebTestClient webTestClient;


    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:23.0")
            .withRealmImportFile("/test-realm-config.json");

    /**
     * @param registry 키클록 발행자 URI 가 테스트 키클록 인스턴스를 가리키도록 변경
     */
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/PolarBookshop");
    }

    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder() // 키클록을 호출하기 위한 WebClient 통신
                .baseUrl(keycloakContainer.getAuthServerUrl() + "/realms/PolarBookshop/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        isabelleTokens = authenticateWith("isabelle", "password", webClient);
        bjornTokens = authenticateWith("bjorn", "password", webClient);
    }

    @Test
    @DisplayName("점원이 카탈로그에 책을 추가하는데 성공하는 통합테스트")
    void whenPostRequestAuthorizedThenBookCreated() {
        var expectedBook = Book.of("1234567890", "Test Book", "author", 9.99, "제이펍");

        webTestClient
                .post()
                .uri("/books")
                .headers(headers ->
                        headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
                });
    }

    @Test
    @DisplayName("손님이 카탈로그에 책을 추가하는데 실패하는 통합테스트")
    void whenPostRequestUnAuthorizedThenBook403() {
        var expectedBook = Book.of("1234567890", "Test Book", "author", 9.99, "제이펍");

        webTestClient
                .post()
                .uri("/books")
                .headers(headers ->
                        headers.setBearerAuth(bjornTokens.accessToken()))
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        var expectedBook = Book.of("1234567890", "Test Book", "author", 9.99, "제이펍");

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block(); // 결과가 나올 때까지 막고 기다린다.
    }

    private record KeycloakToken(String accessToken) {

        @JsonCreator // 이 생성자를 통해 역직렬화하도록 지시
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }

    }
}
