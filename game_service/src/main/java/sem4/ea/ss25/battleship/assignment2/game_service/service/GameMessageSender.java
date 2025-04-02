package sem4.ea.ss25.battleship.assignment2.game_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.game_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameMessage;

@Service
public class GameMessageSender {
	private final RabbitTemplate rabbitTemplate;

	public GameMessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendCreateGameCommand(GameMessage message) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.GAME_COMMANDS_ROUTING_KEY,
				message
		);
	}

	public void sendGameCreatedEvent(GameDTO game) {
		GameMessage message = new GameMessage();
		message.setType("GAME_CREATED");
		message.setGameId(game.id());
		message.setBoardId(game.boardId());
		message.setPlayerIds(game.playerIds());
		message.setGameData(game);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.GAME_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendPlayerAddedEvent(Long gameId, Long playerId) {
		GameMessage message = new GameMessage();
		message.setType("PLAYER_ADDED");
		message.setGameId(gameId);
		message.setPlayerId(playerId);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.GAME_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendGameEndedEvent(Long gameId) {
		GameMessage message = new GameMessage();
		message.setType("GAME_ENDED");
		message.setGameId(gameId);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.GAME_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendGameWonEvent(Long gameId, Long playerId) {
		GameMessage message = new GameMessage();
		message.setType("GAME_WON");
		message.setGameId(gameId);
		message.setPlayerId(playerId);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.GAME_EVENTS_ROUTING_KEY,
				message
		);
	}
}