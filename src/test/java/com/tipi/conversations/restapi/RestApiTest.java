package com.tipi.conversations.restapi;

import com.tipi.conversations.domain.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by @maximilientyc on 26/03/2016.
 */
public class RestApiTest {

	@Rule
	public final ExpectedException expectedException;
	private final ConversationController conversationController;
	private final ConversationService conversationService;
	private final ConversationFactory conversationFactory;
	private final MessageFactory messageFactory;
	private final ParticipantFactory participantFactory;
	private final UserRepository userRepository;
	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;

	public RestApiTest() {
		conversationRepository = new SampleConversationRepository();
		messageRepository = Mockito.mock(SampleMessageRepository.class);
		conversationService = new ConversationService(conversationRepository, messageRepository);
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		userRepository = new SampleUserRepository();
		participantFactory = new ParticipantFactory(userRepository);
		expectedException = ExpectedException.none();

		conversationController = new ConversationController(participantFactory, conversationRepository, conversationFactory);
	}

	@Test
	public void should_create_a_new_conversation() {
		// given
		CreateConversationForm createConversationForm = new CreateConversationForm();
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");
		createConversationForm.setUserIds(userIdList);

		// when
		String conversationId = conversationController.postConversation(createConversationForm);

		// then
		boolean conversationExists = conversationRepository.exists(conversationId);
		assertThat(conversationExists).isTrue();
	}

}
