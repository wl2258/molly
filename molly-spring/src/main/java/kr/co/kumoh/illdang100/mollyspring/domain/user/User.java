package kr.co.kumoh.illdang100.mollyspring.domain.user;

import kr.co.kumoh.illdang100.mollyspring.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 25)
    private String username;

    @Column(unique = false, length = 60) // 패스워드 인코딩(BCrypt)
    private String password;

    @Column(unique = false, length = 20)
    private String fullname;

    @Column(unique = false, length = 20)
    private String nickname;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role; // ADMIN, CUSTOMER

    @Builder
    public User(Long id, String username, String password, String fullname, String nickname, String profileImage, UserEnum role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }
}
