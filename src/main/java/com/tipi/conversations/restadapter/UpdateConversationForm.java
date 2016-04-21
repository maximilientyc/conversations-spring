package com.tipi.conversations.restadapter;

/**
 * Created by @maximilientyc on 02/04/2016.
 */
public class UpdateConversationForm {

	private final String conversationId;
	private final Iterable<String> userIds;

	public UpdateConversationForm(String conversationId, Iterable<String> userIds) {
		this.conversationId = conversationId;
		this.userIds = userIds;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Iterable<String> getUserIds() {
		return userIds;
	}

}
