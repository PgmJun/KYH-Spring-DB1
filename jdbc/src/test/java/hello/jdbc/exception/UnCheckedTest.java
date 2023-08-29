package hello.jdbc.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnCheckedTest {

	@Test
	void unchecked_catch() {
		Service service = new Service();
		service.callCatch();

	}

	@Test
	void unchecked_throw() {
		Service service = new Service();
		Assertions.assertThatThrownBy(() -> service.callThrow())
			.isInstanceOf(MyUncheckedException.class);

	}

	static class MyUncheckedException extends RuntimeException {
		public MyUncheckedException(String message) {
			super(message);
		}
	}

	/**
	 * UnChecked 예외는
	 * 예외를 잡거나, 던지지 않아도 된다.
	 * 예외를 잡지 않으면 자동으로 밖에 던진다.
	 */
	static class Service {
		Repository repository = new Repository();

		public void callCatch() {
			try {
				repository.call();
			} catch (MyUncheckedException e) {
				log.info("예외 처리, message = {}", e.getMessage(), e);
			}
		}

		/**
		 * 예외를 잡지 않아도 된다. 자연스레 상위로 넘어간다.
		 * Checked 예외와는 다르게 throws를 선언하지 않아도 된다.
		 */
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
