package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.*;
import com.google.gson.*;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
		@ContextConfiguration(classes = SampleConfiguration.class)
})
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
		String userIdListAsJson = getAsJson(userIdList);

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		ResultActions resultActions = mockMvc
				.perform(post("/conversations")
						.content(userIdListAsJson)
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
		String userIdListAsJson = getAsJson(userIdList);

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		ResultActions resultActions = mockMvc
				.perform(post("/conversations")
						.content(userIdListAsJson)
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
		String userIdListAsJson = getAsJson(userIdList);

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		MvcResult mvcResult = mockMvc
				.perform(post("/conversations")
						.content(userIdListAsJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		// then
		ResultActions resultActions = mockMvc
				.perform(get(location)
						.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void should_return_new_conversation_on_get() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");
		String userIdListAsJson = getAsJson(userIdList);

		// when
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
		MvcResult postMvcResult = mockMvc
				.perform(post("/conversations")
						.content(userIdListAsJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		// then
		String location = postMvcResult.getResponse().getHeader("Location");
		Conversation conversation = getConversationHttp(location);

		assertThat(conversation.countParticipants()).isEqualTo(2);
		assertThat(conversation.containsParticipant("max")).isTrue();
		assertThat(conversation.containsParticipant("bob")).isTrue();
	}

	@Test
	public void should_contain_alice_when_add_to_max_and_bob_conversation() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");
		String conversationAsJson = getAsJson(userIdList);

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();

		MvcResult postMvcResult = mockMvc
				.perform(post("/conversations")
						.content(conversationAsJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String location = postMvcResult.getResponse().getHeader("Location");
		Conversation conversation = getConversationHttp(location);

		// when
		userIdList.add("alice");
		conversationAsJson = getAsJson(conversation.getConversationId(), userIdList);
		MvcResult putMvcResult = mockMvc
				.perform(put("/conversations")
						.content(conversationAsJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		// then
		Conversation conversationUpdated = conversationRepository.get(conversation.getConversationId());
		assertThat(conversationUpdated.countParticipants()).isEqualTo(3);
		assertThat(conversationUpdated.containsParticipant("max")).isTrue();
		assertThat(conversationUpdated.containsParticipant("bob")).isTrue();
		assertThat(conversationUpdated.containsParticipant("alice")).isTrue();
	}

	private Conversation getConversationHttp(String location) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();

		MvcResult getMvcResult = mockMvc
				.perform(get(location)
						.accept(MediaType.APPLICATION_JSON)).andReturn();
		String conversationAsString = getMvcResult.getResponse().getContentAsString();
		Gson gson = gsonBuilder().create();
		Conversation conversation = gson.fromJson(conversationAsString, Conversation.class);
		return conversation;
	}

	private GsonBuilder gsonBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
				return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
			}
		});
		return gsonBuilder;
	}

	private String getAsJson(List<String> userIdList) {
		ConversationJson conversationJson = new ConversationJson(userIdList);
		String userIdListAsJson = gsonBuilder().create().toJson(conversationJson);
		return userIdListAsJson;
	}

	private String getAsJson(String conversationId, List<String> userIdList) {
		ConversationJson conversationJson = new ConversationJson(conversationId, userIdList);
		String userIdListAsJson = gsonBuilder().create().toJson(conversationJson);
		return userIdListAsJson;
	}

	private class ConversationJson {

		private String conversationId;
		private List<String> userIds;

		public ConversationJson(List<String> userIdList) {
			this.userIds = userIdList;
		}

		public ConversationJson(String conversationId, List<String> userIdList) {
			this.conversationId = conversationId;
			this.userIds = userIdList;
		}

		public String getConversationId() {
			return conversationId;
		}

		public List<String> getUserIds() {
			return userIds;
		}
	}
}
