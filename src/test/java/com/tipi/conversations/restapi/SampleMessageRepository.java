package com.tipi.conversations.restapi;

import com.tipi.conversations.domain.Message;
import com.tipi.conversations.domain.MessageRepository;
import com.tipi.conversations.domain.MessageSearchCriteria;

/**
 * Created by @maximilientyc on 20/03/2016.
 */
public class SampleMessageRepository implements MessageRepository {

	public void add(Message message) {

	}

	public boolean exists(Message message) {
		return false;
	}

	public Message get(String messageId) {
		return null;
	}

	public long count(MessageSearchCriteria criteria) {
		return 0;
	}
}
