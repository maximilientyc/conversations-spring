package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.commands.CreateConversationCommand;
import com.github.maximilientyc.conversations.commands.UpdateConversationCommand;
import com.github.maximilientyc.conversations.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
@Controller
public class ConversationController {

	private ParticipantFactory participantFactory;
	private ConversationRepository conversationRepository;
	private ConversationFactory conversationFactory;
	private UserService userService;

	public ConversationController() {
	}

	public ConversationController(ParticipantFactory participantFactory, ConversationRepository conversationRepository, ConversationFactory conversationFactory, UserService userService) {
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.conversationFactory = conversationFactory;
		this.userService = userService;
	}

	@RequestMapping(value = "/conversations", method = RequestMethod.POST)
	public ResponseEntity<?> postConversation(@RequestBody CreateConversationForm createConversationForm) {
		String conversationId = new CreateConversationCommand(
				createConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository, userService
		).execute().getConversationId();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(conversationId).toUri());

		return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/conversations/{conversationId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Conversation> getConversation(@PathVariable String conversationId) {
		Conversation conversation = conversationRepository.get(conversationId);
		return new ResponseEntity<>(conversation, HttpStatus.OK);
	}

	@RequestMapping(value = "/conversations", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void putConversation(@RequestBody UpdateConversationForm updateConversationForm) {
		new UpdateConversationCommand(
				updateConversationForm.getConversationId(), updateConversationForm.getUserIds(), conversationFactory, participantFactory, conversationRepository, userService
		).execute();
	}
}
