package hello.jdbc.exception.translator;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import hello.jdbc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringExceptionTranslatorTest {

	DataSource dataSource;

	@BeforeEach
	void init() {
		dataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME,
			ConnectionConst.PASSWORD);
	}

	@Test
	void sqlExceptionErrorCode() {
		String sql = "select bad grammer";

		try {
			Connection conn = dataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			assertThat(e.getErrorCode()).isEqualTo(42122);
			int errorCode = e.getErrorCode();
			log.info("errorCode={}", errorCode);
		}
	}


	@Test
	void exceptionTranslator() {
		String sql = "select bad grammer";

		try {
			Connection con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.executeQuery();
		}catch (SQLException e) {
			assertThat(e.getErrorCode()).isEqualTo(42122);

			SQLErrorCodeSQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(
				dataSource);

			//BadSqlGrammerException
			DataAccessException resultEx = exTranslator.translate("select", sql, e);
			log.info("resultEx", resultEx);
			assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
		}
	}

}
