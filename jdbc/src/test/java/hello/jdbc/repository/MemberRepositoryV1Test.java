package hello.jdbc.repository;

import static hello.jdbc.connection.ConnectionConst.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV1Test {

	MemberRepositoryV1 repository;

	@BeforeEach
	void beforeEach() {
		// 기본 DriverManager - 항상 새로운 커넥션 흭득
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);

		repository = new MemberRepositoryV1(dataSource);
	}

	@Test
	void crud() throws SQLException, InterruptedException {

		final String memberId = "member11";

		//save
		Member member = new Member(memberId, 10000);
		repository.save(member);

		//fidnById
		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember={}", findMember);
		Assertions.assertThat(findMember).isEqualTo(member);

		//update
		repository.update(memberId, 20000);
		Member updateMember = repository.findById(memberId);
		Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

		//delete
		repository.delete(memberId);
		Assertions.assertThatThrownBy(() -> repository.findById(memberId))
			.isInstanceOf(NoSuchElementException.class);

		Thread.sleep(1000);
	}


}