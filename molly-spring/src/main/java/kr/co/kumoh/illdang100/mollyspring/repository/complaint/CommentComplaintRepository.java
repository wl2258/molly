package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import kr.co.kumoh.illdang100.mollyspring.domain.complaint.CommentComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentComplaintRepository extends JpaRepository<CommentComplaint, Long>, CommentComplaintRepositoryCustom {

    List<CommentComplaint> findByComment_IdIn(List<Long> commentIds);

    List<CommentComplaint> findByComment_Id(Long commentId);
}
