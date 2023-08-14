package hello.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

	private final MemberRepositoryV2 memberRepository;
	private final DataSource dataSource;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Connection conn = dataSource.getConnection();
		try {
			conn.setAutoCommit(false); //트랜잭션 시작
			//비즈니스 로직 수행
			bizLogic(conn, fromId, toId, money);
			conn.commit(); // 성공시 커밋
		} catch (Exception e) {
			conn.rollback(); // 실패시 롤백
			throw new IllegalStateException(e);
		} finally {
			release(conn);
		}


	}

	private void bizLogic(Connection conn, String fromId, String toId, int money) throws SQLException {
		Member fromMember = memberRepository.findById(conn, fromId);
		Member toMember = memberRepository.findById(conn, toId);

		memberRepository.update(conn, fromId, fromMember.getMoney() - money);
		validation(toMember);
		memberRepository.update(conn, toId, toMember.getMoney() + money);
	}

	private void release(Connection conn) {
		if(conn != null) {
			try {
				conn.setAutoCommit(true); // setAutoCommit은 default가 true이기 때문에,
				// 해당 커넥션이 풀로 돌아가서 다른 곳에서 호출됐을때 설정없이 false상태로 사용됨을 막기 위해 true로 변경 후 close() 수행
				conn.close();
			} catch (Exception e) {
				log.info("error", e);
			}
		}
	}

	private void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체중 예외 발생");
		}
	}
}
