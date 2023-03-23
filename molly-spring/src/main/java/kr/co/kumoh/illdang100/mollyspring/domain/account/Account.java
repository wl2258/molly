package kr.co.kumoh.illdang100.mollyspring.domain.account;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import kr.co.kumoh.illdang100.mollyspring.domain.image.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 25)
    private String username;

    @Column(unique = false, length = 60) // 패스워드 인코딩(BCrypt)
    private String password;

    @Column(unique = false, length = 20)
    private String fullName;

    @Column(unique = true, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountEnum role; // ADMIN, CUSTOMER

    @Column(unique = true, length = 25)
    private String email;

    @Embedded
    private ImageFile accountProfileImage;

    @Builder
    public Account(Long id, String username, String password, String fullName, String nickname, AccountEnum role, String email, ImageFile accountProfileImage) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.role = role;
        this.email = email;
        this.accountProfileImage = accountProfileImage;
    }
}
