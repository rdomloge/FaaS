package com.example.faas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.faas.reactor.rmq.Receiver;

@Configuration
public class RabbitConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);

	@Value("${RMQ_HOST:li1088-171.members.linode.com}")
	private String host;

	@Value("${RMQ_USER:guest}")
	private String username;

	@Value("${RMQ_PASSWORD:guest}")
	private String password;
	
	@Value("${RMQ_VHOST:faas}")
	private String vhost;
	
	@Value("${RMQ_RESPONSE_EXCHANGE:faas-response-exchange}")
	private String responseExchange;
	
	@Value("${RMQ_REQUEST_QUEUE:faas-request-queue}")
	private String requestQueue;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		LOGGER.debug("Connecting {}/{} to {}", username, password, host);
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(vhost);
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate r = new RabbitTemplate(connectionFactory());
		r.setExchange(responseExchange);
		return r;
	}

	@Bean 
	public MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receive");
	}
	
	@Bean 
	public Exchange responseExchange() {
		return new DirectExchange(responseExchange);
	}
	
	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connFac, 
			MessageListenerAdapter adapter) {
		
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connFac);
		container.setQueueNames(requestQueue);
		container.setMessageListener(adapter);
		return container;
	}
}
