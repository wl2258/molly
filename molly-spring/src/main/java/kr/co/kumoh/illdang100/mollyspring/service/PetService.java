package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.*;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetRespDto.PetCalendarResponse;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.medication.MedicationRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.cat.CatRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.dog.DogRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.pet.PetRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.rabbit.RabbitRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.surgery.SurgeryRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.vaccination.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.pet.PetReqDto.*;
import static kr.co.kumoh.illdang100.mollyspring.dto.pet.PetRespDto.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetService {
    private final CatRepository catRepository;
    private final DogRepository dogRepository;
    private final RabbitRepository rabbitRepository;
    private final AccountRepository accountRepository;
    private final PetRepository petRepository;
    private final S3Service s3Service;

    /**
     * 반려동물 등록
     * @param request
     * @return
     * @throws IOException
     */
    @Transactional
    public PetDetailResponse registerPet(PetSaveRequest request) throws IOException {

        Optional<Account> findUserOpt = accountRepository.findById(request.getUserId());
        if (!findUserOpt.isPresent()) throw new CustomApiException("존재하지 않는 사용자입니다.");

        Account findUser = findUserOpt.get();
        PetTypeEnum petType = request.getPetType();

        Optional<Pet> findPetOpt = petRepository.findByAccountIdAndPetTypeAndPetNameAndBirthDate(findUser.getId(), petType, request.getPetName(), request.getBirthDate());
        if (findPetOpt.isPresent()) throw new CustomApiException("이미 등록된 반려동물입니다.");

        MultipartFile imageFile = request.getImageFile();
        if (petType.equals(CAT)) {
            Pet savedCat = saveCat(request, petType, findUser, imageFile);
            return viewDetails(savedCat.getId());
        }
        else if (petType.equals(DOG)) {
            Pet savedDog = saveDog(request, petType, findUser, imageFile);
            return viewDetails(savedDog.getId());
        }
        else if (petType.equals(RABBIT)) {
            Pet savedRabbit = saveRabbit(request, petType, findUser, imageFile);
            return viewDetails(savedRabbit.getId());
        }
        throw new CustomApiException("반려동물 등록 실패");
    }

    /**
     * 반려동물 정보 상세보기
     * @param petId
     * @return
     */
    public PetDetailResponse viewDetails(Long petId) {
        Pet findPet = checkPresentPet(petId);
        PetTypeEnum petType = findPet.getPetType();

        String petSpecies = getPetSpecies(findPet);

        PetDetailResponse response = PetDetailResponse.builder()
                .userId(findPet.getAccount().getId())
                .petId(petId)
                .petType(petType)
                .petName(findPet.getPetName())
                .birthDate(findPet.getBirthDate())
                .gender(findPet.getGender())
                .neuteredStatus(findPet.isNeuteredStatus())
                .weight(findPet.getWeight())
                .species(petSpecies)
                .build();

        if (findPet.getCaution() != null) response.setCaution(findPet.getCaution());
        if (findPet.getPetProfileImage() != null) response.setProfileImage(findPet.getPetProfileImage().getStoreFileName());

        return response;
    }

    /**
     * 반려동물 기본 정보 수정
     * @param request
     * @throws IOException
     */
    @Transactional
    public Long updatePet(PetUpdateRequest request) {

        Long petId = request.getPetId();
        Pet findPet = checkPresentPet(petId);

        findPet.updatePet(request);
        updatePetSpecies(request.getSpecies(), findPet);

        return findPet.getId();
    }

    /**
     * 반려동물 정보 삭제
     * @param petId
     */
    @Transactional
    public void deletePet(Long petId) {
    }

    /**
     * 연간 달력 정보
     * @param petId
     * @return
     */
    public PetCalendarResponse viewAnnualCalendarSchedule(Long petId) {
        PetDetailResponse response = viewDetails(petId);
        return PetCalendarResponse.builder()
                .petType(response.getPetType())
                .petName(response.getPetName())
                .birthDate(response.getBirthDate())
                .medication(response.getMedication())
                .surgery(response.getSurgery())
                .vaccination(response.getVaccination())
                .build();
    }

    public Pet findPetOrElseThrow(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 반려동물입니다."));
    }

    private Pet checkPresentPet(Long petId) {
        Pet findPet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 반려동물입니다."));

        return findPet;
    }

    private static String getPetSpecies(Pet findPet) {
        String petSpecies = new String();
        if (isDog(findPet)) {
            Dog dog = (Dog) findPet;
            petSpecies = dog.getDogSpecies().toString();
        }
        else if (isCat(findPet)) {
            Cat cat = (Cat) findPet;
            petSpecies = cat.getCatSpecies().toString();
        }
        else if (isRabbit(findPet)) {
            Rabbit rabbit = (Rabbit) findPet;
            petSpecies = rabbit.getRabbitSpecies().toString();
        }
        return petSpecies;
    }

    private static void updatePetSpecies(String petSpecies, Pet pet) {
        if (isDog(pet)) {
            Dog dog = (Dog) pet;
            DogEnum dogSpecies = DogEnum.valueOf(petSpecies);
            if (!dog.compareDogSpecies(dogSpecies)) dog.updateDogSpecies(dogSpecies);
        }
        else if (isCat(pet)) {
            Cat cat = (Cat) pet;
            CatEnum catSpecies = CatEnum.valueOf(petSpecies);
            if (!cat.compareCatSpecies(catSpecies)) cat.updateCatSpecies(catSpecies);
        }
        else if (isRabbit(pet)) {
            Rabbit rabbit = (Rabbit) pet;
            RabbitEnum rabbitSpecies = RabbitEnum.valueOf(petSpecies);
            if (!rabbit.compareRabbitSpecies(rabbitSpecies)) rabbit.updateRabbitSpecies(rabbitSpecies);
        }
    }

    private static boolean isCat(Pet findPet) {
        return findPet instanceof Cat;
    }

    private static boolean isDog(Pet findPet) {
        return findPet instanceof Dog;
    }

    private static boolean isRabbit(Pet findPet) {
        return findPet instanceof Rabbit;
    }

    private Pet saveCat(PetSaveRequest request, PetTypeEnum petType, Account findUser, MultipartFile multipartFile) throws IOException {

        ImageFile petProfileImage = null;

        if (multipartFile != null)
            petProfileImage = s3Service.upload(multipartFile, "pet");

        Cat createdCat = createCat(request, petType, findUser, petProfileImage);
        catRepository.save(createdCat);

        return createdCat;
    }

    private Pet saveDog(PetSaveRequest request, PetTypeEnum petType, Account findUser, MultipartFile multipartFile) throws IOException {

        ImageFile petProfileImage = null;
        if (multipartFile != null)
            petProfileImage = s3Service.upload(multipartFile, "pet");

        Dog createdDog = createDog(request, petType, findUser, petProfileImage);
        dogRepository.save(createdDog);

        return createdDog;
    }

    private Pet saveRabbit(PetSaveRequest request, PetTypeEnum petType, Account findUser, MultipartFile multipartFile) throws IOException {

        ImageFile petProfileImage = null;
        if (multipartFile != null)
            petProfileImage = s3Service.upload(multipartFile, "pet");

        Rabbit createdRabbit = createRabbit(request, petType, findUser, petProfileImage);
        rabbitRepository.save(createdRabbit);

        return createdRabbit;
    }

    private Cat createCat(PetSaveRequest request, PetTypeEnum petType, Account findUser, ImageFile petProfileImage) {

        return Cat.builder()
                .account(findUser)
                .petName(request.getPetName())
                .gender(request.getGender())
                .birthdate(request.getBirthDate())
                .weight(request.getWeight())
                .neuteredStatus(request.isNeuteredStatus())
                .petType(petType)
                .catSpecies(CatEnum.valueOf(request.getSpecies()))
                .caution(request.getCaution())
                .petProfileImage(petProfileImage)
                .catSpecies(CatEnum.valueOf(request.getSpecies()))
                .build();
    }

    private Dog createDog(PetSaveRequest request, PetTypeEnum petType, Account findUser, ImageFile petProfileImage) {

        return Dog.builder()
                .account(findUser)
                .petName(request.getPetName())
                .gender(request.getGender())
                .birthdate(request.getBirthDate())
                .weight(request.getWeight())
                .neuteredStatus(request.isNeuteredStatus())
                .petType(petType)
                .dogSpecies(DogEnum.valueOf(request.getSpecies()))
                .caution(request.getCaution())
                .petProfileImage(petProfileImage)
                .dogSpecies(DogEnum.valueOf(request.getSpecies()))
                .build();
    }

    private Rabbit createRabbit(PetSaveRequest request, PetTypeEnum petType, Account findUser, ImageFile petProfileImage) {

        return Rabbit.builder()
                .account(findUser)
                .petName(request.getPetName())
                .gender(request.getGender())
                .birthdate(request.getBirthDate())
                .weight(request.getWeight())
                .neuteredStatus(request.isNeuteredStatus())
                .petType(petType)
                .rabbitSpecies(RabbitEnum.valueOf(request.getSpecies()))
                .caution(request.getCaution())
                .petProfileImage(petProfileImage)
                .rabbitSpecies(RabbitEnum.valueOf(request.getSpecies()))
                .build();
    }
}
