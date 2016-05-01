package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.commands.CreateConversationCommand;
import com.github.maximilientyc.conversations.commands.UpdateConversationCommand;
import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.ConversationRepository;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
@Controller
public class ConversationController {

	private ParticipantFactory participantFactory;
	private ConversationRepository conversationRepository;
	private ConversationFactory conversationFactory;

	public ConversationController() {
	}

	public ConversationController(ParticipantFactory participantFactory, ConversationRepository conversationRepository, ConversationFactory conversationFactory) {
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.conversationFactory = conversationFactory;
	}

	@RequestMapping(value = "/conversations", method = RequestMethod.POST)
	public ResponseEntity<?> postConversation(@RequestBody CreateConversationForm createConversationForm) {
		String conversationId = new CreateConversationCommand(
				createConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository
		).execute().getConversationId();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(conversationId).toUri());

		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/conversations", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void putConversation(UpdateConversationForm updateConversationForm) {
		new UpdateConversationCommand(
				updateConversationForm.getConversationId(), updateConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository
		).execute();
	}
}
