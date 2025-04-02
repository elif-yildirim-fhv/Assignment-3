package sem4.ea.ss25.battleship.assignment2.player_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.player_service.config.RabbitMQConfig;

@Service
public class PlayerMessageSender {
	private final RabbitTemplate rabbitTemplate;

	public PlayerMessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendCreatePlayerMessage(String name) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.ROUTING_KEY,
				name
		);
	}
}