package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by @maximilientyc on 24/04/2016.
 */

@SpringBootApplication
public class SampleApplication {

	private UserRepository userRepository;
	private ConversationRepository conversationRepository;
	private MessageRepository messageRepository;

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

	@Bean
	public UserRepository userRepository() {
		UserRepository userRepository = new SampleUserRepository();
		return userRepository;
	}

	@Bean
	public ParticipantFactory participantFactory() {
		return new ParticipantFactory(userRepository);
	}

	@Bean
	public ConversationRepository conversationRepository() {
		return new SampleConversationRepository();
	}

	@Bean
	public MessageRepository messageRepository() {
		return new SampleMessageRepository();
	}

	@Bean
	public ConversationFactory conversationFactory() {
		ConversationService conversationService = new ConversationService(conversationRepository, messageRepository);
		return new ConversationFactory(conversationService);
	}

}
