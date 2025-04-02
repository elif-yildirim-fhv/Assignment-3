package sem4.ea.ss25.battleship.assignment2.board_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.board_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.BoardMessage;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.ShipDTO;

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
	public void handleBoardCommands(BoardMessage message) {
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
				// The hit result event is already sent in the boardService.makeGuess method
				break;

			case "END_GAME":
				boardService.endGame(message.getBoardId());
				break;

			default:
				break;
		}
	}

	@RabbitListener(queues = RabbitMQConfig.GAME_EVENTS_QUEUE)
	public void handleGameEvents(Map<String, Object> message) {
		String type = (String) message.get("type");

		if (type == null) {
			return;
		}

		switch (type) {
			case "GAME_CREATED":
				// Handle game created event if needed
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
	}
}