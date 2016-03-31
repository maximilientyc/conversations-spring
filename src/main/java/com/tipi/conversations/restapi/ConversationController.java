package com.tipi.conversations.restapi;

import com.tipi.conversations.api.CreateConversationCommand;
import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationFactory;
import com.tipi.conversations.domain.ConversationRepository;
import com.tipi.conversations.domain.ParticipantFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
@Controller
public class ConversationController {

	private ParticipantFactory participantFactory;
	private ConversationRepository conversationRepository;
	private ConversationFactory conversationFactory;

	public ConversationController(ParticipantFactory participantFactory, ConversationRepository conversationRepository, ConversationFactory conversationFactory) {
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.conversationFactory = conversationFactory;
	}

	public String postConversation(@RequestBody CreateConversationForm createConversationForm) {
		Conversation conversation = conversationFactory.buildConversation();
		for (String userId : createConversationForm.getUserIds()) {
			conversation.addParticipant(participantFactory.buildParticipant(userId));
		}
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(conversation, conversationRepository);
		createConversationCommand.execute();
		return conversation.getConversationId();
	}

}
