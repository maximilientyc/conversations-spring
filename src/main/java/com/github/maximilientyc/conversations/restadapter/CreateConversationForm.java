package com.github.maximilientyc.conversations.restadapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
public class CreateConversationForm {

	private final Collection<String> userIds;

	public CreateConversationForm() {
		this.userIds = new ArrayList<>();
	}

	public CreateConversationForm(Collection<String> userIds) {
		this.userIds = userIds;
	}

	public Collection<String> getUserIds() {
		return userIds;
	}

}
