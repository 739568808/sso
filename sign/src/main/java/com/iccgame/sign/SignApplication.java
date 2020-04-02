package com.iccgame.sign;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SignApplication {

    public static void main(String[] args) throws IOException {
        //SpringApplication.run(SignApplication.class, args);

        Connection.Response response = Jsoup.connect("http://www.sso.com:8080/login")
                .header("'pathvariable", "/login")
                .header("sign", "0492afd3dac6b089ffc602b60ed2a745cca0a016")
                .header("timestamp", "1585817037106")
                .data("username", "admin")
                .data("password", "1234156")
                .ignoreContentType(true)
                .method(Connection.Method.POST).execute();
        System.out.println(response.body());
    }

}
