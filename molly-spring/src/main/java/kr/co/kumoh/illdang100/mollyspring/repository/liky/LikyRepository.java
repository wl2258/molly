package kr.co.kumoh.illdang100.mollyspring.repository.liky;

import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikyRepository extends JpaRepository<Liky, Long> {

    boolean existsByAccountIdAndBoard_Id(Long accountId, Long boardId);

    void deleteByAccountIdAndBoard_Id(Long accountId, Long boardId);

    List<Liky> findByBoard_Id(Long boardId);
}
