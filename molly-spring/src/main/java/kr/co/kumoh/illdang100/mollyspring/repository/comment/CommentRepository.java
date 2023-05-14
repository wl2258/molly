package kr.co.kumoh.illdang100.mollyspring.repository.comment;

import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoard_Id(Long boardId);

    List<Comment> findByBoard_IdOrderByCreatedDate(Long boardId);
}
