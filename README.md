# molly
애완동물 예방접종과 동물병원에 대한 정보를 쉽게 접할 수 있고, 애완동물 주인들의 정보 공유를 위한 커뮤니티 기능을 제공하는 웹 사이트
<p align="center"><img src="https://github.com/illdang100/molly/assets/80948330/b04a0a8a-d833-4083-9f7a-30387bf9b0c1.png" width="100%"/></p>

## 💡 매뉴얼 (youtube)
[![Watch the video](https://img.youtube.com/vi/ryWhWpxDxIs/maxresdefault.jpg)](https://youtu.be/ryWhWpxDxIs)

## ☑️ 핵심기능 설명
#### ✔️ 예방접종 일정 관리 및 알림 기능 
반려동물의 예방접종 일정을 관리하는 기능을 제공합니다. <br/>
사용자는 각각의 반려동물 프로필을 생성하여 예방접종 일정을 등록할 수 있습니다. <br/>
또한, 알림 기능을 통해 사용자가 일정을 놓치지 않도록 도와줍니다. <br/>

#### ✔️ 반려동물 의료 기록 관리 기능
반려동물의 의료 기록을 관리하는 기능을 제공합니다. <br/>
사용자는 각각의 반려동물 프로필에 건강 관련 정보를 기록할 수 있으며, 이를 통해 예방접종 일정과 의료 기록을 한 곳에서 효율적으로 관리할 수 있습니다. <br/>

#### ✔️ 커뮤니티 기능
반려동물을 키우는 사람들을 위한 커뮤니티 기능을 제공합니다. <br/>
사용자들은 자유롭게 글을 작성하고, 다른 사용자들과 의견을 나눌 수 있습니다. <br/>
이를 통해 정보를 공유하고 사용자들 간 소통을 도모할 수 있습니다. <br/>

#### ✔️ 동물 병원 위치 및 정보 제공 기능
사용자들에게 가까운 동물 병원의 위치와 정보를 제공하는 기능을 포함하고 있습니다. <br/>
사용자는 지도를 통해 주변 동물 병원을 쉽게 찾을 수 있으며, 영업 여부 및 구글에서 제공하는 해당 병원에 대한 평점과 같은 각 병원의 정보를 쉽게 확인할 수 있습니다. <br/>

#### ✔️ 관리자 기능
웹 사이트의 관리자는 커뮤니티 게시글을 모니터링할 수 있는 권한을 가지고 있습니다. <br/>
관리자는 커뮤니티 게시글과 댓글에 대한 삭제 작업을 수행할 수 있으며, 다른 사용자들에게 해가 되는 행동을 하는 사용자를 정지시킬 수 있는 권한을 가집니다. <br/>

## 🐾 개발 일정
<p align="center"><img src="https://github.com/illdang100/molly/assets/80948330/0a187191-23bd-4a88-af95-92965db7a065.png" width=100% /></p>

## 🌈 전체 시스템 구조
<p align="center"><img src="https://user-images.githubusercontent.com/97449471/232280029-59eebf27-43aa-4687-805d-875ca19097df.png" width=700 height=450 /></p>

## 📃 데이터베이스 구조
<p align="center"><img src="https://github.com/illdang100/molly/assets/80948330/5b4bcf66-3b96-40cf-9002-6ec35d0bc573.png" width=700 height=450 /></p>

## 🖨️ Api 명세서
<p align="center"><img src="https://github.com/illdang100/molly/assets/80948330/47c93e0e-f8df-46f2-b716-249dc0d39777.png" width=700 /></p>

## 👥 역할 분담
#### Back-end
- 정연준(PM) : 소셜로그인, 회원가입, 커뮤니티, 관리자 기능
- 손지민 : 반려동물 등록, 예방접종 일정 기능
  
#### Front-end
- 장윤정 : 웹 디자인, 프론트엔드 기능

## 📦 Packages
#### SERVER 주요 OPENSOURCE
| Name | Description |
| --- | --- |
| `Spring Security` |  애플리케이션의 보안 및 인증 처리 |
| `Spring OAuth2 Client` |  OAuth 2.0 클라이언트 구현 |
| `com.auth0:java-jwt` |  JWT(Json Web Token) 생성 및 검증을 위한 라이브러리 |
| `com.querydsl:querydsl-jpa` |  Querydsl 를 사용하여 JPA 쿼리를 생성하기 위한 라이브러리 |
| `com.querydsl:querydsl-apt` |  Querydsl 어노테이션 프로세서를 사용하여 쿼리 타입 클래스를 생성하기 위한 라이브러리 |
| `org.springframework.cloud:spring-cloud-starter-aws` |  스프링 클라우드에서 AWS를 사용하기 위한 라이브러리 |

#### Dependencies Module (package.json) 
```javascript
{
  "name": "molly-react",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "@ckeditor/ckeditor5-build-classic": "^37.1.0",
    "@ckeditor/ckeditor5-react": "^6.0.0",
    "@googlemaps/react-wrapper": "^1.1.35",
    "@react-google-maps/api": "^2.18.1",
    "@reduxjs/toolkit": "^1.9.5",
    "@testing-library/jest-dom": "^5.16.5",
    "@testing-library/react": "^13.4.0",
    "@testing-library/user-event": "^13.5.0",
    "axios": "^1.3.4",
    "date-fns": "^2.29.3",
    "google-map-react": "^2.2.1",
    "moment": "^2.29.4",
    "react": "^18.2.0",
    "react-datepicker": "^4.10.0",
    "react-dom": "^18.2.0",
    "react-icons": "^4.8.0",
    "react-redux": "^8.0.5",
    "react-router-dom": "^6.9.0",
    "react-scripts": "5.0.1",
    "react-spinners": "^0.13.8",
    "redux": "^4.2.1",
    "redux-persist": "^6.0.0",
    "styled-components": "^5.3.8",
    "web-vitals": "^2.1.4"
  }
}
```

## ⚒️ Tools
<span><img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=React&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Spring-5FB832?style=flat-square&logo=Spring&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/MySql-00758F?style=flat-square&logo=MySql&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Amazon-FF9900?style=flat-square&logo=Amazon&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/GitHub-000000?style=flat-square&logo=GitHub&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/></span>




