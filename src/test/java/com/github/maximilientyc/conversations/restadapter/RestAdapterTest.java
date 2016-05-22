package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.MessageFactory;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.MessageRepository;
import com.github.maximilientyc.conversations.domain.repositories.UserRepository;
import com.github.maximilientyc.conversations.domain.services.ConversationService;
import com.github.maximilientyc.conversations.domain.services.UserService;
import com.github.maximilientyc.conversations.infrastructure.searches.PaginatedList;
import com.google.gson.*;
import org.hamcrest.Matchers;
import org.junit.Before;
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
	public ExpectedException expectedException;
	private MockMvc mockMvc;

	private ConversationController conversationController;
	private ConversationService conversationService;
	private ConversationFactory conversationFactory;
	private MessageFactory messageFactory;
	private ParticipantFactory participantFactory;
	private UserRepository userRepository;
	private UserService userService;
	private ConversationRepository conversationRepository;
	private MessageRepository messageRepository;

	public RestAdapterTest() {
		expectedException = ExpectedException.none();
	}

	@Before
	public void initComponents() {
		conversationRepository = new SampleConversationRepository();
		messageRepository = new SampleMessageRepository();
		conversationService = new ConversationService(conversationRepository, messageRepository);
		conversationFactory = new ConversationFactory(conversationService);
		messageFactory = new MessageFactory(conversationService);
		userRepository = new SampleUserRepository();
		userService = new SampleUserService();
		participantFactory = new ParticipantFactory(userRepository);

		conversationController = new ConversationController(participantFactory, conversationRepository, conversationFactory, userService);
		mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
	}

	@Test
	public void should_return_201_created_when_creating_a_new_conversation() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");
		String userIdListAsJson = getAsJson(userIdList);

		// when
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

		// when
		MvcResult mvcResult = postConversation(userIdList);
		String location = mvcResult.getResponse().getHeader("Location");
		ResultActions resultActions = mockMvc
				.perform(get(location)
						.accept(MediaType.APPLICATION_JSON));

		// then
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
		MvcResult postMvcResult = postConversation(userIdList);
		String location = postMvcResult.getResponse().getHeader("Location");
		Conversation conversation = getConversationHttp(location);

		// when
		userIdList.add("alice");
		String conversationAsJson = getAsJson(conversation.getConversationId(), userIdList);
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

	@Test
	public void should_return_all_conversations_created_by_max() throws Exception {
		// given
		List<String> userIdList = new ArrayList<String>();
		userIdList.add("max");
		userIdList.add("bob");

		// when
		postConversation(userIdList);
		postConversation(userIdList);
		ResultActions resultActions = mockMvc
				.perform(get("/conversations/search")
						.param("userId", "max")
						.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk());

		MvcResult mvcResult = resultActions.andReturn();
		Gson gson = gsonBuilder().create();
		PaginatedList<Conversation> conversationList = gson.fromJson(mvcResult.getResponse().getContentAsString(), PaginatedList.class);
		assertThat(conversationList.getTotalRowCount()).isEqualTo(2);
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

	private MvcResult postConversation(List<String> userIdList) throws Exception {
		String conversationAsJson = getAsJson(userIdList);

		MvcResult postMvcResult = mockMvc
				.perform(post("/conversations")
						.content(conversationAsJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		return postMvcResult;
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
