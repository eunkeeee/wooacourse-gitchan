# Authentication

### **💋** Basic Authentication

- 클라이언트는 간단하게 `username` 과 `password` 로 인증 가능

인증을 하기 위해서는 `credentials` (`username` 과 `password`)을 *********Authorization********* HTTP header에 특정 형식으로 넣어서 전달해야 한다. header에 넣는 예시는 아래와 같다. 

```jsx
Basic YmFlbGR1bmc6SHR0cENsaWVudA==
```

Basic 이라는 키워드로 시작해서 뒤에 *username:password* 의 내용을 가진 base64-encoded value가 온다. 

### **💋 Java HttpClient**

일반적인 GET Request부터 살펴보자.

아래의 예시 코드에서는 인증과 관련된 내용은 지금은 아직 없다.

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

여기서 로그를 체크해보면,

<img width="497" alt="스크린샷 2023-04-26 오후 1 43 13" src="https://user-images.githubusercontent.com/107979804/234471921-c08387ac-6ef7-445e-b715-02f970ced624.png">

HTTP status 200 → 요청이 성공했다는 것을 알 수 있다!

### **💋** HttpClient Authenticator 사용하기

Postman Echo endpoint의 URL을 사용해보자!

```java
HttpRequest request = HttpRequest.newBuilder()
  .GET()
  .uri(new URI("https://postman-echo.com/basic-auth"))
  .build();
```

request 부분을 이렇게 바꾸어서 실행해봤다. 

```
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://postman-echo.com/basic-auth"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Status {}", response.statusCode());
    }
```


<img width="493" alt="스크린샷 2023-04-26 오후 1 44 41" src="https://user-images.githubusercontent.com/107979804/234472081-a313f441-e23f-40a4-b568-f1b8cd56328f.png">


HTTP status 401 → `Unauthorized`된 것이고, 이 응답은 endpoint는 인증을 필요로 하는데, 클라이언트는 아무 `credential`도 보내지 않았다는 것을 의미한다. 

그러면 클라이언트가 `username`과 `password`로 이루어진 `credential`을 보내도록 바구면 되잖?

```
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

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://postman-echo.com/basic-auth"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Status {}", response.statusCode());
    }
}
```

이거 이대로 돌리면!


<img width="501" alt="스크린샷 2023-04-26 오후 1 47 54" src="https://user-images.githubusercontent.com/107979804/234472470-e673616a-bc13-451e-ab07-c891b5311588.png">


