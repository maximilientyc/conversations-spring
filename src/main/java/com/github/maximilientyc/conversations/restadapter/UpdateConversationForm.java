package com.github.maximilientyc.conversations.restadapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by @maximilientyc on 02/04/2016.
 */
public class UpdateConversationForm {

	private final String conversationId;
	private final Collection<String> userIds;

	public UpdateConversationForm() {
		this.conversationId = null;
		this.userIds = new ArrayList<>();
	}

	public UpdateConversationForm(String conversationId, Collection<String> userIds) {
		this.conversationId = conversationId;
		this.userIds = userIds;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Collection<String> getUserIds() {
		return userIds;
	}

}
