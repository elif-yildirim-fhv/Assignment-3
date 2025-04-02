package sem4.ea.ss25.battleship.assignment2.player_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.player_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.player_service.service.PlayerService;

@Service
public class PlayerMessageReceiver {
	private final PlayerService playerService;

	public PlayerMessageReceiver(PlayerService playerService) {
		this.playerService = playerService;
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public PlayerDTO handleCreatePlayerMessage(String name) {
		return playerService.createPlayer(name);
	}
}