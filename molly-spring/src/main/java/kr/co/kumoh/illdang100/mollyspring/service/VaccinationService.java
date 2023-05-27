package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.pet.Pet;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.VaccinationHistory;
import kr.co.kumoh.illdang100.mollyspring.dto.vaccination.VaccinationRespDto.VaccinationResponse;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.pet.PetRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.vaccination.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum.*;
import static kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.CatVaccinationEnum.*;
import static kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.DogVaccinationEnum.*;
import static kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.RabbitVaccinationEnum.*;
import static kr.co.kumoh.illdang100.mollyspring.domain.vaccinations.RabbitVaccinationEnum.RVH;
import static kr.co.kumoh.illdang100.mollyspring.dto.vaccination.VaccinationReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.vaccination.VaccinationRespDto.*;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final PetRepository petRepository;

    /**
     * 예방접종 이력 추가
     *
     * @param vaccinationSaveRequest
     * @return
     */
    @Transactional
    public VaccinationSaveResponse saveVaccination(VaccinationSaveRequest vaccinationSaveRequest) {
        Long petId = vaccinationSaveRequest.getPetId();
        Pet findPet = findPetOrElseThrow(petId);

        String vaccinationName = vaccinationSaveRequest.getVaccinationName();
        LocalDate vaccinationDate = vaccinationSaveRequest.getVaccinationDate();
        List<VaccinationHistory> preVaccineList = vaccinationRepository.findByPet_IdOrderByVaccinationDateAsc(petId);

        if (!preVaccineList.isEmpty()) {
            for (VaccinationHistory vaccine : preVaccineList) {
                if (vaccine.getVaccinationName().equals(vaccinationName))
                    throw new CustomApiException("해당 예방접종 이력이 이미 존재합니다.");
                if (vaccinationDate.isBefore(vaccine.getVaccinationDate())) throw new CustomApiException("이전에 등록된 예방접종 날짜 이후에 다음 접종을 등록할 수 있습니다.");
            }
        }

        VaccinationHistory vaccination = VaccinationHistory.builder()
                .pet(findPet)
                .vaccinationName(vaccinationName)
                .vaccinationDate(vaccinationDate)
                .build();

        vaccinationRepository.save(vaccination);

        List<VaccineInfoResponse> predictList = new ArrayList<>();
        PetTypeEnum petType = findPet.getPetType();
        if (petType.equals(DOG)) {
            predictList.addAll(predictDogAfterSave(findPet.getBirthdate(), vaccination));
        } else if (petType.equals(CAT)) {
            predictList.addAll(predictCatAfterSave(findPet.getBirthdate(), vaccination));
        } else if (petType.equals(RABBIT)) {
            predictList.addAll(predictRabbitAfterSave(findPet.getBirthdate(), vaccination));
        }

        return new VaccinationSaveResponse(vaccination.getId(), predictList);
    }

    /**
     * 예방접종 이력 조회
     *
     * @param petId
     * @return
     */
    public List<VaccinationResponse> viewVaccinationList(Long petId) {

        List<VaccinationHistory> vHistory = vaccinationRepository.findByPet_IdOrderByVaccinationDateAsc(petId);
        if (vHistory.isEmpty()) return new ArrayList<>();
        return vHistory.stream()
                .map(v -> new VaccinationResponse(v.getId(), v.getVaccinationName(), v.getVaccinationDate()))
                .collect(Collectors.toList());
    }

    /**
     * 예방접종 이력 수정
     *
     * @param request
     */
    @Transactional
    public VaccinationUpdateResponse updateVaccination(Long petId, VaccinationUpdateRequest request) {
        Pet findPet = findPetOrElseThrow(petId);

        Long vaccinationId = request.getVaccinationId();
        VaccinationHistory vaccination = findVaccinationOrElseThrow(vaccinationId);

        vaccination.updateVaccination(request);

        List<VaccineInfoResponse> predictList = new ArrayList<>();

        PetTypeEnum petType = findPet.getPetType();
        if (petType.equals(DOG)) {
            predictList.addAll(predictDog(findPet.getBirthdate(), List.of(new VaccinationRequest(vaccination.getVaccinationName(), vaccination.getVaccinationDate()))));
        } else if (petType.equals(CAT)) {
            predictList.addAll(predictCat(findPet.getBirthdate(), List.of(new VaccinationRequest(vaccination.getVaccinationName(), vaccination.getVaccinationDate()))));
        } else if (petType.equals(RABBIT)) {
            predictList.addAll(predictRabbit(findPet.getBirthdate(), List.of(new VaccinationRequest(vaccination.getVaccinationName(), vaccination.getVaccinationDate()))));
        }

        return new VaccinationUpdateResponse(predictList);
    }


    /**
     * 예방접종 이력 삭제
     *
     * @param request
     */
    @Transactional
    public VaccinationUpdateResponse deleteVaccination(VaccinationDeleteRequest request) {
        Long petId = request.getPetId();
        Pet findPet = findPetOrElseThrow(petId);

        Long vaccinationId = request.getVaccinationId();
        VaccinationHistory vaccination = findVaccinationOrElseThrow(vaccinationId);

        vaccinationRepository.deleteById(vaccinationId);

        PetTypeEnum petType = findPet.getPetType();
        LocalDate birthdate = findPet.getBirthdate();

        List<VaccineInfoResponse> resultList = new ArrayList<>();
        if (petType.equals(DOG)) {
            resultList.addAll(predictDogAfterDelete(petId, birthdate, vaccination));
        } else if (petType.equals(CAT)) {
            resultList.addAll(predictCatAfterDelete(petId, birthdate, vaccination));
        } else if (petType.equals(RABBIT)) {
            resultList.addAll(predictRabbitAfterDelete(petId, birthdate, vaccination));
        }

        return new VaccinationUpdateResponse(resultList);

    }

    private VaccinationHistory findVaccinationOrElseThrow(Long vaccinationId) {
        return vaccinationRepository.findById(vaccinationId)
                .orElseThrow(() -> new CustomApiException("예방접종 이력이 존재하지 않습니다."));
    }

    public Pet findPetOrElseThrow(Long petId) {

        return petRepository.findById(petId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 반려동물입니다."));
    }

    /**
     * 강아지 예방접종 날짜 예측 (예방정종 기록 없는 경우)
     */
    public List<VaccineInfoResponse> predictDog_anyRecord(LocalDate birthdate) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();
        resultList.addAll(predictDogDHPPL_noRecord(birthdate));
        resultList.addAll(predictDogCORONA_noRecord(birthdate));
        resultList.addAll(predictDogKENNEL_noRecord(birthdate));
        resultList.addAll(predictDogFLU_noRecord(birthdate));
        resultList.add(predictDogRABIES_noRecord(birthdate));
        resultList.add(predictDogANTIBODY_noRecord(birthdate));

        return resultList;
    }

    public List<VaccineInfoResponse> predictDogDHPPL_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(6);
        LocalDate now = LocalDate.now();

        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(2);

        VaccineInfoResponse firstVaccine = new VaccineInfoResponse(DHPPL.getValue() + " 1차", vaccineBaseDate);
        VaccineInfoResponse secondVaccine = new VaccineInfoResponse(DHPPL.getValue() + " 2차", vaccineNextDate);
        VaccineInfoResponse thirdVaccine = new VaccineInfoResponse(DHPPL.getValue() + " 3차", vaccineNextDate.plusWeeks(2));
        VaccineInfoResponse forthVaccine = new VaccineInfoResponse(DHPPL.getValue() + " 4차", vaccineNextDate.plusWeeks(4));
        VaccineInfoResponse fifthVaccine = new VaccineInfoResponse(DHPPL.getValue() + " 5차", vaccineNextDate.plusWeeks(6));

        return List.of(firstVaccine, secondVaccine, thirdVaccine, forthVaccine, fifthVaccine);
    }

    public List<VaccineInfoResponse> predictDogCORONA_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(6);

        LocalDate now = LocalDate.now();
        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(2);

        VaccineInfoResponse firstVaccine = new VaccineInfoResponse(CORONAVIRUS.getValue() + " 1차", vaccineBaseDate);
        VaccineInfoResponse secondVaccine = new VaccineInfoResponse(CORONAVIRUS.getValue() + " 2차", vaccineNextDate);

        return List.of(firstVaccine, secondVaccine);
    }

    public List<VaccineInfoResponse> predictDogKENNEL_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(10);
        LocalDate now = LocalDate.now();

        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(6);

        VaccineInfoResponse firstVaccine = new VaccineInfoResponse(KENNEL_COUGH.getValue() + " 1차", vaccineBaseDate);
        VaccineInfoResponse secondVaccine = new VaccineInfoResponse(KENNEL_COUGH.getValue() + " 2차", vaccineNextDate);


        return List.of(firstVaccine, secondVaccine);
    }

    public List<VaccineInfoResponse> predictDogFLU_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(14);
        LocalDate now = LocalDate.now();

        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(10);

        VaccineInfoResponse firstVaccine = new VaccineInfoResponse(INFLUENZA.getValue() + " 1차", vaccineBaseDate);
        VaccineInfoResponse secondVaccine = new VaccineInfoResponse(INFLUENZA.getValue() + " 2차", vaccineNextDate);

        return List.of(firstVaccine, secondVaccine);
    }

    public VaccineInfoResponse predictDogRABIES_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(16);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(16);

        return new VaccineInfoResponse(DOG_RABIES.getValue(), vaccineBaseDate);
    }

    public VaccineInfoResponse predictDogANTIBODY_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(16);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(16);

        return new VaccineInfoResponse(D0G_ANTIBODY_TITER_TEST.getValue(), vaccineBaseDate);
    }

    /**
     * 고양이 예방접종 날짜 예측 (예방정종 기록 없는 경우)
     */
    public List<VaccineInfoResponse> predictCat_anyRecord(LocalDate birthdate) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        resultList.addAll(predictCatCVRP_noRecord(birthdate));
        resultList.add(predictCatANTIBODY_noRecord(birthdate));
        resultList.add(predictCatFIP_noRecord(birthdate));
        resultList.add(predictCatRABIES_noRecord(birthdate));

        return resultList;
    }

    public List<VaccineInfoResponse> predictCatCVRP_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(6);
        LocalDate now = LocalDate.now();
//        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(6);

        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(2);

        VaccineInfoResponse firstVaccine = new VaccineInfoResponse(CVRP.getValue() + " 1차", vaccineBaseDate);
        VaccineInfoResponse secondVaccine = new VaccineInfoResponse(CVRP.getValue() + " 2차", vaccineNextDate);
        VaccineInfoResponse thirdVaccine = new VaccineInfoResponse(CVRP.getValue() + " 3차", vaccineNextDate.plusWeeks(2));

        return List.of(firstVaccine, secondVaccine, thirdVaccine);
    }

    private static VaccineInfoResponse predictCatRABIES_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(10);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(10);

        return new VaccineInfoResponse(CAT_RABIES.getValue(), vaccineBaseDate);
    }

    private static VaccineInfoResponse predictCatFIP_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(12);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(12);

        return new VaccineInfoResponse(FIP.getValue(), vaccineBaseDate);
    }

    private static VaccineInfoResponse predictCatANTIBODY_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusWeeks(14);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(14);

        return new VaccineInfoResponse(CAT_ANTIBODY_TITER_TEST.getValue(), vaccineBaseDate);
    }

    /**
     * 토끼 예방접종 날짜 예측 (예방접종 기록 없는 경우)
     */
    public List<VaccineInfoResponse> predictRabbit_anyRecord(LocalDate birthdate) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        resultList.addAll(predictRabbitRVH_noRecord(birthdate));
        resultList.add(predictRabbitRABIES_noRecord(birthdate));

        return resultList;
    }

    public List<VaccineInfoResponse> predictRabbitRVH_noRecord(LocalDate birthdate) {
        LocalDate now = LocalDate.now();
        LocalDate vaccineBaseDate = birthdate.plusWeeks(3);

        LocalDate vaccineNextDate = vaccineBaseDate.plusMonths(1);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusMonths(1);

        VaccineInfoResponse firstVaccine1 = new VaccineInfoResponse(RVH.getValue() + " 1차", vaccineBaseDate);
        VaccineInfoResponse secondVaccine = new VaccineInfoResponse(RVH.getValue() + " 2차", vaccineNextDate);

        return List.of(firstVaccine1, secondVaccine);
    }

    private static VaccineInfoResponse predictRabbitRABIES_noRecord(LocalDate birthdate) {
        LocalDate vaccineBaseDate = birthdate.plusMonths(3);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(3);

        return new VaccineInfoResponse(RABBIT_RABIES.getValue(), vaccineBaseDate);
    }

    /**
     * 토끼 예방접종 날짜 예측
     * (이전 접종 기록이 존재할 때)
     */
    public VaccineInfoResponse predictRabbitRVH_existRecord(LocalDate vaccinationDate) {
        LocalDate vaccineBaseDate = vaccinationDate.plusMonths(1);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(1);

        return new VaccineInfoResponse(RVH.getValue() + " 2차", vaccineBaseDate);
    }

    /**
     * 고양이 예방접종 날짜 예측
     * (이전 접종 기록이 존재할 때)
     */
    private List<VaccineInfoResponse> predictCatCVRP_existRecord(LocalDate vaccinationDate, int start) {
        List<VaccineInfoResponse> responseList = new ArrayList<>();
        LocalDate vaccineBaseDate = vaccinationDate.plusWeeks(2);
        responseList.add(new VaccineInfoResponse(CVRP.getValue() + " " + start + "차", vaccineBaseDate));

        LocalDate now = LocalDate.now();
        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(2);

        for (int i = start + 1; i <= 3; i++) {
            responseList.add(new VaccineInfoResponse(CVRP.getValue() + " " + i + "차", vaccineNextDate));
            vaccineNextDate = vaccineNextDate.plusWeeks(2);
        }
        return responseList;
    }

    /**
     * 강아지 예방접종 날짜 예측
     * (이전 접종 기록이 존재할 때)
     */
    private List<VaccineInfoResponse> predictDogDHPPL_existRecord(LocalDate vaccinationDate, int start) {
        List<VaccineInfoResponse> responseList = new ArrayList<>();

        LocalDate vaccineBaseDate = vaccinationDate.plusWeeks(2);
        responseList.add(new VaccineInfoResponse(DHPPL.getValue() + " " + start + "차", vaccineBaseDate));

        LocalDate now = LocalDate.now();
        LocalDate vaccineNextDate = vaccineBaseDate.plusWeeks(2);
        if (vaccineBaseDate.isBefore(now)) vaccineNextDate = now.plusWeeks(2);

        for (int i = start + 1; i <= 5; i++) {
            responseList.add(new VaccineInfoResponse(DHPPL.getValue() + " " + i + "차", vaccineNextDate));
            vaccineNextDate = vaccineNextDate.plusWeeks(2);
        }
        return responseList;
    }

    private VaccineInfoResponse predictDogCORONA_existRecord(LocalDate vaccinationDate) {
        LocalDate vaccineBaseDate = vaccinationDate.plusWeeks(2);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(2);

        return new VaccineInfoResponse(CORONAVIRUS.getValue() + " 2차", vaccineBaseDate);
    }

    private VaccineInfoResponse predictDogKENNEL_existRecord(LocalDate vaccinationDate) {
        LocalDate vaccineBaseDate = vaccinationDate.plusWeeks(2);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(2);

        return new VaccineInfoResponse(KENNEL_COUGH.getValue() + " 2차", vaccineBaseDate);
    }

    private VaccineInfoResponse predictDogFLU_existRecord(LocalDate vaccinationDate) {
        LocalDate vaccineBaseDate = vaccinationDate.plusWeeks(2);
        LocalDate now = LocalDate.now();
        if (vaccineBaseDate.isBefore(now)) vaccineBaseDate = now.plusWeeks(2);

        return new VaccineInfoResponse(INFLUENZA.getValue() + " 2차", vaccineBaseDate);
    }

    public List<VaccineInfoResponse> predictDog(LocalDate birthdate, List<VaccinationRequest> vaccination) {

        List<VaccineInfoResponse> resultList = new ArrayList<>();

        List<VaccinationRequest> dhpplList = new ArrayList<>();
        boolean dhppl_check = false, corona_check = false, kennel_check = false, flu_check = false, rabies_check = false, antibody_check = false;
        for (VaccinationRequest request : vaccination) {
            String vaccinationName = request.getVaccinationName();
            LocalDate vaccinationDate = request.getVaccinationDate();

            if (vaccinationName.contains(DHPPL.getValue())) {
                dhpplList.add(request);
                dhppl_check = true;
            } else if (vaccinationName.contains(CORONAVIRUS.getValue())) {
                resultList.add(predictDogCORONA_existRecord(vaccinationDate));
                corona_check = true;
            } else if (vaccinationName.contains(KENNEL_COUGH.getValue())) {
                resultList.add(predictDogKENNEL_existRecord(vaccinationDate));
                kennel_check = true;
            } else if (vaccinationName.contains(INFLUENZA.getValue())) {
                resultList.add(predictDogFLU_existRecord(vaccinationDate));
                flu_check = true;
            } else if (vaccinationName == DOG_RABIES.getValue()) rabies_check = true;
            else if (vaccinationName == D0G_ANTIBODY_TITER_TEST.getValue()) antibody_check = true;
        }

        if (!dhppl_check) {
            resultList.addAll(predictDogDHPPL_noRecord(birthdate));
        } else {
            resultList.addAll(calculateNextDHPPLDate(dhpplList));
        }
        if (!corona_check) resultList.addAll(predictDogCORONA_noRecord(birthdate));
        if (!kennel_check) resultList.addAll(predictDogKENNEL_noRecord(birthdate));
        if (!flu_check) resultList.addAll(predictDogFLU_noRecord(birthdate));
        if (!rabies_check) resultList.add(predictDogRABIES_noRecord(birthdate));
        if (!antibody_check) resultList.add(predictDogANTIBODY_noRecord(birthdate));

        return resultList;
    }

    private List<VaccineInfoResponse> calculateNextDHPPLDate(List<VaccinationRequest> dhpplList) {
        dhpplList.sort(new Comparator<VaccinationRequest>() {
            @Override
            public int compare(VaccinationRequest o1, VaccinationRequest o2) {
                if (o1.getVaccinationDate().isBefore(o2.getVaccinationDate())) return -1;
                else if (o1.getVaccinationDate().isEqual(o2.getVaccinationDate())) return 0;
                else return 1;
            }
        });

        VaccinationRequest lastVaccine = dhpplList.get(dhpplList.size() - 1);
        LocalDate lastVaccineDate = lastVaccine.getVaccinationDate();
        String lastVaccineName = lastVaccine.getVaccinationName();
        int n_idx = lastVaccineName.indexOf(" ");
        int vaccine_Nth = lastVaccineName.charAt(n_idx + 1) - '0';

        List<VaccineInfoResponse> responseList = predictDogDHPPL_existRecord(lastVaccineDate, vaccine_Nth + 1);
        LocalDate now = LocalDate.now();

        LocalDate vaccineDate = responseList.get(0).getVaccinationDate();
        int between = 0;
        if (vaccineDate.isBefore(now)) {
            Period betweenPeriod = Period.between(vaccineDate, now);
            between = betweenPeriod.getDays();
        }

        for (int i = 1; i < responseList.size(); i++) {
            VaccineInfoResponse vaccine = responseList.get(i);
            vaccine.setVaccinationDate(vaccine.getVaccinationDate().plusDays(between));
        }

        return responseList;
    }

    public List<VaccineInfoResponse> predictCat(LocalDate birthdate, List<VaccinationRequest> vaccination) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        List<VaccinationRequest> cvrpList = new ArrayList<>();
        boolean cvrp_check = false, rabies_check = false, antibody_check = false, fip_check = false;
        for (VaccinationRequest request : vaccination) {
            String vaccinationName = request.getVaccinationName();
            if (vaccinationName.contains(CVRP.getValue())) {
                cvrpList.add(request);
                cvrp_check = true;
            }
            if (vaccinationName == CAT_RABIES.getValue()) {
                rabies_check = true;
            }
            if (vaccinationName == FIP.getValue()) {
                fip_check = true;
            }
            if (vaccinationName == CAT_ANTIBODY_TITER_TEST.getValue()) {
                antibody_check = true;
            }
        }

        if (!cvrp_check) resultList.addAll(predictCatCVRP_noRecord(birthdate));
        else {
            resultList.addAll(calculateNextCVRPDate(cvrpList));
        }
        if (!rabies_check) resultList.add(predictCatRABIES_noRecord(birthdate));
        if (!fip_check) resultList.add(predictCatFIP_noRecord(birthdate));
        if (!antibody_check) resultList.add(predictCatANTIBODY_noRecord(birthdate));

        return resultList;
    }

    private List<VaccineInfoResponse> calculateNextCVRPDate(List<VaccinationRequest> cvrpList) {
        cvrpList.sort(new Comparator<VaccinationRequest>() {
            @Override
            public int compare(VaccinationRequest o1, VaccinationRequest o2) {
                if (o1.getVaccinationDate().isBefore(o2.getVaccinationDate())) return -1;
                else if (o1.getVaccinationDate().isEqual(o2.getVaccinationDate())) return 0;
                else return 1;
            }
        });

        VaccinationRequest lastVaccine = cvrpList.get(cvrpList.size() - 1);
        LocalDate lastVaccineDate = lastVaccine.getVaccinationDate();
        String lastVaccineName = lastVaccine.getVaccinationName();
        int n_idx = lastVaccineName.indexOf(" ");
        int vaccine_Nth = lastVaccineName.charAt(n_idx + 1) - '0';

        List<VaccineInfoResponse> responseList = predictCatCVRP_existRecord(lastVaccineDate, vaccine_Nth + 1);
        LocalDate now = LocalDate.now();

        LocalDate vaccineDate = responseList.get(0).getVaccinationDate();
        int between = 0;
        if (vaccineDate.isBefore(now)) {
            Period betweenPeriod = Period.between(vaccineDate, now);
            between = betweenPeriod.getDays();
        }

        for (int i = 1; i < responseList.size(); i++) {
            VaccineInfoResponse vaccine = responseList.get(i);
            vaccine.setVaccinationDate(vaccine.getVaccinationDate().plusDays(between));
        }
        return responseList;
    }

    public List<VaccineInfoResponse> predictRabbit(LocalDate birthdate, List<VaccinationRequest> vaccination) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        boolean rvh_check = false, rabies_check = false;
        for (VaccinationRequest request : vaccination) {
            String vaccinationName = request.getVaccinationName();
            LocalDate vaccinationDate = request.getVaccinationDate();

            if (vaccinationName.contains(RVH.getValue())) {
                resultList.add(predictRabbitRVH_existRecord(vaccinationDate));
                rvh_check = true;
            }

            if (vaccinationName.contains(RVH.getValue())) rabies_check = true;
        }

        if (!rvh_check) resultList.addAll(predictRabbitRVH_noRecord(birthdate));
        if (!rabies_check) resultList.add(predictRabbitRABIES_noRecord(birthdate));

        return resultList;
    }

    public List<VaccineInfoResponse> predictDogAfterSave( LocalDate birthdate, VaccinationHistory savedVaccine) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        String savedVaccineName = savedVaccine.getVaccinationName();
        LocalDate savedVaccineDate = savedVaccine.getVaccinationDate();

        if (savedVaccineName.contains(DHPPL.getValue())) {
            int n_idx = savedVaccineName.indexOf(" ");
            int vaccine_Nth = savedVaccineName.charAt(n_idx + 1) - '0';

            resultList.addAll(predictDogDHPPL_existRecord(savedVaccineDate, vaccine_Nth + 1));
        }

        if (savedVaccineName.contains(CORONAVIRUS.getValue())) {
            resultList.add(predictDogCORONA_existRecord(savedVaccineDate));
        }

        if (savedVaccineName.contains(KENNEL_COUGH.getValue())) {
            resultList.add(predictDogKENNEL_existRecord(savedVaccineDate));
        }

        if (savedVaccineName.contains(INFLUENZA.getValue())) {
            resultList.add(predictDogFLU_existRecord(savedVaccineDate));
        }

        if (savedVaccineName.contains(DOG_RABIES.getValue())) {
            resultList.add(predictDogRABIES_noRecord(birthdate));
        }

        if (savedVaccineName.contains(D0G_ANTIBODY_TITER_TEST.getValue())) {
            resultList.add(predictDogANTIBODY_noRecord(birthdate));
        }

        return resultList;
    }

    public List<VaccineInfoResponse> predictDogAfterDelete(Long petId, LocalDate birthdate, VaccinationHistory deletedVaccine) {

        List<VaccineInfoResponse> resultList = new ArrayList<>();
        String deletedVaccineName = deletedVaccine.getVaccinationName();

        if (deletedVaccineName.contains(DHPPL.getValue())) {
            int n_idx = deletedVaccineName.indexOf(" ");
            int vaccine_Nth = deletedVaccineName.charAt(n_idx + 1) - '0';

            Optional<VaccinationHistory> preVaccineOpt = findByVaccineNameOrElseThrow(petId, vaccine_Nth-1, DHPPL.getValue());

            if (!preVaccineOpt.isPresent()) {
                resultList.addAll(predictDogDHPPL_noRecord(birthdate));
            } else {
                VaccinationHistory preVaccine = preVaccineOpt.get();
                LocalDate preVaccineDate = preVaccine.getVaccinationDate();
                resultList.addAll(predictDogDHPPL_existRecord(preVaccineDate, vaccine_Nth));
            }
        }

        if (deletedVaccineName.contains(CORONAVIRUS.getValue())) {
            int n_idx = deletedVaccineName.indexOf(" ");
            int vaccine_Nth = deletedVaccineName.charAt(n_idx + 1) - '0';

            Optional<VaccinationHistory> preVaccineOpt = findByVaccineNameOrElseThrow(petId, vaccine_Nth-1, CORONAVIRUS.getValue());
            if (vaccine_Nth == 1 && !preVaccineOpt.isPresent()) {
                resultList.addAll(predictDogCORONA_noRecord(birthdate));
            } else {
                VaccinationHistory preVaccine = preVaccineOpt.get();
                LocalDate preVaccineDate = preVaccine.getVaccinationDate();
                resultList.add(predictDogCORONA_existRecord(preVaccineDate));
            }
        }

        if (deletedVaccineName.contains(KENNEL_COUGH.getValue())) {
            int n_idx = deletedVaccineName.indexOf(" ");
            int vaccine_Nth = deletedVaccineName.charAt(n_idx + 1) - '0';

            Optional<VaccinationHistory> preVaccineOpt = findByVaccineNameOrElseThrow(petId, vaccine_Nth-1, KENNEL_COUGH.getValue());
            if (vaccine_Nth == 1 && !preVaccineOpt.isPresent()) {
                resultList.addAll(predictDogKENNEL_noRecord(birthdate));
            } else {
                VaccinationHistory preVaccine = preVaccineOpt.get();
                LocalDate preVaccineDate = preVaccine.getVaccinationDate();
                resultList.add(predictDogKENNEL_existRecord(preVaccineDate));
            }
        }

        if (deletedVaccineName.contains(INFLUENZA.getValue())) {
            int n_idx = deletedVaccineName.indexOf(" ");
            int vaccine_Nth = deletedVaccineName.charAt(n_idx + 1) - '0';

            Optional<VaccinationHistory> preVaccineOpt = findByVaccineNameOrElseThrow(petId, vaccine_Nth-1, INFLUENZA.getValue());
            if (vaccine_Nth == 1 && !preVaccineOpt.isPresent()) {
                resultList.addAll(predictDogFLU_noRecord(birthdate));
            } else {
                VaccinationHistory preVaccine = preVaccineOpt.get();
                LocalDate preVaccineDate = preVaccine.getVaccinationDate();
                resultList.add(predictDogFLU_existRecord(preVaccineDate));
            }
        }

        if (deletedVaccineName.contains(DOG_RABIES.getValue())) {
            resultList.add(predictDogRABIES_noRecord(birthdate));
        }

        if (deletedVaccineName.contains(D0G_ANTIBODY_TITER_TEST.getValue())) {
            resultList.add(predictDogANTIBODY_noRecord(birthdate));
        }
        return resultList;
    }

    public List<VaccineInfoResponse> predictCatAfterSave(LocalDate birthdate, VaccinationHistory savedVaccine) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        String savedVaccineName = savedVaccine.getVaccinationName();
        LocalDate savedVaccineDate = savedVaccine.getVaccinationDate();
        if (savedVaccineName.contains(CVRP.getValue())) {
            int n_idx = savedVaccineName.indexOf(" ");
            int vaccine_Nth = savedVaccineName.charAt(n_idx + 1) - '0';

            resultList.addAll(predictCatCVRP_existRecord(savedVaccineDate, vaccine_Nth+1));
        }

        if (savedVaccineName.contains(CAT_RABIES.getValue())) {
            resultList.add(predictCatRABIES_noRecord(birthdate));
        }

        if (savedVaccineName.contains(FIP.getValue())) {
            resultList.add(predictCatFIP_noRecord(birthdate));
        }

        if (savedVaccineName.contains(CAT_ANTIBODY_TITER_TEST.getValue())) {
            resultList.add(predictCatANTIBODY_noRecord(birthdate));
        }

        return resultList;
    }

    public List<VaccineInfoResponse> predictCatAfterDelete(Long petId, LocalDate birthdate, VaccinationHistory deletedVaccine) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        String deletedVaccineName = deletedVaccine.getVaccinationName();
        if (deletedVaccineName.contains(CVRP.getValue())) {
            int n_idx = deletedVaccineName.indexOf(" ");
            int vaccine_Nth = deletedVaccineName.charAt(n_idx + 1) - '0';

            Optional<VaccinationHistory> preVaccineOpt = findByVaccineNameOrElseThrow(petId, vaccine_Nth - 1, KENNEL_COUGH.getValue());
            if (!preVaccineOpt.isPresent()) {
                resultList.addAll(predictCatCVRP_noRecord(birthdate));
            } else {
                VaccinationHistory preVaccine = preVaccineOpt.get();
                LocalDate preVaccineDate = preVaccine.getVaccinationDate();
                resultList.addAll(predictCatCVRP_existRecord(preVaccineDate, vaccine_Nth));
            }
        }

        if (deletedVaccineName.contains(CAT_RABIES.getValue())) {
            resultList.add(predictCatRABIES_noRecord(birthdate));
        }

        if (deletedVaccineName.contains(FIP.getValue())) {
            resultList.add(predictCatFIP_noRecord(birthdate));
        }

        if (deletedVaccineName.contains(CAT_ANTIBODY_TITER_TEST.getValue())) {
            resultList.add(predictCatANTIBODY_noRecord(birthdate));
        }

        return resultList;
    }

    public List<VaccineInfoResponse> predictRabbitAfterSave (LocalDate birthdate, VaccinationHistory savedVaccine) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        String savedVaccineName = savedVaccine.getVaccinationName();
        LocalDate savedVaccineDate = savedVaccine.getVaccinationDate();
        if (savedVaccineName.contains(RVH.getValue())) {
                resultList.add(predictRabbitRVH_existRecord(savedVaccineDate));
            }

        if (savedVaccineName.contains(RABBIT_RABIES.getValue())) {
            resultList.add(predictRabbitRABIES_noRecord(birthdate));
        }

        return resultList;
    }

    public List<VaccineInfoResponse> predictRabbitAfterDelete(Long petId, LocalDate birthdate, VaccinationHistory deleteVaccination) {
        List<VaccineInfoResponse> resultList = new ArrayList<>();

        String deletedVaccineName = deleteVaccination.getVaccinationName();
        if (deletedVaccineName.contains(RVH.getValue())) {
            int n_idx = deletedVaccineName.indexOf(" ");
            int vaccine_Nth = deletedVaccineName.charAt(n_idx + 1) - '0';

            Optional<VaccinationHistory> preVaccineOpt = findByVaccineNameOrElseThrow(petId, vaccine_Nth - 1, RVH.getValue());
            if (!preVaccineOpt.isPresent()) {
                resultList.addAll(predictRabbitRVH_noRecord(birthdate));
            } else {
                VaccinationHistory preVaccine = preVaccineOpt.get();
                LocalDate preVaccineDate = preVaccine.getVaccinationDate();
                resultList.add(predictRabbitRVH_existRecord(preVaccineDate));
            }
        }

        if (deletedVaccineName.contains(RABBIT_RABIES.getValue())) {
            resultList.add(predictRabbitRABIES_noRecord(birthdate));
        }

        return resultList;
    }

    private Optional<VaccinationHistory> findByVaccineNameOrElseThrow(Long petId, int vaccine_Nth, String vaccineName) {
        Optional<VaccinationHistory> savedVaccine = vaccinationRepository.findByVaccinationNameAndPet_Id( vaccineName + " " + String.valueOf(vaccine_Nth), petId);
        return savedVaccine;
    }
}
