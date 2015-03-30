## Overview
#### 플레이의 컨트롤러
- 웹 애플리케이션의 URL구조를 설계한 경험이 있는가?
- 그러나 URL을 설계하는 것은 중요하다.

#### 기존 자바EE의 URL의 문제점(;스트럿츠 1.x)
- /product.do
- product.do?ean=5010255079763
- product.do/ean=5010255079763?method=edit
- URL들이 구현에 특화되어 있다.
- .do 확장자는 프레임워크가 요청을 어떤 클래스에 맵핑해야 하는지 나타내지만 아무 의미도 없음
- 쿼리스트링 파라미터를 특정 액션을 위해 사용하는게 부적절함
- productEdit.do?ean=5010255079763 으로 리팩토링? => 아쉽게도 딱히 대안이 있는 것도 아님
- 접두사나 확장자로 URL 맵핑을 하려고 시도 한다.

#### 훌륭한 URI는 변하지 않는다!!
- http://www.w3.org/Provider/Style/URI.html

#### 플레이의 안정된 URL
- /products
- /product/5010255079763
- /product/5010255079763/edit

####좋은URL설계의 장점
- 애플리케이션을 쉽게 이해할 수 있다.
- URL이 잘 변하지 않는다.
- 간결하다.

#### 소스코드 설명
app/controllers/Products.scala 파일
```
package controllers

import play.api.mvc.{Action, Controller}

/**
 * A controller class with four action methods.
 */
object Products extends Controller {

  def list(pageNumber: Int) = Action {   // #1 Show product list
    NotImplemented
  }

  def details(ean: Long) = Action {      // #2 Show product details
    NotImplemented
  }

  def edit(ean: Long) = Action {         // #3 Edit product details
    NotImplemented
  }

  def update(ean: Long) = Action {       // #4 Update product details
    NotImplemented
  }
}
```

conf/route 파일
```
GET /                    controllers.Application.home()

GET /products            controllers.Products.list(page: Int ?= 1)

GET /product/:ean        controllers.Products.details(ean: Long)

GET /product/:ean/edit   controllers.Products.edit(ean: Long)

PUT /product/:ean        controllers.Products.update(ean: Long)
```

####route 파일 tip
- route 파일에서 위에 있을 수록 우선순위가 높다
- 슬래시를 포함한 경로에 매치될 수 있도록 :대신 *을 사용할 수 있다.
 - 만약 아래와 같이 선언한다면 /assets/ 아래에 오는 모든 문자열(/ 포함)은 file 파라미터에 바운드 된다.
 - URL이 /assets/images/sample.png 인 경우 file은 images/sample.png 이 됨
````
GET /assets/*file         controllers.Assets.at(path = "/public", file)
````

####라우팅을 수행할 때 다음과 같은 일이 일어난다
1. 라우터는 요청URL을 통해 구성된 경로 비교하여 적절한 경로를 찾는다.
2. 라우터는 타입에 특화된 바인더중 하나를 사용하여 매개변수를 바인딩한다.
 - 기본적으로 String으로 변환되지만 id: Long과 같은 타입 명시가 있으면 해당 타입으로 변환
 - 타입 변환 시 오류가 발생하면 HTTP 코드 400(Bad Request)를 반환
3. 라우터는 컨트롤러의 액션을 호출하고, 매개변수를 전달한다.

####리버스 라우팅
- 라우팅은 URL이 액션 메소드로 바인딩 되는 것
- 리버스 라우팅은 컨트롤러 액션을 통해 URL을 찾아내는 것

#### 응답 생성하기
- 컨트롤러의 Action으로 생성
- 플레인 텍스트 - Ok("Version 2.3.8")
- HTML - Ok(view.html.index())
- JSON - val success = Map("status" -> "success");  val json = Json.toJson(success); Ok(json)
- XML - Ok(<status>success</status>)
- Binary data
