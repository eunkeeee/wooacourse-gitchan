package com.example.inflearnstudy;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class DataConnectionClient {
    private String url;

    public DataConnectionClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    public void disConnect() {
        System.out.println("close + " + url);
    }

    @PostConstruct
    public void init() {
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() {
        disConnect();
    }
}
