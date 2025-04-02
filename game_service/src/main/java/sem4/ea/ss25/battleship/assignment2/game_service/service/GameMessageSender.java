package sem4.ea.ss25.battleship.assignment2.game_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.game_service.config.RabbitMQConfig;

@Service
public class GameMessageSender {
	private final RabbitTemplate rabbitTemplate;

	public GameMessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendCreateGameMessage() {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.ROUTING_KEY,
				"create_game"
		);
	}
}