package com.tipi.conversations.restapi;

import java.util.List;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
public class CreateConversationForm {

	private List<String> userIds;

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
}
