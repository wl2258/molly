package kr.co.kumoh.illdang100.mollyspring.dto.board;

import kr.co.kumoh.illdang100.mollyspring.domain.board.BoardEnum;
import kr.co.kumoh.illdang100.mollyspring.domain.image.BoardImage;
import kr.co.kumoh.illdang100.mollyspring.domain.pet.PetTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BoardReqDto {

    @Getter
    @Setter
    public static class CreatePostRequest {

        private String title;
        private String content;
        private List<BoardImage> boardImages;
        private BoardEnum category;
        private PetTypeEnum petType;

    }
}
