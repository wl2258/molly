package kr.co.kumoh.illdang100.mollyspring.repository.board;

import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @EntityGraph(attributePaths = "account")
    Optional<Board> findWithAccountById(Long boardId);

    List<Board> findByAccount_Id(Long accountId);

    List<Board> findByAccountEmail(String accountEmail);
}
