# 필수과제
- [x] JWT 로그인 구현
- [x] 내 도서 리스트 구현
- [x] 내 도서 상세정보 구현
- [x] 로그인 한 회원의 정보 구현
- [x] Spring Doc으로 API 문서화

# 추가과제
- [x] 엑세스 토큰 화이트리스트 구현(Member 엔티티에 accessToken 필드 추가)
- [x] 리액트에서 작동되도록 구현

---
# 접근방법

파이널 프로젝트 마지막 주제는 REST API에 대한 이해였다. REST API를 이해하기 위해서 간략히 
HTTP API와 연관지어 생각해 볼 수 있다. 웹 통신에서 HTTP API를 만족하면서 추가적으로
1) 자원의 식별

2) 메세지를 통한 리소스 조작

3) 자기서술적 메세지

4) 애플리케이션의 상태에 대한 엔진으로서 하이퍼미디어(HATEOAS)

4가지의 조건을 만족하는 것이 REST API라고 볼 수 있다. 사실 Rest API가 가장
HTTP API를 활용하는 API중 대중화 되어있기에 동일하게 부르기도 한다.

하지만, HTTP API가 더 큰 개념이라고 볼 수 있다.

REST API는 HTTP API이다. 는 성립하지만
HTTP API는 REST API이다. 는 항상 성립하지는 않는다.

이러한 간단한 지식을 이해하고 REST API방식과 SPRING을 활용해 구현하기 위해서
필수 & 추가 과제를 아래의 순서대로 개발을 진행 하기로 생각했다. (기능의 흐름상 일치하는 것)

1) JWT 로그인 구현
2) 로그인 한 회원의 정보 구현
3) 내 도서 리스트 구현
4) 내 도서 상세정보 구현
5) 엑세스 토큰 화이트리스트 구현
6) Spring Doc로 api 문서화
7) 리액트와 실제 연동

---
## jwt 로그인 구현
로그인 구현에서 주의 할 점은 일반적으로 우리가 Srping을 사용하면 Session을 활용하던가
jwt를 활용하는 한가지 방식을 이용했다. 그러나 하나의 프로젝트에서 2가지를 모두 사용하기
위해서는 2가지의 Security 설정을 해야 한다. 또한 리액트와의 연동을 해야하기 때문ㅇ
Security에서 리액트(3000포트), 스프링(8080포트)의 통신이 허용되도록 cors 에러를 해결해야했다.
로그인 구현에서 가장 중요한 점은 Security설정이라고 생각했다.

 강사님의 힌트 코드와 이전의 Security Security JWT부분을 참고하여 작성하였다. 로그인의
프로세스를 아래와 같이 정리하였다.

 - spring security (일정 페이지 이외) 모두 막기
 - 사용자 로그인
 - 사용자 정보 받기
 - jwt 발급
 - header에 jwt 추가
 - 응답

이떄 주의해야할 부분은 spring security 부분이다. 사실 그 이외의 부분은 이전 코드를 조금만 수정
하면 된다.

spring security를 총 2개를 따로 만들어 준뒤 REST API를 사용할 곳에 해당 메서드를 추가해서
해당 URL로 들어오는 요청은 모두 JWT로 관리할 수 있도록 만들 수 있다.

```aidl
http.antMatcher("/api/**")
```

그 후에 이전 SESSION 방식과 동일하게 csrf 기능을 끄고, 로그인이 필요한 기능은
authorizeRequests를 통해 막아준다. 이때, 반듯이 seesionManagement를 통해서
세션을 사용하지 않는다고 선언해 주는 것이 좋다.

```aidl
.sessionManagement(sessionManagement -> sessionManagement
        .sessionCreationPolicy(STATELESS)
```

이후 우리가 배운 filter를 적용해서 controller에 접근하기 전에
jwt인증을 실행하면 된다.
---

## 로그인한 회원의 정보 구현, 내 도서 리스트 구현, 내 도서 상세정보 구현

3가지 기능의 접근 방식이 동이하여, 하나로 묶어서 소개해 본다.

3가지 기능은 사실 별거 없다. 로그인이 되었다면 JWT가 생길 것이고
이때 JWT에 들어있는 정보는 이전에 우리가 Session에서 만들었던
SecurityMember 객체이다. (User를 상속한 객체 이름 입니다.)
따라서 우리는 @AuthenticationPrincipal SecurityMember securityMember
를 통해서 로그인한 사용자의 정보를 확인할 수 있다.

이제 우리는 로그인 정보를 통해서 1 ~ 3주차에서 만들었던
mybook, post, product 3가지를 DB를 조회해서 가져오기만 하면된다.

접근 방식이랄 것도 없이 단순 Select를 통해서 가져와 넘겨 주면 된다.

이때 주의할 점이 한가지 있다.

 - 반환되는 데이터 형식 정하기

반환되는 데이터의 형식이 지정되어 있기 때문에, 이전에 우리가 활용했던
Entity를 그대로 돌려주면 안된다. (필요없는 데이터도 가기 때문에)
따라서 DTO를 만들어 필요한 내용만 넣어주도록 구현하면 된다.

=> 물론 반환되는 데이터 형식은 이전에 만들었던 Util.spring.responseEntityOf와 RsData.successOf, Util.mapOf 를 통해서
구현하면 된다. 자세한 사항은 코드를 참조

---
## 엑세스 토큰 화이트리스트 구현

화이트리스트로 구현하는 방식은 간단하다. Member 엔티티에 accessToken속성을
추가하고 jwt를 해당 속성에 넣어주면 된다.

그러면 왜 화이트 리스트를 구현해야 하는지 의문이 들 수 있다.

jwt의 만료 기간 때문에 우리는 리프레시 토큰을 활용한 적이 있다.
(jwt 만료기간을 짧게 만들어 계속 리프레시 하는 방식을 의미 || 만료기간이 길면, 데이터 수정이 있어도 수정이 이루어지지 않는다.)

이때 만약 프론트에서는 리프레시 될때마다 사용자에게 로그인을
강요할 수 없기 때문에(항상 요청마다 자신의 토큰이 만료되었나 확인해야한다.), 리프레시가 되지만, 토큰은 그대로
유지하는 방식이다.

크게 어렵지 않은 구현이다. 첫 토큰 발급때, accessToken에 저장을 한 후, 다음 요청마다
JwtAuthorizationFilter에서 해당 토큰이 유요한지 체크만 하면된다.
---
## Spring Doc로 API 문서화

해당 부분은 사실 크게 접근 방법이랄 것도 없이, Spring doc의 의존성을
추가해 준후, @Tag를 통해서 설명하고 싶은 말을 적기만 하면 된다.
--

## 리액트와 실제 연동
 
기존에 주어진 요구사항의 데이터를 만족하였다면, 크게 어렵지 않게
바로 연동이 된다.

---
# 특이사항

 **- Postman**

리액트와 실제 연동을 하기 전에 Postman을 통해
사전 테스트를 진행하였다. jwt를 유지하기 위해서
Postman의 설정 방식에 대해서 배워볼 수 있었다.

 **- 리액트와 실제 연동**

리액트의 연동의 부분에서 로그인의 정보를 받는 과정에서
개별적으로 데이터를 받으려 하다 보니 맵핑이 되지 않는 오류가 났었다.
사실 좀만 생각해 본다면, 맵핑을 할 수 없는 부분이였다.
```aidl
String username, String password
```

이렇게 데이터를 단편적으로 받을 수 없다는 의미가 된다. 처음 생각에는 json의 key:value 형식이니
당연히 이렇게 되겠지 생각하였지만, {username : "", password : ""} 해당 데이터를 하나의
묶음으로 생각해야 한다. 하나의 묶음 데이터가 String username에 {username : "", password : ""}
해당 데이터가 들어가지 못하는 것은 당연하다. 따라서 아래와 같이 오류를 수정해 해결할 수 있었다.

```aidl
@Data
public class ApiMemberDto {
    String username;
    String password;
}

@RequestBody ApiMemberDto apiMemberDto
```
