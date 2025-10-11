package com.softcafe.restaurant_system;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantSystemApplication {

  public static void main(String[] args) {
    File env = new File(".env");
    if (env.exists()) {
      Dotenv dotenv = Dotenv.load();
      dotenv.entries().forEach(entry -> {
        System.setProperty(entry.getKey(), entry.getValue());
      });
    }
    SpringApplication.run(RestaurantSystemApplication.class, args);
  }

}
