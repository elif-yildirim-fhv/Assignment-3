package sem4.ea.ss25.battleship.assignment2.board_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sem4.ea.ss25.battleship.assignment2.board_service.config.RabbitMQConfig;
import sem4.ea.ss25.battleship.assignment2.board_service.dto.BoardDTO;
import sem4.ea.ss25.battleship.assignment2.board_service.domain.BoardMessage;

@Service
public class BoardMessageSender {
	private final RabbitTemplate rabbitTemplate;

	public BoardMessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendCreateBoardCommand(BoardMessage message) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.BOARD_COMMANDS_ROUTING_KEY,
				message
		);
	}

	public void sendBoardCreatedEvent(BoardDTO board) {
		BoardMessage message = new BoardMessage();
		message.setType("BOARD_CREATED");
		message.setBoardId(board.id());
		message.setBoardData(board);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.BOARD_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendShipPlacedEvent(Long boardId, Long playerId) {
		BoardMessage message = new BoardMessage();
		message.setType("SHIP_PLACED");
		message.setBoardId(boardId);
		message.setPlayerId(playerId);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.BOARD_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendHitResultEvent(Long gameId, Long boardId, Long playerId, boolean isHit) {
		BoardMessage message = new BoardMessage();
		message.setType(isHit ? "SHIP_HIT" : "SHOT_MISSED");
		message.setGameId(gameId);
		message.setBoardId(boardId);
		message.setPlayerId(playerId);
		message.setHit(isHit);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.BOARD_EVENTS_ROUTING_KEY,
				message
		);
	}

	public void sendMakeGuessCommand(BoardMessage message) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.BOARD_COMMANDS_ROUTING_KEY,
				message
		);
	}

	public void sendGameOverEvent(Long gameId, Long boardId, Long playerId) {
		BoardMessage message = new BoardMessage();
		message.setType("GAME_OVER");
		message.setGameId(gameId);
		message.setBoardId(boardId);
		message.setPlayerId(playerId);

		rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE,
				RabbitMQConfig.BOARD_EVENTS_ROUTING_KEY,
				message
		);
	}
}