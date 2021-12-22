package com.ernyka.cardmanager;

import com.ernyka.cardmanager.api.Client;
import com.ernyka.cardmanager.api.model.Comment;
import com.ernyka.cardmanager.api.model.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CardManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardManagerApplication.class, args);
    }

}
