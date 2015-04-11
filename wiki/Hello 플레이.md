## 개요
#### 플레이는 높은 생산성을 원하는 '웹 개발자'를 위한 웹 프레임워크
 - 플레이 1.x는 자바로 작성되었지만 플레이 2버전은 스칼라로 작성됨
 - 스칼라 웹 개발을 할 수 있도록 한 프레임워크 + 타입 안정성까지 향상
 - JVM에서 동작하고 자바를 지원하지만 자바EE 기반이 아님

#### 플레이 공식 사이트 플레이 소개 [(공식 사이트)](https://www.playframework.com)
 - 생산성 높은 모던 웹 프레임워크
 - Stateless
 - 웹 친화적 아키텍처, 특징
 - Iteratee IO는 기반, 리액티브 모델 때문에 예측 가능한 최소한의 리소스를 사용함

> Play is a high-productivity Java and Scala web application framework that integrates the components and APIs you need for modern web application development.

> Play is based on a lightweight, stateless, web-friendly architecture and features predictable and minimal resource consumption (CPU, memory, threads) for highly-scalable applications thanks to its reactive model, based on Iteratee IO.

#### 플레이 철학 [(공식 사이트)](https://www.playframework.com/documentation/2.3.x/Philosophy)
- 비동기 프로그래밍에 적합
- 타입 세이프
- 자바와 스칼라 지원
- 강력한 빌드 시스템
- 데이터 저장소와 모델 통합

#### 높은 생산성, 간결함 (개발 속도 향상)
 - 풀스택 웹 프레임워크
  - 검증된 라이브러리 사용 방법을 문서화해서 제공
 - 복잡한 설정 없이 바로 페이지 생성할 수 있음
 - 변경된 코드도 별도의 배포과정 없이 웹 브라우저에서 바로 확인
 - Netty 서버를 내장하고 있음 (별도의 외부 서버 설정 불필요)
 - 선언적인 애플리케이션 URL 설정
 - HTTP 와 스칼라 API의 타입세이프한 매핑
 - 모던 웹, 모바일 지원
  - RESTful by default
  - Asset Compiler for CoffeeScript, LESS, etc
  - JSON is a first class citizen
  - Websockets, Comet, EventSource
  - Extensive NoSQL & Big Data Support

#### HTTP 메소드 활용
 - 하나의 URL로 다른 API에 매핑할 수 있음
  - GET /product
  - PUT /product
  - DELETE /product
 - /updateProductDetails와 같은 방식이 아닌
 - 개발자가 RESTful한 URL을 설계할 수 있도록 지원
  - 위와 같은 방식으로 routes 파일에서 정의


#### 타입 세이프한 웹 개발
 - HTTP 라우팅 파일, 스칼라 뷰 템플릿 매개변수는 스칼라 타입으로 매핑됨

#### 플레이 설치
  - OSX : play Download -> /etc/paths 다운받은 activator 경로 추가
  - Windows : play Download -> 환경변수 path에 내용 추가
    (;윈도우의 경우 jdk를 별도 설치해야 합니다.)

#### 플레이 애플리케이션 생성/실행
  - 콘솔에 activator new 명령어를 통해서 애플리케이션 생성  
   `$ activator new PlayForScala play-scala`

  - 생성된 프로젝트 폴더로 이동하여 activator 실행
   ````
    $ cd PlayForScala
   $ activator
````

  - activator 콘솔에서 run 명령어를 통해서 플레이 애플리케이션 실행
  - 9000번 포트로 웹 서버 실행
   - http://localhost:9000 에서 웰컴 페이지 확인

## 플레이 애플리케이션 구조 
#### 프로젝트 폴더별 역할
  - app — 애플리케이션 소스 코드
   - controllers (액션 메서드 등 스칼라 파일들)
   - views (스칼라 뷰 템플릿으로 html로 변환되는 파일들)
  - conf — 설정 파일과 데이터
   - application.conf (애플리케이션 설정)
   - routes (HTTP URL과 스칼라 메서드 매핑)
  - project — 프로젝트 빌드 스크립트
  - public — 공개적으로 접근 가능한 정적 파일
  - test — 자동화된 테스트

#### 파일/소스 코드 설명 
routes 파일
````
     # Home page
    
     //URL을 Application.scala 파일의 index 메서드로 매핑
     GET     /               controllers.Application.index
````

Application.scala 파일
````
package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
      
      //views 폴더의 index.scala.html 템플릿에 매개변수를 전달하며 호출
      Ok(views.html.index("Your new application is ready."))     
  }
}
````

위의 예제 코드를 보면 play.api로 시작된 패키지명을 볼 수 있다, Play를 자바버전으로 실행시켜보았거나 구글링을 통해서 여러 예제를 보신 분이라면 알겠지만 Play API에는 두가지 종류의 패키지가 있다.
하나는 `play.`으로 시작하는 것과  `play.api.`로 시작하는 패키지가 있다. 예를들어 mvc패키지는 `play.mvc`와 `play.api.mvc`가 있는데 이 둘의 차이점은 전자는 Play 자바전용이고 후자는 Play 스칼라 전용이다. 따라서 이 예제를 실행시키는 스칼라 개발자분들은 play와 관련된 클래스는 play.api로 시작되는 패키지를 import하면 된다.

index.scala.html 파일
````
     //매개변수 선언부 (이 경우 1개)
     @(message: String)  
    
     //다른 뷰 템플릿 호출 : main.scala.html에 매개변수 2개 전달하며 호출
     //항상 main 템블릿을 호출해야하는 것은 아님. 공통 헤더, 풋더, 메뉴 등을 main에 만들어서 재사용할 경우 유용
    
     //첫번째 매개변수 String 타입 
     @main("Welcome to Play") {  

        //두번째 매개변수 html 타입
        @play20.welcome(message) 

     }
````

main.scala.html 파일 : 위 뷰 템플릿에서 호출되는 함수
````
      //두개의 매개변수 선언
      @(title: String)(content: Html)  

      <!DOCTYPE html>
      <html>
         <head>
            //첫번째 매개변수 사용
            <title>@title</title>
            <link rel="stylesheet" media="screen" href="@routes.Assets.at("stylesheets/main.css")">
            <link rel="shortcut icon" type="image/png" href="@routes.Assets.at("images/favicon.png")">
            <script src="@routes.Assets.at("javascripts/jquery-1.9.0.min.js")" type="text/javascript"></script>
        </head>
       <body>
          //두번째 매개변수 사용
          @content
       </body>
     </html>
````

## Hello world 출력
