package hello.jdbc.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ConnectionUtilTest {

	@Test
	void connection() {
		Connection connection = ConnectionUtil.getConnection();
		Assertions.assertThat(connection).isNotNull();
	}

}