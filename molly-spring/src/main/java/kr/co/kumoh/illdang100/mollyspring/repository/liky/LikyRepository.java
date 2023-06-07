package kr.co.kumoh.illdang100.mollyspring.repository.liky;

import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikyRepository extends JpaRepository<Liky, Long> {

    boolean existsByAccountEmailAndBoard_Id(String accountEmail, Long boardId);

    void deleteByAccountEmailAndBoard_Id(String accountEmail, Long boardId);

    List<Liky> findByBoard_Id(Long boardId);
}
