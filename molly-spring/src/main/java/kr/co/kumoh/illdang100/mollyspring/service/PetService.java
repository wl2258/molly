package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.*;
import kr.co.kumoh.illdang100.mollyspring.dto.pet.PetRespDto.PetCalendarResponse;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.cat.CatRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.dog.DogRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.pet.PetRepository;
import kr.co.kumoh.illdang100.mollyspring.repository.rabbit.RabbitRepository;
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
     * @param petSaveRequest
     * @return
     * @throws IOException
     */
    @Transactional
    public PetDetailResponse registerPet(PetSaveRequest petSaveRequest) throws IOException {

        Account findUser = accountRepository.findById(petSaveRequest.getUserId())
                .orElseThrow(() -> new CustomApiException("존재하지 않는 사용자입니다."));

        PetTypeEnum petType = petSaveRequest.getPetType();

        Optional<Pet> findPetOpt = petRepository.findByAccountIdAndPetTypeAndPetNameAndBirthdate(findUser.getId(), petType, petSaveRequest.getPetName(), petSaveRequest.getBirthdate());
        if (findPetOpt.isPresent()) throw new CustomApiException("이미 등록된 반려동물입니다.");

        MultipartFile imageFile = petSaveRequest.getImageFile();
        if (petType.equals(CAT)) {
            Pet savedCat = saveCat(petSaveRequest, petType, findUser, imageFile);
            return viewDetails(savedCat.getId());
        }
        else if (petType.equals(DOG)) {
            Pet savedDog = saveDog(petSaveRequest, petType, findUser, imageFile);
            return viewDetails(savedDog.getId());
        }
        else if (petType.equals(RABBIT)) {
            Pet savedRabbit = saveRabbit(petSaveRequest, petType, findUser, imageFile);
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

        PetDetailResponse petDetailResponse = PetDetailResponse.builder()
                .userId(findPet.getAccount().getId())
                .petId(petId)
                .petType(petType)
                .petName(findPet.getPetName())
                .birthdate(findPet.getBirthdate())
                .gender(findPet.getGender())
                .neuteredStatus(findPet.isNeuteredStatus())
                .weight(findPet.getWeight())
                .species(petSpecies)
                .build();

        if (findPet.getCaution() != null) petDetailResponse.setCaution(findPet.getCaution());
        if (findPet.getPetProfileImage() != null) petDetailResponse.setProfileImage(findPet.getPetProfileImage().getStoreFileName());

        return petDetailResponse;
    }

    /**
     * 반려동물 기본 정보 수정
     * @param petUpdateRequest
     * @throws IOException
     */
    @Transactional
    public Long updatePet(PetUpdateRequest petUpdateRequest) {

        Long petId = petUpdateRequest.getPetId();
        Pet findPet = checkPresentPet(petId);

        findPet.updatePet(petUpdateRequest);
        updatePetSpecies(petUpdateRequest.getSpecies(), findPet);

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
        PetDetailResponse petDetailResponse = viewDetails(petId);
        return PetCalendarResponse.builder()
                .petType(petDetailResponse.getPetType())
                .petName(petDetailResponse.getPetName())
                .birthdate(petDetailResponse.getBirthdate())
                .medication(petDetailResponse.getMedication())
                .surgery(petDetailResponse.getSurgery())
                .vaccination(petDetailResponse.getVaccination())
                .build();
    }

    /**
     * 반려동물 프로필 이미지 변경
     * @param petId
     * @param multipartFile
     * @throws IOException
     */
    @Transactional
    public void updatePetProfile(Long petId, MultipartFile multipartFile) throws IOException {

        Pet findPet = deletePetProfile(petId);
        ImageFile updatedImageFile = s3Service.upload(multipartFile, FileRootPathVO.PET_PATH);
        findPet.updatePetProfileImage(updatedImageFile);
    }

    /**
     * 반려동물 프로필 이미지 삭제
     * @param petId
     * @return
     */
    @Transactional
    public Pet deletePetProfile(Long petId) {

        Pet findPet = findPetOrElseThrow(petId);

        ImageFile petProfileImage = findPet.getPetProfileImage();
        if (petProfileImage != null) {
            s3Service.delete(petProfileImage.getStoreFileName());
            findPet.updatePetProfileImage(null);
        }
        return findPet;
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

    private Pet saveCat(PetSaveRequest petSaveRequest, PetTypeEnum petType, Account findUser, MultipartFile multipartFile) throws IOException {

        ImageFile petProfileImage = null;

        if (multipartFile != null)
            petProfileImage = s3Service.upload(multipartFile, "pet");

        Cat createdCat = createCat(petSaveRequest, petType, findUser, petProfileImage);
        catRepository.save(createdCat);

        return createdCat;
    }

    private Pet saveDog(PetSaveRequest petSaveRequest, PetTypeEnum petType, Account findUser, MultipartFile multipartFile) throws IOException {

        ImageFile petProfileImage = null;
        if (multipartFile != null)
            petProfileImage = s3Service.upload(multipartFile, "pet");

        Dog createdDog = createDog(petSaveRequest, petType, findUser, petProfileImage);
        dogRepository.save(createdDog);

        return createdDog;
    }

    private Pet saveRabbit(PetSaveRequest petSaveRequest, PetTypeEnum petType, Account findUser, MultipartFile multipartFile) throws IOException {

        ImageFile petProfileImage = null;
        if (multipartFile != null)
            petProfileImage = s3Service.upload(multipartFile, "pet");

        Rabbit createdRabbit = createRabbit(petSaveRequest, petType, findUser, petProfileImage);
        rabbitRepository.save(createdRabbit);

        return createdRabbit;
    }

    private Cat createCat(PetSaveRequest petSaveRequest, PetTypeEnum petType, Account findUser, ImageFile petProfileImage) {

        return Cat.builder()
                .account(findUser)
                .petName(petSaveRequest.getPetName())
                .gender(petSaveRequest.getGender())
                .birthdate(petSaveRequest.getBirthdate())
                .weight(petSaveRequest.getWeight())
                .neuteredStatus(petSaveRequest.isNeuteredStatus())
                .petType(petType)
                .catSpecies(CatEnum.valueOf(petSaveRequest.getSpecies()))
                .caution(petSaveRequest.getCaution())
                .petProfileImage(petProfileImage)
                .catSpecies(CatEnum.valueOf(petSaveRequest.getSpecies()))
                .build();
    }

    private Dog createDog(PetSaveRequest petSaveRequest, PetTypeEnum petType, Account findUser, ImageFile petProfileImage) {

        return Dog.builder()
                .account(findUser)
                .petName(petSaveRequest.getPetName())
                .gender(petSaveRequest.getGender())
                .birthdate(petSaveRequest.getBirthdate())
                .weight(petSaveRequest.getWeight())
                .neuteredStatus(petSaveRequest.isNeuteredStatus())
                .petType(petType)
                .dogSpecies(DogEnum.valueOf(petSaveRequest.getSpecies()))
                .caution(petSaveRequest.getCaution())
                .petProfileImage(petProfileImage)
                .dogSpecies(DogEnum.valueOf(petSaveRequest.getSpecies()))
                .build();
    }

    private Rabbit createRabbit(PetSaveRequest petSaveRequest, PetTypeEnum petType, Account findUser, ImageFile petProfileImage) {

        return Rabbit.builder()
                .account(findUser)
                .petName(petSaveRequest.getPetName())
                .gender(petSaveRequest.getGender())
                .birthdate(petSaveRequest.getBirthdate())
                .weight(petSaveRequest.getWeight())
                .neuteredStatus(petSaveRequest.isNeuteredStatus())
                .petType(petType)
                .rabbitSpecies(RabbitEnum.valueOf(petSaveRequest.getSpecies()))
                .caution(petSaveRequest.getCaution())
                .petProfileImage(petProfileImage)
                .rabbitSpecies(RabbitEnum.valueOf(petSaveRequest.getSpecies()))
                .build();
    }
}
