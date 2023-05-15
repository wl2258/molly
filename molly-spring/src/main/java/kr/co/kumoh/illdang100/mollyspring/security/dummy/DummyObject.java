package kr.co.kumoh.illdang100.mollyspring.security.dummy;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.board.Board;
import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.comment.Comment;
import kr.co.kumoh.illdang100.mollyspring.domain.liky.Liky;
import kr.co.kumoh.illdang100.mollyspring.domain.medication.MedicationHistory;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.*;
import kr.co.kumoh.illdang100.mollyspring.domain.surgery.SurgeryHistory;
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

    protected Board newBoard(Account account, String title, String content,
                             BoardEnum category, PetTypeEnum petType, boolean isNotice) {

        return Board.builder()
                .account(account)
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

    protected Liky newLiky(Board board, Long accountId, BoardRepository boardRepository) {

        board.increaseViews();
        board.increaseLikyCnt();

        if (boardRepository != null) {
            boardRepository.save(board);
        }

        return new Liky(board, accountId);
    }

    protected Comment newComment(Board board, String commentContent,
                                 Long accountId, BoardRepository boardRepository) {

        board.increaseViews();
        board.increaseCommentCnt();

        if (boardRepository != null) {
            boardRepository.save(board);
        }

        return new Comment(board, commentContent, accountId);
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
