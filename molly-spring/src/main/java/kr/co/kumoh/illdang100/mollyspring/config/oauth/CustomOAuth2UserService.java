package kr.co.kumoh.illdang100.mollyspring.config.oauth;

import kr.co.kumoh.illdang100.mollyspring.config.auth.PrincipalDetails;
import kr.co.kumoh.illdang100.mollyspring.config.oauth.provider.GoogleUserInfo;
import kr.co.kumoh.illdang100.mollyspring.config.oauth.provider.KakaoUserInfo;
import kr.co.kumoh.illdang100.mollyspring.config.oauth.provider.OAuth2UserInfo;
import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import kr.co.kumoh.illdang100.mollyspring.domain.account.AccountEnum;
import kr.co.kumoh.illdang100.mollyspring.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // TODO: 테스트 진행하기!!

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String username = "";
        if (registrationId.equals("google")) {

            log.info("구글 로그인 요청!");

            String providerId = oAuth2User.getAttribute("sub");
            username = registrationId + "_" + providerId;

            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {

            log.info("카카오 로그인 요청!");

            long providerId = (long) attributes.get("id");
            username = registrationId + "_" + providerId;

            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.error("지원하지 않는 소셜 로그인");
            // TODO: 예외 발생시키고 FailureHandler 처리하기!!
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인");
//            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_request", "The requested provider is not supported", null), "The requested provider is not supported");
        }

        Optional<Account> accountOptional = accountRepository.findByUsername(username);

        // TODO: 소셜 로그인 후 입력 못받은 정보 따로 입력받기
        // 입력 못받은 정보 있으면 추가 정보 입력 받는 페이지로 리다이렉트??
        Account account;
        String oauthEmail = oAuth2UserInfo.getEmail();
        if (accountOptional.isEmpty()) {

            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            account = Account.builder()
                    .username(username)
                    .email(oauthEmail)
                    .role(AccountEnum.CUSTOMER)
                    .build();
            accountRepository.save(account);
        } else {
            account = accountOptional.get();

            String email = account.getEmail();
            if (!oauthEmail.equals(email)) {
                account.changeEmail(oauthEmail);

                accountRepository.save(account);
            }
        }

        return new PrincipalDetails(account, oAuth2User.getAttributes());
    }
}
