package kr.co.kumoh.illdang100.mollyspring.repository.complaint;

import kr.co.kumoh.illdang100.mollyspring.domain.complaint.BoardComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardComplaintRepository extends JpaRepository<BoardComplaint, Long>, BoardComplaintRepositoryCustom {

    List<BoardComplaint> findByBoard_Id(Long boardId);

    void deleteByBoard_Id(Long boardId);
}
