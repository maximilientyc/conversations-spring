package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.*;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SampleApplication.class)
public class RestAdapterTest {

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

	public RestAdapterTest() {
		conversationRepository = new SampleConversationRepository();
		messageRepository = new SampleMessageRepository();
		conversationService = new ConversationService(conversationRepository, messageRepository);
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		userRepository = new SampleUserRepository();
		participantFactory = new ParticipantFactory(userRepository);
		expectedException = ExpectedException.none();

		conversationController = new ConversationController(participantFactory, conversationRepository, conversationFactory);
	}

	@Test
	public void should_return_201_created_when_creating_a_new_conversation() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		ResultActions resultActions = mockMvc
				.perform(post("/conversations")
						.content(getSampleUserIdsAsJson())
						.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isCreated());
	}

	@Test
	public void should_contain_location_in_header_when_creating_a_new_conversation() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		ResultActions resultActions = mockMvc
				.perform(post("/conversations")
						.content(getSampleUserIdsAsJson())
						.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(header().string("Location", Matchers.containsString("/conversations/")));
	}

	@Test
	public void should_return_json_when_retrieving_newly_created_conversation() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		MvcResult mvcResult = mockMvc
				.perform(post("/conversations")
						.content(getSampleUserIdsAsJson())
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		// then
		ResultActions resultActions = mockMvc
				.perform(get(location)
						.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	private String getSampleUserIdsAsJson() {
		return "{" +
				"  \"userIds\" : [\"max\", \"bob\"]" +
				"} ";
	}
}
