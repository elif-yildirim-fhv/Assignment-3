package sem4.ea.ss25.battleship.assignment2.game_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String EXCHANGE = "battleship_exchange";

	public static final String GAME_COMMANDS_QUEUE = "game_commands_queue";
	public static final String GAME_EVENTS_QUEUE = "game_events_queue";
	public static final String BOARD_EVENTS_QUEUE = "board_events_queue";

	public static final String GAME_COMMANDS_ROUTING_KEY = "game.commands";
	public static final String GAME_EVENTS_ROUTING_KEY = "game.events";
	public static final String BOARD_EVENTS_ROUTING_KEY = "board.events";

	@Bean
	public Queue gameCommandsQueue() {
		return new Queue(GAME_COMMANDS_QUEUE);
	}

	@Bean
	public Queue gameEventsQueue() {
		return new Queue(GAME_EVENTS_QUEUE);
	}

	@Bean
	public Queue boardEventsQueue() {
		return new Queue(BOARD_EVENTS_QUEUE);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE);
	}

	@Bean
	public Binding gameCommandsBinding(Queue gameCommandsQueue, TopicExchange exchange) {
		return BindingBuilder.bind(gameCommandsQueue).to(exchange).with(GAME_COMMANDS_ROUTING_KEY);
	}

	@Bean
	public Binding gameEventsBinding(Queue gameEventsQueue, TopicExchange exchange) {
		return BindingBuilder.bind(gameEventsQueue).to(exchange).with(GAME_EVENTS_ROUTING_KEY);
	}

	@Bean
	public Binding boardEventsBinding(Queue boardEventsQueue, TopicExchange exchange) {
		return BindingBuilder.bind(boardEventsQueue).to(exchange).with(BOARD_EVENTS_ROUTING_KEY);
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}
}