package kr.co.kumoh.illdang100.mollyspring.security.oauth;

import kr.co.kumoh.illdang100.mollyspring.handler.ex.CustomOAuth2AuthenticationException;
import kr.co.kumoh.illdang100.mollyspring.repository.suspension.SuspensionDateRepository;
import kr.co.kumoh.illdang100.mollyspring.security.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.security.oauth.provider.GoogleUserInfo;
import kr.co.kumoh.illdang100.mollyspring.security.oauth.provider.KakaoUserInfo;
import kr.co.kumoh.illdang100.mollyspring.security.oauth.provider.OAuth2UserInfo;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;
    private final SuspensionDateRepository suspensionDateRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String username = getUsername(oAuth2User, registrationId, attributes);
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, attributes);

        validateUserSuspension(oAuth2UserInfo.getEmail());

        Account account = accountRepository.findByUsername(username)
                .orElseGet(() -> createNewAccount(username, oAuth2UserInfo.getEmail()));

        return new PrincipalDetails(account, attributes);
    }

    private String getUsername(OAuth2User oAuth2User, String registrationId, Map<String, Object> attributes) {
        String providerId;
        if (registrationId.equals("google")) {
            log.debug("구글 로그인 요청!");
            providerId = oAuth2User.getAttribute("sub");
        } else if (registrationId.equals("kakao")) {
            log.debug("카카오 로그인 요청!");
            providerId = String.valueOf(attributes.get("id"));
        } else {
            log.error("지원하지 않는 소셜 로그인");
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다");
        }
        return registrationId + "_" + providerId;
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return new GoogleUserInfo(attributes);
        } else if (registrationId.equals("kakao")) {
            return new KakaoUserInfo(attributes);
        } else {
            log.error("지원하지 않는 소셜 로그인");
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다");
        }
    }

    private void validateUserSuspension(String email) {
        suspensionDateRepository.findByAccountEmail(email).ifPresent(suspensionDate -> {
            log.info("정지된 사용자 계정입니다.");
            if (!LocalDate.now().isAfter(suspensionDate.getSuspensionExpiryDate())) {
                throw new CustomOAuth2AuthenticationException("정지된 사용자 계정입니다. 정지 기간:" + suspensionDate.getSuspensionExpiryDate());
            }
        });
    }

    private Account createNewAccount(String username, String email) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        Account account = Account.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(AccountEnum.CUSTOMER)
                .build();

        return accountRepository.save(account);
    }
}
