package sem4.ea.ss25.battleship.assignment2.board_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.board_service.config.RabbitMQConfig;

@Service
public class BoardMessageSender {
	private final RabbitTemplate rabbitTemplate;

	public BoardMessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendCreateBoardMessage() {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.ROUTING_KEY,
				"create_board"
		);
	}
}