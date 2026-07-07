# 📝 문득 - 문화생활 블로깅 커뮤니티

> 한국IT직업전문학교 2023-2학기 심화프로젝트 · **우수상 수상**

전시, 공연, 독서 같은 문화생활 경험을 기록하고 다른 사람들과 공유할 수 있는 블로깅 커뮤니티 서비스입니다. Frontend 1명, Backend 1명으로 이루어진 2인 팀 프로젝트로, 저는 Backend 전반(Controller, Model, DB, 인증, 실시간 통신)을 담당했습니다.

---

## 프로젝트 정보

| 항목 | 내용 |
|---|---|
| 유형 | 팀 프로젝트 (심화프로젝트) |
| 성과 | 우수상 |
| 팀 구성 | Frontend 1명 / Backend 1명 |
| 담당 | Backend |
| 스택 | Java, Spring MVC, Thymeleaf, MySQL, MyBatis, Redis, WebSocket, Spring Security |

---

## 왜 이런 구조로 만들었나

둘이서 개발하다 보니 서로 작업 영역이 겹치지 않는 게 중요했습니다. 그래서 Spring MVC의 Controller / Model / View 분리를 최대한 활용해서, 프론트엔드 팀원은 View만 신경 쓰고 저는 Controller와 Model 쪽만 작업하는 식으로 역할을 나눴습니다.

```java
@GetMapping("/join")
public String getJoin(Model model) {
    JoinForm joinForm = new JoinForm();
    model.addAttribute("joinForm", joinForm);
    return "join/join";
}
```

---

## 주요 구현 내용

**공통 화면 요소 처리**
헤더처럼 페이지마다 반복되는 요소는 Thymeleaf `th:replace`로 컴포넌트화했습니다. `th:each`, `th:if`를 활용해 목록을 그리거나 조건부 렌더링, 입력값 검증까지 뷰 레벨에서 처리해 사용자가 잘못된 값을 넣었을 때 바로 걸러지도록 했습니다.

**DB 설계**
부모-자식 관계가 있는 테이블은 외래키로 묶어서 데이터가 중복 저장되지 않게 하고 조인도 깔끔하게 되도록 설계했습니다. 삭제나 수정이 일어났을 때 연관 데이터가 꼬이지 않도록 `CASCADE` 옵션도 적절히 활용했습니다.

**이메일 인증 (Redis)**
회원가입할 때 이메일로 인증번호를 보내는데, 이 인증번호를 Redis에 TTL과 함께 저장했습니다. 시간이 지나면 자동으로 사라지기 때문에 별도로 만료 처리 로직을 짤 필요가 없었습니다.

**실시간 1:1 채팅 (WebSocket)**
채팅 기능은 HTTP Polling 방식 대신 WebSocket으로 구현해서 실시간성을 확보했습니다.

---

## 내가 맡았던 부분

- `JavaMailSender` + Redis TTL 조합으로 이메일 인증 로직 구현
- Session 기반 로그인/로그아웃 처리
- MyBatis로 DB 연동 및 동적 쿼리 작성
- Filter로 요청 전/후 공통 처리 로직 구현
- WebSocket 핸드셰이킹 및 실시간 채팅 구현
- 행정안전부 「소프트웨어 개발보안 가이드」 기준 반영
- Java Validation으로 서버단 검증 적용
  - 클라이언트 검증만으로는 우회가 가능하고, 웹/모바일/API 등 여러 클라이언트를 상대해야 하는 상황에서는 서버단 검증이 훨씬 일관되고 안전하다는 걸 실감했습니다.
- Spring Security `BCryptPasswordEncoder`로 비밀번호 해싱
  - 솔트가 적용된 단방향 해시 방식이 무차별 대입 공격 방어에 왜 효과적인지 직접 적용해보며 이해했습니다.

---

## 트러블슈팅: 채팅이 모든 방에 뿌려지는 문제

처음 채팅 기능을 붙였을 때, 특정 채팅방에서 메시지를 보내면 그 방뿐 아니라 접속해 있던 모든 채팅방에 메시지가 함께 뿌려지는 문제가 있었습니다.

원인을 찾아보니 `WebSocketHandler`가 채팅방을 구분하지 않고 연결된 모든 세션에 그냥 메시지를 broadcast하고 있었습니다.

해결 방법으로, 각 채팅방에 고유 UUID를 부여하고 이걸 Model에서 JS로 넘겨준 다음, 메시지를 보낼 때 `roomId`도 같이 실어 보내도록 수정했습니다. 서버에서는 이 roomId와 세션의 접속 주소를 비교해서 같은 방의 세션에만 메시지를 전달하도록 처리했습니다.

```java
String goalRoom = "ws://.../ws/chat/" + chatMessage.getRoomId();
if (roomSession.getUri().toString().equals(goalRoom)) {
    roomSession.sendMessage(message);
}
```

이 과정에서 STOMP가 destination(구독 경로) 기준으로 메시지를 라우팅한다는 것, 그리고 같은 topic을 여러 클라이언트가 구독하고 있으면 의도치 않게 전체 브로드캐스트가 될 수 있다는 걸 제대로 이해하게 됐습니다.

---

