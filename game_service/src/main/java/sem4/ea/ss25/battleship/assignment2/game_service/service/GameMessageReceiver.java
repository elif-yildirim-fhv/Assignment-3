package sem4.ea.ss25.battleship.assignment2.game_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.game_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.game_service.dto.GameDTO;


@Service
public class GameMessageReceiver {
	private final GameService gameService;

	public GameMessageReceiver(GameService gameService) {
		this.gameService = gameService;
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public GameDTO handleCreateGameMessage(String message) {
		return gameService.createGame();
	}
}