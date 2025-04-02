package sem4.ea.ss25.battleship.assignment2.player_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.player_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.player_service.domain.PlayerMessage;

@Service
public class PlayerMessageSender {
	private final RabbitTemplate rabbitTemplate;

	public PlayerMessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendCreatePlayerCommand(PlayerMessage message) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.PLAYER_COMMANDS_ROUTING_KEY,
				message
		);
	}

	public void sendPlayerCreatedEvent(PlayerDTO player) {
		PlayerMessage message = new PlayerMessage();
		message.setType("PLAYER_CREATED");
		message.setPlayerId(player.id());
		message.setPlayerName(player.name());
		message.setPlayerData(player);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.PLAYER_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendScoreUpdatedEvent(PlayerDTO player, boolean isHit) {
		PlayerMessage message = new PlayerMessage();
		message.setType("SCORE_UPDATED");
		message.setPlayerId(player.id());
		message.setHit(isHit);
		message.setPlayerData(player);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.PLAYER_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendGameResultEvent(PlayerDTO player, boolean isWinner) {
		PlayerMessage message = new PlayerMessage();
		message.setType(isWinner ? "GAME_WON" : "GAME_LOST");
		message.setPlayerId(player.id());
		message.setPlayerData(player);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.PLAYER_EVENTS_ROUTING_KEY,
				message
		);
	}
}