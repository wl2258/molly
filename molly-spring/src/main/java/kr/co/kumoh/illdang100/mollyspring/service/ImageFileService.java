package kr.co.kumoh.illdang100.mollyspring.service;

import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.Pet;
import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomApiException;
import kr.co.kumoh.illdang100.mollyspring.repository.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageFileService {

    private final S3Service s3Service;
    private final PetRepository petRepository;

    /**
     * 반려동물 프로필 이미지 변경
     * @param petId
     * @param multipartFile
     * @throws IOException
     */
    @Transactional
    public void updatePetProfileFile(Long petId, MultipartFile multipartFile) throws IOException {

        Pet findPet = deletePetProfileFile(petId);
        ImageFile updatedImageFile = s3Service.upload(multipartFile, FileRootPathVO.PET_PATH);
        findPet.updatePetProfileImage(updatedImageFile);
    }

    /**
     * 반려동물 프로필 이미지 삭제
     * @param petId
     * @return
     */
    public Pet deletePetProfileFile(Long petId) {

        Pet findPet = petRepository.findById(petId)
                .orElseThrow(() -> new CustomApiException("존재하지 않는 반려동물입니다."));

        ImageFile petProfileImage = findPet.getPetProfileImage();
        if (petProfileImage != null)
            s3Service.delete(petProfileImage.getStoreFileName());

        return findPet;
    }
}
