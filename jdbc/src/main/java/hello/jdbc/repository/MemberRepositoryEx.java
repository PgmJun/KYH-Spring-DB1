package hello.jdbc.repository;

import java.sql.SQLException;

import hello.jdbc.domain.Member;

public interface MemberRepositoryEx {
	Member save(Member member) throws SQLException;
	Member findById(String memberId) throws SQLException;
	void update(Member member) throws SQLException;
	void delete(Member member) throws SQLException;

}
