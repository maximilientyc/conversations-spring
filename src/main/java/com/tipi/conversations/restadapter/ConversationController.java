package com.tipi.conversations.restadapter;

import com.tipi.conversations.commands.CreateConversationCommand;
import com.tipi.conversations.commands.UpdateConversationCommand;
import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationFactory;
import com.tipi.conversations.domain.ConversationRepository;
import com.tipi.conversations.domain.ParticipantFactory;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
public class ConversationController {

	private ParticipantFactory participantFactory;
	private ConversationRepository conversationRepository;
	private ConversationFactory conversationFactory;

	public ConversationController(ParticipantFactory participantFactory, ConversationRepository conversationRepository, ConversationFactory conversationFactory) {
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.conversationFactory = conversationFactory;
	}

	public String postConversation(CreateConversationForm createConversationForm) {
		CreateConversationCommand createConversationCommand = new CreateConversationCommand(createConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository);
		Conversation conversation = createConversationCommand.execute();
		return conversation.getConversationId();
	}

	public void putConversation(UpdateConversationForm updateConversationForm) {
		UpdateConversationCommand updateConversationCommand = new UpdateConversationCommand();
	}
}
