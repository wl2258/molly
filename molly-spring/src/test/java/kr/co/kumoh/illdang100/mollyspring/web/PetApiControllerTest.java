package kr.co.kumoh.illdang100.mollyspring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.medication.MedicationHistory;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.*;
import kr.co.kumoh.illdang100.mollyspring.domain.surgery.SurgeryHistory;
import kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.VaccinationHistory;
import kr.co.kumoh.illdang100.mollyspring.dto.medication.MedicationReqDto;
import kr.co.kumoh.illdang100.mollyspring.dto.surgery.SurgeryReqDto;
import kr.co.kumoh.illdang100.mollyspring.dto.vaccination.VaccinationReqDto;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.image.PetImageRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.medication.MedicationRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.pet.PetRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.surgery.SurgeryRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.vaccination.VaccinationRepository;
import kr.co.kumoh.illdang100.mollyspring.security.dummy.DummyObject;
import kr.co.kumoh.illdang100.mollyspring.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static kr.co.kumoh.illdang100.mollyspring.dto.pet.PetReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class PetApiControllerTest extends DummyObject {

    @Autowired
    EntityManager em;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private PetService petService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired private PetImageRepository petImageRepository;
    @Autowired private SurgeryRepository surgeryRepository;
    @Autowired private VaccinationRepository vaccinationRepository;
    @Autowired private MedicationRepository medicationRepository;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @DisplayName("반려동물 정보 등록")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void savePet_success() throws Exception {
        MedicationReqDto.MedicationRequest medication1 = new MedicationReqDto.MedicationRequest("medication1", LocalDate.now().minusWeeks(1), LocalDate.now().minusDays(2));
        MedicationReqDto.MedicationRequest medication2 = new MedicationReqDto.MedicationRequest("medication2", LocalDate.now().minusMonths(1), LocalDate.now().minusWeeks(2));
        List<MedicationReqDto.MedicationRequest> medicationList = List.of(medication1, medication2);

        SurgeryReqDto.SurgeryRequest surgery1 = new SurgeryReqDto.SurgeryRequest("surgery1", LocalDate.now().minusMonths(2));
        SurgeryReqDto.SurgeryRequest surgery2 = new SurgeryReqDto.SurgeryRequest("surgery2", LocalDate.now().minusYears(3));
        List<SurgeryReqDto.SurgeryRequest> surgeryList = List.of(surgery1, surgery2);

        VaccinationReqDto.VaccinationRequest vaccination1 = new VaccinationReqDto.VaccinationRequest("vaccination1", LocalDate.now().minusDays(6));
        VaccinationReqDto.VaccinationRequest vaccination2 = new VaccinationReqDto.VaccinationRequest("vaccination2", LocalDate.now().minusYears(1));
        List<VaccinationReqDto.VaccinationRequest> vaccinationList = List.of(vaccination1, vaccination2);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/pet")
                .param("petType", "CAT")
                .param("petName", "나비")
                .param("species", "ABYSSINIAN")
                .param("birthdate", "2021-01-23")
                .param("gender", "FEMALE")
                .param("neuteredStatus", "true")
                .param("weight", String.valueOf(5.1))
                .param("caution", "털이 잘 빠짐")
                .content(medicationList.toString())
                .content(vaccinationList.toString())
                .content(surgeryList.toString())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("등록 실패 - 이미 등록된 반려동물인 경우")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void savePet_failure_v2() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/pet")
                .param("petName", "몰리")
                .param("petType", "DOG")
                .param("species", DogEnum.CHIHUAHUA.toString())
                .param("weight", String.valueOf(3.5))
                .param("birthdate", LocalDate.now().minusYears(1).toString())
                .param("gender", "FEMALE")
                .param("neuteredStatus", "true")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("반려동물 정보 수정")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updatePet_success() throws Exception {

        //given
        Long petId = 1L;

        PetUpdateRequest updateRequest = new PetUpdateRequest(PetTypeEnum.DOG.toString(), "강아지", DogEnum.GRATE_DANE.toString(),
                LocalDate.now().minusYears(1), PetGenderEnum.FEMALE, true, 3.0, null, null, null, null);
        String requestBody = om.writeValueAsString(updateRequest);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/pet/" + petId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("수정 실패 - 존재하지 않는 반려동물인 경우")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updatePet_failure() throws Exception {

        //given
        Long petId = 56L;

        PetUpdateRequest updateRequest = new PetUpdateRequest(PetTypeEnum.DOG.toString(), "강아지", DogEnum.GRATE_DANE.toString(),
                LocalDate.now().minusYears(1), PetGenderEnum.FEMALE, true, 3.0, "", null, null, null);
        String requestBody = om.writeValueAsString(updateRequest);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/pet/" + petId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("수정 실패 - petName Name null")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updatePet_failure2() throws Exception {

        //given
        Long petId = 1L;

        PetUpdateRequest updateRequest = new PetUpdateRequest(PetTypeEnum.DOG.toString(), null, DogEnum.GRATE_DANE.toString(),
                LocalDate.now().minusYears(1), PetGenderEnum.FEMALE, true, 3.0, "", null, null, null);
        String requestBody = om.writeValueAsString(updateRequest);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/pet/111")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("반려동물 정보 상세보기")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void viewDetails_success() throws Exception {

        // given
        Long petId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/auth/pet/" + petId.toString()).contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        resultActions.andExpect(status().isOk());
    }


    @Test
    @DisplayName("상세보기 실패 - 존재하지 않는 반려동물인 경우")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void viewDetails_failure() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/auth/pet/56").contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("반려동물 삭제")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletePet_success() throws Exception {

        // given
        Long petId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/auth/pet/" + petId.toString()).contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("반려동물 삭제 실패 - 존재하지 않는 반려동물인 경우")
    @WithUserDetails(value = "molly", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deletePet_failure() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/auth/pet/56").contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private void dataSetting() {
        Account account = newAccount("molly", "일당백");
        accountRepository.save(account);

        Pet pet = newPet(account, "몰리", LocalDate.now().minusYears(1), PetGenderEnum.FEMALE, true, 3.5, PetTypeEnum.DOG, null, DogEnum.CHIHUAHUA);
        petRepository.save(pet);

        MedicationHistory medication = newMockMedication(pet, "쓸개골 탈구");
        medicationRepository.save(medication);

        SurgeryHistory surgery = newMockSurgery(pet, "종양 제거 수술");
        surgeryRepository.save(surgery);

        VaccinationHistory vaccination = newMockVaccination(pet, "심장사상충");
        vaccinationRepository.save(vaccination);

        em.clear();
    }
}