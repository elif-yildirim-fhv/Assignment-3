package sem4.ea.ss25.battleship.assignment2.game_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.game_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameMessage;

import java.util.Map;

@Service
public class GameMessageReceiver {
	private final GameService gameService;
	private final GameMessageSender gameMessageSender;

	public GameMessageReceiver(GameService gameService, GameMessageSender gameMessageSender) {
		this.gameService = gameService;
		this.gameMessageSender = gameMessageSender;
	}

	@RabbitListener(queues = RabbitMQConfig.GAME_COMMANDS_QUEUE)
	public void handleGameCommands(GameMessage message) {
		switch (message.getType()) {
			case "CREATE_GAME":
				GameDTO game = gameService.createGame();
				gameMessageSender.sendGameCreatedEvent(game);
				break;
			case "ADD_PLAYER":
				gameService.addPlayerToGame(message.getGameId(), message.getPlayerId());
				gameMessageSender.sendPlayerAddedEvent(message.getGameId(), message.getPlayerId());
				break;
			case "END_GAME":
				gameService.endGame(message.getGameId());
				gameMessageSender.sendGameEndedEvent(message.getGameId());
				break;
			default:
				break;
		}
	}

	@RabbitListener(queues = RabbitMQConfig.BOARD_EVENTS_QUEUE)
	public void handleBoardEvents(Map<String, Object> message) {
		String type = (String) message.get("type");

		if (type == null) {
			return;
		}

		switch (type) {
			case "BOARD_CREATED":
				Long boardId = Long.valueOf(message.get("boardId").toString());
				// Update game with the new board ID if needed
				break;

			case "SHIP_HIT":
			case "SHOT_MISSED":
				Long gameId = Long.valueOf(message.get("gameId").toString());
				boolean isHit = "SHIP_HIT".equals(type);

				// Check if game is over (all ships hit)
				if (isHit && gameService.checkGameOver(gameId)) {
					gameService.endGame(gameId);
					gameMessageSender.sendGameEndedEvent(gameId);
				}
				break;

			case "GAME_OVER":
				Long gameOverId = Long.valueOf(message.get("gameId").toString());
				gameService.endGame(gameOverId);
				gameMessageSender.sendGameEndedEvent(gameOverId);
				break;

			default:
				break;
		}
	}
}