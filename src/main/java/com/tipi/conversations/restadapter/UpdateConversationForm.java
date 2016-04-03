package com.tipi.conversations.restadapter;

import java.util.List;

/**
 * Created by @maximilientyc on 02/04/2016.
 */
public class UpdateConversationForm {

	private List<String> userIds;

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIdList) {
		this.userIds = userIds;
	}
}
