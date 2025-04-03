package sem4.ea.ss25.battleship.assignment2.player_service.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.player_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.player_service.dto.PlayerDTO;
import sem4.ea.ss25.battleship.assignment2.player_service.domain.PlayerMessage;

import java.io.IOException;
import java.util.Map;

@Service
public class PlayerMessageReceiver {
	private final PlayerService playerService;
	private final PlayerMessageSender playerMessageSender;

	public PlayerMessageReceiver(PlayerService playerService, PlayerMessageSender playerMessageSender) {
		this.playerService = playerService;
		this.playerMessageSender = playerMessageSender;
	}

	@RabbitListener(queues = RabbitMQConfig.PLAYER_COMMANDS_QUEUE)
	public void handlePlayerCommands(PlayerMessage message, Channel channel,
									 @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		try {
			switch (message.getType()) {
				case "CREATE_PLAYER":
					PlayerDTO player = playerService.createPlayer(message.getPlayerName());
					playerMessageSender.sendPlayerCreatedEvent(player);
					break;

				case "UPDATE_SCORE":
					PlayerDTO updatedPlayer = playerService.updateScore(message.getPlayerId(), message.isHit());
					playerMessageSender.sendScoreUpdatedEvent(updatedPlayer, message.isHit());
					break;

				default:
					break;
			}
			channel.basicAck(tag, false);
		} catch (Exception e) {
			channel.basicNack(tag, false, true);
		}
	}

	@RabbitListener(queues = RabbitMQConfig.BOARD_EVENTS_QUEUE)
	public void handleBoardEvents(Map<String, Object> message, Channel channel,
								  @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		try {
			String type = (String) message.get("type");

			if (type == null) {
				channel.basicAck(tag, false);
				return;
			}

			switch (type) {
				case "SHIP_HIT":
				case "SHOT_MISSED":
					Long playerId = Long.valueOf(message.get("playerId").toString());
					boolean isHit = "SHIP_HIT".equals(type);

					PlayerDTO updatedPlayer = playerService.updateScore(playerId, isHit);
					playerMessageSender.sendScoreUpdatedEvent(updatedPlayer, isHit);
					break;

				default:
					break;
			}
			channel.basicAck(tag, false);
		} catch (Exception e) {
			channel.basicNack(tag, false, true);
		}
	}

	@RabbitListener(queues = RabbitMQConfig.GAME_EVENTS_QUEUE)
	public void handleGameEvents(Map<String, Object> message, Channel channel,
								 @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		try {
			String type = (String) message.get("type");

			if (type == null) {
				channel.basicAck(tag, false);
				return;
			}

			switch (type) {
				case "GAME_WON":
					Long winnerId = Long.valueOf(message.get("playerId").toString());
					PlayerDTO winner = playerService.recordGameResult(winnerId, true);
					playerMessageSender.sendGameResultEvent(winner, true);
					break;

				default:
					break;
			}
			channel.basicAck(tag, false);
		} catch (Exception e) {
			channel.basicNack(tag, false, true);
		}
	}
}