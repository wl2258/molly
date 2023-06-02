package kr.co.kumoh.illdang100.mollyspring.repository.suspension;

import kr.co.kumoh.illdang100.mollyspring.domain.suspension.Suspension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspensionRepository extends JpaRepository<Suspension, Long> {

    boolean existsByBoardId(Long boardId);
    boolean existsByCommentId(Long commentId);
}
