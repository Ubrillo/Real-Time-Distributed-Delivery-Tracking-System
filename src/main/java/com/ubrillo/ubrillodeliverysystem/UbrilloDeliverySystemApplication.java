package com.ubrillo.ubrillodeliverysystem;

import com.ubrillo.ubrillodeliverysystem.Controller.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class UbrilloDeliverySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(UbrilloDeliverySystemApplication.class, args);
        Controller management = new Controller();

    }
}