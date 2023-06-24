# Spring Lv.1

# Use Case

![Untitled](https://github.com/siwoo1224/spring-week1/assets/96398475/49250d2c-3595-4f30-ac17-c10eacad1a93)

# API 명세서

![API](https://github.com/siwoo1224/spring-week1/assets/96398475/16a6bd51-6368-43e1-9d32-c58ce5958a20)


<aside>
❓ **Why: 과제 제출시에는 아래 질문을 고민해보고 답변을 함께 제출해주세요.**

</aside>

1. 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)

    ⇒  body


2. 어떤 상황에 어떤 방식의 request를 써야하나요?
- Path Variable
    - 데이터를 받기 위해서는 `/star/{name}/age/{age}`  이처럼 URL 경로에서 데이터를 받고자 하는 위치의 경로에 {data} 중괄호를 사용한다.
    - `(@PathVariable String name, @PathVariable int age)`
        - 그리고 해당 요청 메서드 파라미터에 @PathVariable 애너테이션과 함께 {name} 중괄호에 선언한 변수명과 변수타입을 선언하면 해당 경로의 데이터를 받아올 수 있습니다.
- form 태그 POST,  Query String 방식
    - `?name=Robbie&age=95` 처럼 데이터형태를 처리함
    - @RequestParam `(@RequestParam String name, @RequestParam int age)`
        - 해당 요청 메서드 파라미터에 @RequestParam 애너테이션과 함께 key 부분에 선언한 변수명과 변수타입을 선언하면 데이터를 받아올 수 있습니다.
    - `?name=Robbie&age=95` 처럼 데이터가 두 개만 있다면 괜찮지만 여러 개 있다면 @RequestParam 애너테이션으로 하나 씩 받아오기 힘들 수 있습니다. 이때 @ModelAttribute 사용해서 객체로 데이터를 받아옴
- @RequestBody
    - HTTP Body에 JSON 데이터를 담아 서버에 전달할 때 해당 Body 데이터를 Java의 객체로 전달 받을 수 있다.
    - HTTP Body에 `{"name":"Robbie","age":"95"}`  `JSON` 형태로 데이터가 서버에 전달되었을 때 @RequestBody 애너테이션을 사용해 데이터를 객체 형태로 받을 수 있습니다.


3. RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?

    ⇒ API의 리소스 식별자를  중복 없이 고유하게 잘 만들었고, 해당 API에 적절하게 HTTP 메서드를 사용했다.


4.  적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)

    ⇒ DTO,Entity는 잘 분리했지만 현제는 컨트롤러에 Controller, Repository(메모리 DB), Service가 작성되어 있는 거 같습니다.


5. API 명세서 작성 가이드라인을 검색하여 직접 작성한 API 명세서와 비교해보세요!

![Untitled 1](https://github.com/siwoo1224/spring-week1/assets/96398475/28eb038b-0411-4708-8d1b-4582617d2d18)

검색해서 찾은건다. 기능, 메서드, path는 동일하다.

추가 내용 쿼리스트링, Request Body, Response Body, 상태 코드, 예외까지 있는걸 볼 수 있다.