package kr.co.kumoh.illdang100.mollyspring.domain.image;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class ImageFile  {
    private String uploadFileName;
    private String storeFileName;
}
