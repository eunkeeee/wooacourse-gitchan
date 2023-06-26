package com.example.gitchanspring.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch() {
        final Service service = new Service();
        assertDoesNotThrow(service::callCatch);
    }

    @Test
    void checked_throw() {
        final Service service = new Service();
        assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyCheckedException.class);
    }

    static class MyCheckedException extends Exception {

        public MyCheckedException(final String message) {
            super(message);
        }
    }

    static class Service {
        private Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("handle exception, message={}", e.getMessage(), e);
            }
        }

        private void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
