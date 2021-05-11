package com.superheroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.eventtickets"})
@EnableAutoConfiguration(exclude = { KafkaAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class SuperheroesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SuperheroesApplication.class, args);
	}
}
