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

	private List<Conversation> conversationList = new ArrayList<Conversation>();

	public void add(Conversation conversation) {
		conversationList.add(conversation);
	}

	public void update(Conversation conversation) {
		String conversationId = conversation.getConversationId();
		for (Conversation conversation1 : conversationList) {
			if (conversation.getConversationId().equals(conversation1.getConversationId())) {
				conversationList.remove(conversation1);
				conversationList.add(conversation);
			}
		}
	}

	public boolean exists(String conversationId) {
		for (Conversation conversation : conversationList) {
			if (conversation.getConversationId().equals(conversationId)) {
				return true;
			}
		}
		return false;
	}

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

	public long count(ConversationSearchCriteria criteria) {
		return conversationList.size();
	}
}
