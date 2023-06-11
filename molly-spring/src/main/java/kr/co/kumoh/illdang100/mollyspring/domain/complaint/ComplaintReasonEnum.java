package kr.co.kumoh.illdang100.mollyspring.domain.complaint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ComplaintReasonEnum {
    SPAM_PROMOTION("스팸홍보/도배글입니다."),
    PORNOGRAPHY("음란물입니다."),
    ILLEGAL_INFORMATION("불법정보를 포함하고 있습니다."),
    HARMFUL_TO_MINORS("청소년에게 유해한 내용입니다."),
    OFFENSIVE_EXPRESSION("욕설/생명경시/혐오/차별적 표현입니다."),
    PERSONAL_INFORMATION_EXPOSURE("개인정보 노출 게시물입니다."),
    UNPLEASANT_EXPRESSION("불쾌한 표현이 있습니다."),
    ANIMAL_CRUELTY("동물 학대 관련 내용입니다."),
    FAKE_INFORMATION("가짜 정보를 유포하고 있습니다.");
    private final String value;
}
