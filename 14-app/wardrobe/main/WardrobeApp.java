package com.mindera.wardrobe.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.mindera.wardrobe.http"})
public class WardrobeApp {
    public static void main(String[] args) {

        SpringApplication.run( WardrobeApp.class, args ) ;
    }
}
