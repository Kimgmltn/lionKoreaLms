package kr.co.lionkorea.repository;


import kr.co.lionkorea.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryDslRepository {
}
