package hello.jdbc.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV0Test {

	MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

	@Test
	void crud() throws SQLException {

		final String memberId = "member1";

		//save
		Member member = new Member(memberId, 10000);
		repositoryV0.save(member);

		//fidnById
		Member findMember = repositoryV0.findById(member.getMemberId());
		log.info("findMember={}", findMember);
		Assertions.assertThat(findMember).isEqualTo(member);

		//update
		repositoryV0.update(memberId, 20000);
		Member updateMember = repositoryV0.findById(memberId);
		Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

		//delete
		repositoryV0.delete(memberId);
		Assertions.assertThatThrownBy(() -> repositoryV0.findById(memberId))
			.isInstanceOf(NoSuchElementException.class);
	}


}