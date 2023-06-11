package kr.co.kumoh.illdang100.mollyspring.domain.account;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(unique = false, length = 60) // 패스워드 인코딩(BCrypt)
    private String password;

    @Column(unique = true, length = 10)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AccountEnum role; // ADMIN, CUSTOMER

    @Column(unique = true, length = 45)
    private String email;

    @Embedded
    private ImageFile accountProfileImage;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileImage(ImageFile imageFile) {
        this.accountProfileImage = imageFile;
    }
}
