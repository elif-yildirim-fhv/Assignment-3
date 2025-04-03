package sem4.ea.ss25.battleship.assignment2.board_service.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.board_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.BoardMessage;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.ShipDTO;

import java.io.IOException;
import java.util.Map;

@Service
public class BoardMessageReceiver {
	private final BoardService boardService;
	private final BoardMessageSender boardMessageSender;

	public BoardMessageReceiver(BoardService boardService, BoardMessageSender boardMessageSender) {
		this.boardService = boardService;
		this.boardMessageSender = boardMessageSender;
	}

	@RabbitListener(queues = RabbitMQConfig.BOARD_COMMANDS_QUEUE)
	public void handleBoardCommands(BoardMessage message, Channel channel,
									@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		try {
			switch (message.getType()) {
				case "CREATE_BOARD":
					BoardDTO board = boardService.createBoard();
					boardMessageSender.sendBoardCreatedEvent(board);
					break;

				case "PLACE_SHIP":
					ShipDTO ship = boardService.placeShip(
							message.getBoardId(),
							message.getPlayerId(),
							message.getLength(),
							message.getX(),
							message.getY(),
							message.isHorizontal()
					);
					boardMessageSender.sendShipPlacedEvent(message.getBoardId(), message.getPlayerId());
					break;

				case "MAKE_GUESS":
					String result = boardService.makeGuess(
							message.getGameId(),
							message.getBoardId(),
							message.getPlayerId(),
							message.getOpponentId(),
							message.getX(),
							message.getY()
					);
					break;

				case "END_GAME":
					boardService.endGame(message.getBoardId());
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
				case "GAME_CREATED":
					break;

				case "GAME_ENDED":
					if (message.containsKey("boardId")) {
						Long boardId = Long.valueOf(message.get("boardId").toString());
						boardService.endGame(boardId);
					}
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