package com.github.maximilientyc.conversations.restadapter;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
public class CreateConversationForm {

	private final Iterable<String> userIds;

	public CreateConversationForm(Iterable<String> userIds) {
		this.userIds = userIds;
	}

	public Iterable<String> getUserIds() {
		return userIds;
	}

}
