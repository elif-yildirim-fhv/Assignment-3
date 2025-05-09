package sem4.ea.ss25.battleship.assignment2.player_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
public class PlayerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlayerServiceApplication.class, args);
	}
}