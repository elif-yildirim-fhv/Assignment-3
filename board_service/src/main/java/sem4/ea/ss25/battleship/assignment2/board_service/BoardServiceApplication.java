package sem4.ea.ss25.battleship.assignment2.board_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@EnableFeignClients(basePackages = "sem4.ea.ss25.battleship.assignment2.board_service.service")
public class BoardServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardServiceApplication.class, args);
	}
}