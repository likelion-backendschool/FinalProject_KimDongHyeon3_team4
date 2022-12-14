# 필수과제
- [x] 주문
- [x] 결제
- [x] 장바구니
- [x] PG연동

# 추가과제
- [x] 환불

# 접근방법

&nbsp;프로젝트에 앞서서 1Week_Mission에서 추가로 주어진 상품에 관한 과제를 진행해놔야지 2Week_Mission을 진행할 수 있는지 몰랐다.

주어진 2주차 과제를 해결하기 위해서는 상품에 관련된 과제를 먼저 진행해야 된다고 생각했다.

 따라서 상품 관련 CRUD를 빠르게 작성해주었다. (정답 코드가 있지만, 이미 작성한 코드가 있기에 직접 구현하는게 더 빠르다고 판단)

또한 이번 개발은 각 기능에서 우선순의를 정해서, 구현할 수 있는 기능까지만 구현 한뒤, 복합적으로 개발해야 하는 기능은
마지막 기능을 개발할 때, 진행하였다. 각 기능별로 세부 프로세스를 정리하여 진행해 볼 수 있었다.

---
## 상품 CRUD

&nbsp;상품 CRUD는 사실 크게 어렵지 않다. 이미 이전에 진행했던 다양한 프로젝트에서나 1주차에서 진행했던 글과 굉장히 유사하다

여기서 한가지 고려해야할 저머은 Keyword는 수정이 불가능하고, 존재하는 키워드내에서 선택해야 한다는 점이다.

그 외에 특이점은 없어서 사실 접근 방법이랄 것도 없이. 글과 유사하게 작성하면 된다.

---
## 장바구니

&nbsp;장바구니 기능은 이전에 Batch 관련 강의를 진행할 때 다루어 본적이 있다. 해당 강의에서는 결제까지 진행하였지만,
해당 장바구니 구현 파트에서는 결제 파트를 제외하고 장바구니에 상품을 담고 삭제하는 기능을 추가해야 한다고 생각한다.

즉, 장바구니기능을 구현하는 프로세스를 아래처럼 계획했다.

 - 상품 detail 페이지에서 장바구니에 담으면 장바구니에 담겨야 한다.
 - 장바구니에 담겨 있는 상품은 장바구니 페이지에 접속하면, 보여질 수 있어야한다.
 - 장바구니 페이지에서 장바구니에 담긴 상품을 하나 하나 삭제할 수 있어야 한다.


---
## 주문

&nbsp;주문이라고 하면 사실 결제와 같은 의미로 생각하기 쉽다. 그러나 개발을 진행하면서 분명한 분리가 필요하다.

주문을 한다고 모두 결제를 하는 것이아니라 주문 중간에 취소를 할 수도 있다. 이러한 변수를 고려하기위해 구분을 한다.

주문에서 중요한점은 2가지의 주문 방식을 고려해야한다.

 - 상품 개별적으로 하나 하나 구매하는 경우
 - 장바구니에서 한번에 구매하는 경우 (장바구니에서 선택적으로 구매하는 방식은 확장성 기능으로 분류한다.)

크게 2가지의 방식을 고려하여 기능을 개발하는 방향성을 잡았다.

그러나 주문의 기능이 간단히 2가지의 방법만으로 구현할 수 있지 않았다. Final Project 개요를 읽어보면
예치금의 시스템이 도입되어야 하기 때문에 주문에서 사용자의 예치금과 추후 있을 PG연동을 고려하여 주문을 진행해야 한다.

그러나 현 시점에서 PG연동을 진행하지 않기에 PG연동관련 주문 기능은 차후 PG연동기능을 구현할 때 구현한다.

따라서, 주문 기능을 구현하기위한 프로세스는 다음과 같다.

 - 상품 개별적으로 예치금으로 주문할 수 있다.
 - 장바구니에서 상품들을 한번에 예치금으로 주문할 수 있다.
 - 주문 과정에서 결제로 넘어가기전, 취소기능을 추가하여, 주문은 만들어졌지만, 결제가 이루어지지 않은 상태의 객체 생성

---
앞서 말한 예치금 시스템을 주문 기능을 구현하면서 같이 구현하게 되었다. 예치금 시스템의 경우 Member 테이블에 restCash 속성을 추가하여
관리하였다.
---

## 결제
&nbsp;결제는 주문 기능의 연장선이라고 생각할 수 있다. 사실 주문의 기능을 잘 구현했다면, 결제의 부분은 크게 어렵지 않다.

앞서 말한 것처럼 PG연동 전의 결제임으로 간략히 예치금의 잔여 여부를 확인하여 결제를 진행하면 된다.

이때, 결제한 금액 만큼 예치금을 차감해야 한다. 추가적으로 이러한 예치금을 활용하려면 예치금 조차고 주문, 결제의 프로세스를 따라야 한다.

이러한 예치금 충전에 관련된 기능은 PG연동 구현에서 구현하도록 한다. (예치금을 예치금으로 충전할 수는 없다.)

결론적으로 결제 기능 구현의 프로세스는 다음과 같다.

 - 상품을 개별적으로 주문한 결과에 따른 결제를 진행한다. (예치금 + - 과정)
 - 장바구니에서 상품들을 한번에 주문한 결과에 따른 결제를 진행한다. (예치금 + - 과정)


----
&nbsp;주문, 결제 기능을 진행하면서 이전의 강의들을 참고하고 주어진 ERD를 보면 현금의 흐름을 꼭 기록해야 차후 정산, 통계의 서비스를 구현함에 있어서 문제가 
되지 않음을 알 수 있다.

 따라서 주문, 결제 기능을 진행하면서 해당 내용을 기록할 수 있는 기록 기능 구현을 추가적으로 구현하였다.

 - 이때, 로그를 남기는 경우는 결제가 된 경우에서 로그를 남긴다.

---

## PG연동
&nbsp;대망의 PG 연동이다 그동안 개발은 진행하면서 미루어 왔던 추가 기능들을 해당 기능 구현을 진행하면서 모두 진행해야했다.

많은 내용들이 담겨 있어서 좀 체계적으로 구현할 기능을 명세하는게 필요해 보였다. PG연동은 토스페이먼츠를 이용해서 개발하면
기본 구조는 토스페이먼즈 문서 또는 강의에서 진행했던 코드를 활용하면서 진행하였다.

PG연동 기능을 구현의 프로세스를 아래와 같이 정의하였다.

 - 개발 상품에 따른 주문 -> 결제로 이루어지는 주문, 결제 기능 (예치금 / 토스페이먼츠 / 예치금 + 토스페이먼츠 3가지의 경우수 고려)
 - 장바구니 상품에 따른 주문 -> 결제로 이루어지는 주문, 결제 기능 (예치금 / 토스페이먼츠 / 예치금 + 토스페이먼츠 3가지의 경우수 고려)
 - 각 결제에 따른 예치금 (+ - 기능)
 - 예치금 충전을 하기위한 주문 -> 결제 기능
 - 토스페이먼츠 결제에 따른, 현금흐름 기록

총 5가지의 세부 기능을 고려하여 개발을 진행했다.

---

## 환불 & MyBook
&nbsp;환불의 기능은 생각보다 크게 어렵지 않게 구현했다. (기존에 확작성으로 남겨둔, 장바구니 선택적 주문/결제 기능을 포함하지 않았기에)

해당 환불 기능은 MyBook 기능과 연관되어 구현하였다. MyBook 기능은 결제가 완료되었을 때, 해당 상품(도서)를 담아두는 테이블로

결제와 연관되어 구현을 진행하면 된다고 생각했다. 따러서 환불과 MyBook 기능을 구현하려는 프로세스는 아래와 같다.

 - 결제가 완료되었다면, 결제된 상품들을 사용자 마이페이지에 띄운다.
 - 상품 내용 외에 구매 내역을 출력하는 View를 추가로 제작한다.
 - 제작한 구매 내역 view를 활용하여 환불 기능을 추가하여, 구매내역 별로 환불을 할 수 있도록 한다.(이때 환불금은 모두 예치금으로 반환된다.)
 - 환불의 경우 10분의 제약사항이 있기에 서버 local 시간과 주문한 시간의 비교가 필요하다.

# 특이사항

 - **[장바구니]**

&nbsp;장바구니의 경우 확장성 기능에 대한 이슈가 있었다. 확장성 기능이란 "장바구니 선택적 주문 기능"이다.
차후 리팩토링을 통해 해당 기능을 추가를 고려해보아야 한다.

- **[리스트]**

&nbsp;사실.... 리스트 해당 부분은 1주차의 리팩토링 과정이였지만, 깜빡하고 진행하지 못하였던 부분이다. HashTag를 선택하면, 해당
해시태그를 포함하는 글들이 보여지도록 리팩토링이 필요하다.

- **[Security]**

&nbsp;해당 부분도 1주차에 리팩토링하지 못하였던 부분이다. @PreAuthorize를 활용하여 수정하는 방식으로 변경해야한다고 생각한다.

그러나, 해당 문제가 발생하는 이유는 분명히 Security를 통해서 url 접근을 막았지만 어떤 이슈인지 모르겠지만, 권한 부여가
되지 않은 사람도 url을 통해 접근이 가능하다는 문제점이다. 해당 이슈에 대한 원인 파악과 해결이 필요하다.


- **[테스트코드]**

&nbsp;1주차를 진행하면서, 테스트코드의 부재로 인해서 개발의 효율성이 떨어짐을 느겼었지만 
직접 실행을 해보면서 테스트를 하는 방식과 테스트 코드를 통해서 진행하는 방식을 비교해서 생각해 본다면, 협업의 측면에서는

테스트 코드의 효율성이 높아지겠지만, 해당 프로젝트를 오랜기간 운영하지 않거나 혼자 개발하거나 이러한 경우
개인적으로 직접 실행을 해보면서 테스트 하는 방식이 좀 더 가시적으로 오류의 내용을 파악할 수 있어서 혼자 개발할때는 해당 방식을
선호하기는 한다..... 그러나 해당 방식의 문제점은 명확히 나와있기에 테스트 코드를 조금 작성해볼 수 있었지만,

그리 유요한 테스트 코드라고 보기 어렵기 때문에, 차후 테스트 코드에 대한 고찰이 더필요해 보인다.