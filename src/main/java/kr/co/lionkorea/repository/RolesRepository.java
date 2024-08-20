package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findByRoleName(Role role);
}
