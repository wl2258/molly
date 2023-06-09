package kr.co.kumoh.illdang100.mollyspring.security.dummy;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.BoardComplaint;
import kr.co.kumoh.illdang100.mollyspring.domain.complaint.ComplaintReasonEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import kr.co.kumoh.illdang100.mollyspring.domain.medication.MedicationHistory;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.*;
import kr.co.kumoh.illdang100.mollyspring.domain.surgery.SurgeryHistory;
import kr.co.kumoh.illdang100.mollyspring.domain.suspension.SuspensionDate;
import kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.VaccinationHistory;
import kr.co.kumoh.illdang100.mollyspring.repository.board.BoardRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

public class DummyObject {

    protected Account newAccount(String username, String nickname) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return Account.builder()
                .username(username)
                .password(encPassword)
                .email(username + "@naver.com")
                .nickname(nickname)
                .role(AccountEnum.CUSTOMER)
                .build();
    }

    protected Account newAdmin(String username, String nickname) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return Account.builder()
                .username(username)
                .password(encPassword)
                .email(username + "@naver.com")
                .nickname(nickname)
                .role(AccountEnum.ADMIN)
                .build();
    }

    protected Account newMockAccount(Long id, String username, String nickname, AccountEnum accountEnum) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return Account.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username + "@naver.com")
                .nickname(nickname)
                .role(accountEnum)
                .build();
    }

    protected BoardComplaint newBoardComplaint(Board board, String reporterEmail, String reportedEmail, ComplaintReasonEnum complaintReason) {

        return BoardComplaint.builder()
                .board(board)
                .reporterEmail(reporterEmail)
                .reportedEmail(reportedEmail)
                .complaintReason(complaintReason)
                .build();
    }

    protected SuspensionDate newSuspensionDate(String email, LocalDate expiryDate) {

        return new SuspensionDate(email, expiryDate);
    }

    protected SuspensionDate newMockSuspensionDate(Long id, String email, LocalDate expiryDate) {

        return new SuspensionDate(id, email, expiryDate);
    }

    protected Board newBoard(Account account, String title, String content,
                             BoardEnum category, PetTypeEnum petType, boolean isNotice) {

        return Board.builder()
                .account(account)
                .accountEmail(account.getEmail())
                .boardTitle(title)
                .boardContent(content)
                .category(category)
                .petType(petType)
                .views(0L)
                .likyCnt(0L)
                .commentCnt(0L)
                .hasImage(false)
                .isNotice(isNotice)
                .build();
    }

    protected Board newMockBoard(Long id, Account account, String title, String content,
                             BoardEnum category, PetTypeEnum petType, boolean isNotice) {

        return Board.builder()
                .id(id)
                .account(account)
                .accountEmail(account.getEmail())
                .boardTitle(title)
                .boardContent(content)
                .category(category)
                .petType(petType)
                .views(10L)
                .likyCnt(10L)
                .commentCnt(10L)
                .hasImage(false)
                .isNotice(isNotice)
                .build();
    }

    protected Liky newLiky(Board board, String accountEmail, BoardRepository boardRepository) {

        board.increaseViews();
        board.increaseLikyCnt();

        if (boardRepository != null) {
            boardRepository.save(board);
        }

        return new Liky(board, accountEmail);
    }

    protected Liky newMockLiky(Long id, Board board, String accountEmail, BoardRepository boardRepository) {

        board.increaseViews();
        board.increaseLikyCnt();

        if (boardRepository != null) {
            boardRepository.save(board);
        }

        return Liky.builder()
                .id(id)
                .board(board)
                .accountEmail(accountEmail)
                .build();
    }

    protected Comment newComment(Board board, String commentContent,
                                 String accountEmail, BoardRepository boardRepository) {

        board.increaseViews();
        board.increaseCommentCnt();

        if (boardRepository != null) {
            boardRepository.save(board);
        }

        return new Comment(board, commentContent, accountEmail);
    }

    protected Pet newPet(Account account, String petName, LocalDate birthdate, PetGenderEnum gender, boolean neuteredStatus, double weight, PetTypeEnum petType, String caution, DogEnum species) {
        return Dog.builder()
                .account(account)
                .petName(petName)
                .gender(gender)
                .neuteredStatus(neuteredStatus)
                .weight(weight)
                .petType(petType)
                .birthdate(birthdate)
                .caution(caution)
                .dogSpecies(species)
                .build();
    }

    protected Pet newMockPet(Account account, String petName, LocalDate birthdate, DogEnum species) {
        return Dog.builder()
                .account(account)
                .petName(petName)
                .gender(PetGenderEnum.FEMALE)
                .neuteredStatus(true)
                .weight(3.0)
                .petType(PetTypeEnum.DOG)
                .birthdate(birthdate)
                .caution(null)
                .dogSpecies(species)
                .build();
    }

    protected MedicationHistory newMockMedication(Pet pet, String medicationName) {
        return MedicationHistory.builder()
                .pet(pet)
                .medicationName(medicationName)
                .medicationStartDate(LocalDate.now().minusDays(5))
                .medicationEndDate(LocalDate.now())
                .build();
    }

    protected SurgeryHistory newMockSurgery(Pet pet, String surgeryName) {
        return SurgeryHistory.builder()
                .pet(pet)
                .surgeryName(surgeryName)
                .surgeryDate(LocalDate.now().minusYears(1))
                .build();
    }

    protected VaccinationHistory newMockVaccination(Pet pet, String vaccinationName) {
        return VaccinationHistory.builder()
                .pet(pet)
                .vaccinationName(vaccinationName)
                .vaccinationDate(LocalDate.now().minusWeeks(2))
                .build();
    }
}
