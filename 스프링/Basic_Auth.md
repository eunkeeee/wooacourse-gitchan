## 💋 Authentication

인증은 **사용자의 신원을 확인하는 과정**이다. 

스프링에서 인증은 보안과 직결되기 때문에 매우 중요한 부분이며, 아래와 같은 절차를 따른다.



> 1. 사용자가 로그인 페이지에 접근하여 아이디와 비밀번호를 입력합니다.
> 2. 입력받은 아이디와 비밀번호를 가지고 인증 매커니즘을 통해 사용자의 정보를 확인합니다.
> 3. 사용자 정보가 확인되면, 인증 매커니즘은 사용자의 권한 정보까지 확인하여 인증된 사용자로 세션을 생성합니다.




스프링에서 지원하는 인증 방법은 여러 가지가있다. 
대표적으로는 폼 인증(Form Authentication), HTTP 기본 인증(Basic Authentication), OAuth2 등이 있다.


오늘은 그중에서 HTTP 기본 인증(Basic Authentication)에 대해 공부해보려고 한다! 


## **💋** HTTP 기본 인증(Basic Authentication)

HTTP 기본 인증은 **HTTP header를 사용하는 인증 방법** 중에 하나이다. 


인증을 하기 위해서는, 내가 나라는 걸 증명해야 한다. 증명하기 위해서는 가장 쉽게 생각해보면 `username` 과 `password`이 필요하다. 

HTTP 기본 인증 방식을 사용하면 클라이언트는 간단하게 `username` 과 `password` 를 header에 보내서 인증 가능하다. 

근데... `username` 과 `password` 를 그대로 보내면 안되겠지...?



### ✔ 헤더에 보낼 수 있는 형태로 암호화하자!


인증 정보를 떡하니 `깃짱:내비밀번호는비밀이지롱`으로 보내게 되면, 누구나 header를 보고 개인정보를 쓱 가져갈 수 있다. 이 방식은 암호화하는 것이다. 

이 암호화에 사용할 방법은 **Base64로 인코딩**이다. 이름이 낯선데, 직접 할 일은 없다. 인코딩해주는 메서드를 가져다 쓰면 되는 것이다. 

`credential`은 `username`과 `password`를 콜론(:)으로 이어붙인 문자열이다. 

예를 들어, username이 `john`이고 password가 `doe`인 경우, credential은 `john:doe`이며, 이를 Base64로 인코딩하면 `am9objpkb2U=`가 된다.

```
username: john
password: doe
credential: john:doe
credential을 Base64로 인코딩: am9objpkb2U=
```


이렇게 인코딩해서 포장한 인증 정보를 **Authorization 헤더에 실어서 서버에 전송**한다. 

헤더에 넣게 될 때는 방금 인코딩한 값의 맨 앞에 Basic 이라는 키워드를 붙여서 보낸다!




먼저, client가 request를 보내서 서버로부터 response를 받는 과정을 자바 코드로 만들어보자. 

그래야지 위에서 설명한 내용을 header에 넣어서 보내는 방법도 자연스럽게 된다. 



### ✔ Java HttpClient을 사용해서 요청을 보내고 응답을 받아보자! (인증 내용 X)

일반적인 GET Request부터 살펴보자.

이 부분은 아직 인증과 관련된 부분이 아니고, 클라이언트가 요청을 보내서 응답을 받는 연습을 하는 코드이다. 


```java
// HTTP 요청을 실행하는데 필요한 HttpClient 생성
HttpClient client = HttpClient.newHttpClient(); 

// builder 패턴을 사용해서 HttpRequest 생성
HttpRequest request = HttpRequest.newBuilder()
  .GET()
  .uri(new URI("https://postman-echo.com/get"))
  .build(); 

// 앞서 만든 클라이언트로 요청을 보냄
// 우리가 response body를 String처럼 사용하고 싶다는 것을 보여줌
HttpResponse<String> response = client.send(request, BodyHandlers.ofString()); 

logger.info("Status {}", response.statusCode());
```

_[참고] 추가적으로 HttpClient는 Java 11부터 제공되는 API이므로, 이전 버전의 Java를 사용하는 경우, Apache HttpComponents나 OkHttp와 같은 외부 라이브러리를 사용해야 한다._

여기서 로그를 체크해보면,

<img width="497" alt="스크린샷 2023-04-26 오후 1 43 13" src="https://user-images.githubusercontent.com/107979804/234471921-c08387ac-6ef7-445e-b715-02f970ced624.png">

HTTP status 200 → 요청이 성공했다는 것을 알 수 있다!

요청을 잘 받는 연습을 했다. 이제 인증까지 우겨넣어볼까?


### ✔ HttpClient Authenticator에 인증 정보 없이 요청을 보내고 실패해보자!


이제 요청 부분을 조금 바꿔서, 인증을 해달라고 하는 요청으로 보내보자!

아래와 같이 작성했는데, 보다시피 `basic-auth`로 보내니깐 인증을 해달라는 말인데, 우리가 맨 위에서 공부했던 인코딩해서 헤더에 보내는 그런 내용은 없다. 이러면 거절당하는 엔딩이겠지..? 그래도 한 번 확인해보자.



```java
HttpRequest request = HttpRequest.newBuilder()
  .GET()
  .uri(new URI("https://postman-echo.com/basic-auth"))
  .build();
```

request 부분을 이렇게 바꾸어서 실행해봤다. 

```java
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        // HttpClient는 기본적으로 인증 정보를 제공하지 않으므로, 개발자가 직접 정보를 생성하고 요청에 추가해야 한다. 

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://postman-echo.com/basic-auth"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Status {}", response.statusCode());
    }
```


<img width="493" alt="스크린샷 2023-04-26 오후 1 44 41" src="https://user-images.githubusercontent.com/107979804/234472081-a313f441-e23f-40a4-b568-f1b8cd56328f.png">


HTTP status 401 → `사용자가 인증에 실패`했다는 의미이다. 클라이언트가 인증에 필요한 정보를 보내지 않았고, 예측한 대로 역시나 실패! 

그러면, 클라이언트가 인증에 필요한 데이터를 보내도록 바꿔주면 되잖?


## 💋 HTTP 기본 인증(Basic Authentication) 방식을 사용해서 인증에 성공해보자!

아래에 설명되는 두 방식은 모두 HTTP 요청 헤더에 인증 정보를 포함시키는 것으로, 구현 방식은 약간 다르지만 기능적으로 동일하다.

### ✔ HttpClient.Builder의 authenticator 메소드를 사용하여 인증 정보를 제공


```java
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthentication {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("postman", "password".toCharArray());
                        // 이름: postman, 비밀번호: password
                        // 인증 해 주세요! 
                    }
                })
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://postman-echo.com/basic-auth"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Status {}", response.statusCode());
    }
}
```

이거 이대로 실행시키면!


<img width="501" alt="스크린샷 2023-04-26 오후 1 47 54" src="https://user-images.githubusercontent.com/107979804/234472470-e673616a-bc13-451e-ab07-c891b5311588.png">

인증에 필요한 정보를 함께 보내줬더니 200이라고 잘 되었다고 뜬다.



### ✔ HttpRequest.Builder의 header 메소드를 사용하여 Authorization 헤더를 직접 추가

앞에서 말한 내용처럼 `credentials` (`username` 과 `password`)을 주물주물해서 *********Authorization********* HTTP header에 특정 형식으로 넣어서 전달해야 한다. 이 특정 형식 아래에서 보여줄 메서드로 맞출 수 있다. 

```java
private static final String getBasicAuthenticationHeader(String username, String password) {
    String valueToEncode = username + ":" + password; 
    // credential 만들기: 이름이랑 비밀번호를 ':'로 연결하면 됨
    return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    // 위에서 만든 credential을 Base64로 인코딩하고, 앞에 'Basic'을 붙이면 끝!
}
```

이걸 HTTP header에 넣어서 전달하면 인증이 된다는 소리임!

위에서 하던 거에서 바뀐 부분 위주로 주석에 설명해놨음!

```java
public class BasicAuthentication {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("postman", "password".toCharArray());
                    }
                })
                .build();

        // request를 보낼 때, header의 "Authorization"에 인증을 위한 정보를 암호화해서 넣는다. 
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://postman-echo.com/basic-auth"))
                .header("Authorization", getBasicAuthenticationHeader("postman", "password"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Status {}", response.statusCode());
    }

    // 인증 관련 정보(credential)을 암호화하고, 전송 형식에 맞게 반환한다. 
    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
```


이렇게 하면 인증 성공했다는 status 200과 함께 성공한다...! 야호


<img width="2560" alt="스크린샷 2023-04-26 오후 2 24 16" src="https://user-images.githubusercontent.com/107979804/234477320-84cb6e73-f391-4995-8cf4-22f4e2db2b8c.png">







### 💋 하지만, HTTP 기본 인증은 보안성이 떨어진다. 

그런데, 우리가 이렇게 열심히 공부한 이 방법은 그닥 좋은 방법은 아니다. 

HTTP 기본 인증(Basic Authentication) 방식은 사용자의 아이디와 비밀번호를 요청 헤더에 포함시켜 보내기 때문에, 요청을 가로채면 아이디와 비밀번호를 쉽게 볼 수 있다. 또한, 요청 헤더를 수정하는 공격(Header Injection)도 가능하다.

HTTPS와 함께 사용하면 그나마 보완할 수 있다. HTTPS를 사용하면, 클라이언트와 서버 간 통신이 암호화되기 때문에, 중간에 요청을 가로채더라도 아이디와 비밀번호를 알아내기가 어려워진다. 또한, HTTPS를 사용하면 요청 헤더를 수정하는 공격도 방지할 수 있다. HTTPS는 요청 헤더와 응답 헤더를 모두 암호화하기 때문에, 요청이나 응답을 가로채더라도 헤더를 수정하는 것이 불가능하다.



