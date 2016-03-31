package com.tipi.conversations.restapi;

import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationRepository;

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
		for (Conversation conversation : conversationList) {
			if (conversation.getConversationId().equals(conversationId)) {
				return conversation;
			}
		}
		return null;
	}
}
