package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationRepository;
import com.github.maximilientyc.conversations.domain.ConversationSearchCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class SampleConversationRepository implements ConversationRepository {

	private List<Conversation> conversationList = new ArrayList<>();

	@Override
	public void add(Conversation conversation) {
		conversationList.add(conversation);
	}

	@Override
	public void update(Conversation conversation) {
		String conversationId = conversation.getConversationId();
		for (Conversation conversation1 : conversationList) {
			if (conversation.getConversationId().equals(conversation1.getConversationId())) {
				conversationList.remove(conversation1);
				conversationList.add(conversation);
			}
		}
	}

	@Override
	public boolean exists(String conversationId) {
		for (Conversation conversation : conversationList) {
			if (conversation.getConversationId().equals(conversationId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Conversation get(String conversationId) {
		if (conversationId == null) {
			throw new IllegalArgumentException("Conversation Id cannot be empty.");
		}
		for (Conversation conversation : conversationList) {
			if (conversation.getConversationId().equals(conversationId)) {
				return conversation;
			}
		}
		return null;
	}

	@Override
	public List<Conversation> find(ConversationSearchCriteria conversationSearchCriteria) {
		List<Conversation> foundConversationList = new ArrayList<Conversation>();
		for (Conversation conversation : conversationList) {
			if (conversation.containsParticipant(conversationSearchCriteria.getUserId())) {
				foundConversationList.add(conversation);
			}
		}
		return foundConversationList;
	}

	@Override
	public long count(ConversationSearchCriteria criteria) {
		return conversationList.size();
	}
}
