package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata") // 이 클래스를 testdata 프로파일에 할당
@RequiredArgsConstructor
public class BookDataLoader {
    private final BookRepository bookRepository;

    @EventListener(ApplicationReadyEvent.class)
    // ApplicationReadyEvent 발생시 테스트 데이터 생성이 시작. 이 이벤트는 애플리케이션 시작단계가 완료되면 발생한다
    public void loadBookTestData() {
        bookRepository.deleteAll();
        var book1 = Book.of("1491976274", "Building Microservices", "Sam Newman", 15.41,"제이펍");
        var book2 = Book.of("1680502395", "Release It!", "Michael T. Nygard", 25.37,"제이펍");
        bookRepository.saveAll(List.of(book1, book2));
    }


}
