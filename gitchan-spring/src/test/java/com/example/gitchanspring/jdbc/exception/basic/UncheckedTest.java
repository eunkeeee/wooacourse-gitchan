package com.example.gitchanspring.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch() {
        final Service service = new Service();
        assertDoesNotThrow(service::callCatch);
    }

    @Test
    void unchecked_throw() {
        final Service service = new Service();
        Assertions.assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyUncheckedException.class);
    }

    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(final String message) {
            super(message);
        }
    }

    static class Service {

        private Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("handle exception, message={}", e.getMessage(), e);
            }
        }

        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {

        public void call() {
            throw new MyUncheckedException("ex");
        }
    }
}
