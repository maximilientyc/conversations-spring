package com.tipi.conversations.restadapter;

/**
 * Created by @maximilientyc on 02/04/2016.
 */
public class UpdateConversationForm {

	private final Iterable<String> userIds;

	public UpdateConversationForm(Iterable<String> userIds) {
		this.userIds = userIds;
	}

	public Iterable<String> getUserIds() {
		return userIds;
	}

}
