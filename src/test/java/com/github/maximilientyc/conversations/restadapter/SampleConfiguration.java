package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.MessageRepository;
import com.github.maximilientyc.conversations.domain.repositories.UserRepository;
import com.github.maximilientyc.conversations.domain.services.ConversationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @maximilientyc on 24/04/2016.
 */

@Configuration
public class SampleConfiguration {

	private UserRepository userRepository;
	private ConversationRepository conversationRepository;
	private MessageRepository messageRepository;

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
