package com.example.resttaskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestTaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestTaskManagerApplication.class, args);
    }

    /*
        There are three predefined users in the DB with roles ADMIN and USER.

        | Login         | Password | Role  |
        |---------------|:--------:|:-----:|
        | mike@mail.com |   1111   | ADMIN |
        | nick@mail.com |   2222   | USER  |
        | nora@mail.com |   3333   | USER  |
    */

}
