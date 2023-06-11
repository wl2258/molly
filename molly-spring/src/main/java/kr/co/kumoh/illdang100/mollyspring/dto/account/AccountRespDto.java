package kr.co.kumoh.illdang100.mollyspring.dto.account;

import kr.co.kumoh.illdang100.mollyspring.domain.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class AccountRespDto {

    @Data
    @AllArgsConstructor
    @Builder
    public static class AccountProfileResponse {

        private String profileImage;
        private String nickname;
        private String provider;
        private String email;

        public AccountProfileResponse(Account account) {
            this.nickname = account.getNickname();
            this.provider = getProviderFromUsername(account.getUsername());
            this.email = account.getEmail();
        }

        private String getProviderFromUsername(String username) {
            int idx = username.indexOf('_');
            return username.substring(0, idx);
        }
    }
}
