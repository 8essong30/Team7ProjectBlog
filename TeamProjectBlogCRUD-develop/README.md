
> # 7ㅏ즈아  Blog
블로그 백엔드 서버 만들기

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

<br>

<details><summary>💻 프로젝트 개발 환경</summary>

- spring 3.0.1

- JDK 17
- build.gradle
  ```
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    
    compileOnly group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.11.2'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.11.2'
    runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.11.2'
    implementation group: 'org.springframework.boot', name: 'spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-security'
```

- application.properties

  ```
    spring.h2.console.enabled=true
    spring.datasource.url=jdbc:h2:mem:db;MODE=MYSQL;
    spring.datasource.username=sa
    spring.datasource.password=

    spring.thymeleaf.cache=false

    spring.jpa.properties.hibernate.show_sql=true
    logging.level.org.hibernate.type.descriptor.sql=trace
    
    jwt.secret.key=7ZWt7ZW0OTntmZTsnbTtjIXtlZzqta3snYTrhIjrqLjshLjqs4TroZzrgpjslYTqsIDsnpDtm4zrpa3tlZzqsJzrsJzsnpDrpbzrp4zrk6TslrTqsIDsnpA=

</details>

<br>

## 👥 팀원 소개
박정훈, 송경헌, 이송언, 조용연

### 역할
-  **조용연**
    - 스프링시큐리티 처리
- **이송언**
    - 게시글, 댓글 좋아요 기능
- **박정훈**
    - RefreshToken 적용
    - 게시글 카테고리 생성
        - 카테고리 별로 게시글을 조회하는 기능 추가하기
    - 회원탈퇴 기능추가
        -  게시글 삭제, 댓글 삭제 시 연관된 데이터 모두 삭제될 수 있도록 구현

- **송경헌**
    - 대댓글 기능
        - 대댓글 작성
        - 댓글 조회 시 대댓글도 함께 조회
    - 게시글과 댓글 조회 시 페이징, 정렬 기능을 추가

<br>

## 프로젝트 요구사항
<details><summary> 명세
</summary>- 프로젝트에 Spring Security 적용하기

1. 회원 가입 API
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글 수정 / 삭제 가능


2. 로그인 API
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고,
      발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태 코드 와 함께 Client에 반환하기


3. 전체 게시글 목록 조회 API
    - 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    - 작성 날짜 기준 내림차순으로 정렬하기
    - 각각의 게시글에 등록된 모든 댓글을 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
    - 게시글/댓글에 ‘좋아요’ 개수도 함께 반환하기


4.게시글 작성 API
- ~~토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능~~  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 제목, 작성자명(username), 작성 내용을 저장하고
    - 저장된 게시글을 Client 로 반환하기
5. 선택한 게시글 조회 API
    - 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기
      (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    - 선택한 게시글에 등록된 모든 댓글을 선택한 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
    - 게시글/댓글에 ‘좋아요’ 개수도 함께 반환하기
6. 선택한 게시글 수정 API
    - ~~토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능~~  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    - 게시글에 ‘좋아요’ 개수도 함께 반환하기
7. 선택한 게시글 삭제 API
    - ~~토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능~~  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
8. 댓글 작성 API
    - ~~토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능~~  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 게시글의 DB 저장 유무를 확인하기
    - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
9. 댓글 수정 API
    - ~~토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능~~  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
    - 댓글에 ‘좋아요’ 개수도 함께 반환하기
10. 댓글 삭제 API
    - ~~토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능~~  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
11. 예외 처리
    1. 예외처리를 AOP 를 활용하여 구현하기
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 회원가입 시 username과 password의 구성이 알맞지 않으면 에러메시지와 statusCode: 400을 Client에 반환하기
12. 게시글 좋아요 API
    - 사용자는 선택한 게시글에 ‘좋아요’를 할 수 있습니다.
    - 사용자가 이미 ‘좋아요’한 게시글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    - 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
13. 댓글 좋아요 API
    - 사용자는 선택한 댓글에 ‘좋아요’를 할 수 있습니다.
    - 사용자가 이미 ‘좋아요’한 댓글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    - 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기

### 추가기능

- 회원탈퇴(기능추가), 게시글 삭제, 댓글 삭제 시 연관된 데이터 모두 삭제될 수 있도록 구현해 보세요!
- 대댓글 기능을 만들어 보세요!
    - 대댓글 작성하기
    - 댓글 조회 시 대댓글도 함께 조회하기
- 게시글과 댓글 조회할 때 페이징, 정렬 기능을 추가해 보세요!
- 게시글 카테고리 만들어 보세요!
    - 게시글을 분류하는 카테고리를 만들어서 게시글을 작성할 때 카테고리 정보도 함께 저장하기
    - 카테고리 별로 게시글을 조회하는 기능 추가하기
- AccessToken, RefreshToken에 대해 구글링해 보고 RefreshToken을 적용해 보세요!
</details>

<br>

## Class UML
![](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/9864fbee-ac7c-470d-954b-6a8b31c0bd49/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230104%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230104T122314Z&X-Amz-Expires=86400&X-Amz-Signature=1208fbc48c5c14590560252eaaaca27f9b61b9f8dbea42214616da243601b844&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled.png%22&x-id=GetObject)

<br>

## Table ERD
![](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/8b8261ec-e6db-4d72-869c-3d1a0a060c76/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230104%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230104T122357Z&X-Amz-Expires=86400&X-Amz-Signature=c011de0856de3b043b74801b2385230b9042fa8d6f318f8e17115764029bd51e&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled.png%22&x-id=GetObject)

<br>

## API 명세

- ### User API
|  기능  | RestAPI | URI                      | Request                                             | Response                       |
|:----:|:-------:|--------------------------|:----------------------------------------------------|:-------------------------------|
| 회원가입 |  POST   | api/users/signup         | {  <br> username : String, password : String  <br>} | success  수정수정할것                |
| 로그인  |  POST   | api/users/signin         | { <br> username : String, password : String <br> }  | success 수정수정수정수정~!!!!!!!!!!!!! |
| 회원탈퇴 | DELETE  | api/users/resign????!!!! | Header Authorization : Bearer \<JWT>                | 탈퇴성공                           |

<br>

- ### Admin API
|     기능     | RestAPI |                      URI                      | Request                                                                                                                                                                 | Response                                                                                                                                                                       |
|:----------:|:-------:|:---------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 게시글 강제 수정  |   PUT   |           api/admin/posts/{postId}            | Header Authorization : Bearer \<JWT> <br> PostRequestDTO <br>postId<br>{<br>        title : String <br>  username : String  <br>contents : String <br>}                 | 게시글 수정 성공  <br> {<br>   createAt : LocalDateTime<br>   modifiedAt : LocalDateTime<br>   Id : Long<br>   title : String<br>   contents : String<br>   username : String   <br>} |
| 게시글 강제 삭제  | DELETE  |           api/admin/posts/{postId}            | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br>postId<br>                                                                                                | 게시글 삭제 성공                                                                                                                                                                      |
|  댓글 강제 수정  |   PUT   | api/admin/posts/{postId}/comments/{commentId} | Header Authorization : Bearer \<JWT> <br> CommentRequestDTO <br>postId<br>commentId<br>{<br>        title : String <br>  username : String  <br>contents : String <br>} | 댓글 수정 성공  <br> {<br>   createAt : LocalDateTime<br>   modifiedAt : LocalDateTime<br>   Id : Long<br>   contents : String<br>   username : String   <br>}                       |
|  댓글 강제 삭제  | DELETE  | api/admin/posts/{postId}/comments/{commentId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br>postId<br> commentId                                                                                      | 댓글 삭제 성공                                                                                                                                                                       |

<br>

- ### Post API
|       기능       | RestAPI |        URI         | Request                                                                                        | Response                                                                                                                                                                                                                                               |
|:--------------:|:-------:|:------------------:|:-----------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|   전체 게시글 조회    |   GET   |     api/posts      |                                                                                                | {<br>   createAt : LocalDateTime<br>   modifiedAt : LocalDateTime<br>   Id : Long<br>   title : String<br>   contents : String<br>   username : String   <br> comments : List <br> postLIkesCount : int   <br> likedUsers: List<br>}                   |
|   선택 게시글 조회    |   GET   | api/posts/{postId} | Header Authorization : Bearer \<JWT> <br>  postId                                              | {<br>   createAt : LocalDateTime<br>   modifiedAt : LocalDateTime<br>   Id : Long<br>   title : String<br>   contents : String<br>   username : String   <br> comments : List <br> postLIkesCount : int   <br> likedUsers: List<br>}                   |
|     게시글 작성     |  POST   |     api/posts      | UserDetailsImpl <br> {<br> title : String <br>  username : String  <br>contents : String <br>} | 게시글 작성 성공!  <br> {<br>   createAt : LocalDateTime<br>   modifiedAt : LocalDateTime<br>   Id : Long<br>   title : String<br>   contents : String<br>   username : String   <br> comments : List <br> postLIkesCount : int   <br> likedUsers: List<br>}  |
|   선택 게시글 수정    |   PUT   | api/posts/{postId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br>postId                           | 게시글 수정 성공!   <br> {<br>   createAt : LocalDateTime<br>   modifiedAt : LocalDateTime<br>   Id : Long<br>   title : String<br>   contents : String<br>   username : String   <br> comments : List <br> postLIkesCount : int   <br> likedUsers: List<br>} |
|   선택 게시글 삭제    | DELETE  | api/posts/{postId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br>  postId                         | 게시글 삭제 성공!                                                                                                                                                                                                                                             |
| 게시글 좋아요 or 취소  |  POST   | api/posts/{postId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br> postId                          | 게시글에 좋아요를 했습니다. / 좋아요가 취소되었습니다                                                                                                                                                                                                                         |

<br>

- ### Comment API
|      기능       | RestAPI |                   URI                   | Request                                                                                                                     | Response                                                                                                                                                                                                          |
|:-------------:|:-------:|:---------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|     댓글 작성     |  POST   |       api/posts/{postId}/comments       | UserDetailsImpl <br> {<br>comment : String <br>id : Long <br>}                                                              | 댓글 작성 성공! <br> id: Long, <br>writer : String, <br> contents : String, <br>modifiedAt : LocalDateTime, <br>createdAt : LocalDateTime, <br>commentLikesCount : int, <br>likedUsers : List, <br>commentList : List   |
|   선택 댓글 수정    |   PUT   | api/posts/{postId}/comments/{commentId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br>postId<br>commentId {<br>comment : String <br>id : Long <br>} | 댓글 수정 성공!   <br> id: Long, <br>writer : String, <br> contents : String, <br>modifiedAt : LocalDateTime, <br>createdAt : LocalDateTime, <br>commentLikesCount : int, <br>likedUsers : List, <br>commentList : List |
|   선택 댓글 삭제    | DELETE  | api/posts/{postId}/comments/{commentId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br>  postId <br>commentId                                        | 댓글 삭제 성공!                                                                                                                                                                                                         |
| 댓글 좋아요 or 취소  |  POST   | api/posts/{postId}/comments/{commentId} | Header Authorization : Bearer \<JWT> <br> UserDetailsImpl <br> postId <br>commentId                                         | 댓글에 좋아요를 했습니다.    / 좋아요가 취소되었습니다                                                                                                                                                                                  |
