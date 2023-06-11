package kr.co.kumoh.illdang100.mollyspring.repository.pet;

import kr.co.kumoh.illdang100.mollyspring.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByAccount_IdAndPetName(Long accountId, String petName);

    List<Pet> findByAccount_Id(Long accountId);
}
