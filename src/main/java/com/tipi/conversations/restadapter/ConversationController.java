package com.tipi.conversations.restadapter;

import com.github.maximilientyc.conversations.commands.CreateConversationCommand;
import com.github.maximilientyc.conversations.commands.UpdateConversationCommand;
import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.ConversationRepository;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;

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
		return new CreateConversationCommand(
				createConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository
		).execute().getConversationId();
	}

	public void putConversation(UpdateConversationForm updateConversationForm) {
		new UpdateConversationCommand(
				updateConversationForm.getConversationId(), updateConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository
		).execute();
	}
}
