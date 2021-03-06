package com.github.maximilientyc.conversations.restadapter;


import com.github.maximilientyc.conversations.domain.Message;
import com.github.maximilientyc.conversations.domain.MessageRepository;
import com.github.maximilientyc.conversations.domain.MessageSearchCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @maximilientyc on 20/03/2016.
 */
public class SampleMessageRepository implements MessageRepository {

	private List<Message> messageList = new ArrayList<Message>();

	public void add(Message message) {
		messageList.add(message);
	}

	public boolean exists(Message message) {
		String messageId = message.getMessageId();
		for (Message message1 : messageList) {
			if (messageId.equals(message1.getMessageId())) {
				return true;
			}
		}
		return false;
	}

	public Message get(String messageId) {
		for (Message message1 : messageList) {
			if (messageId.equals(message1.getMessageId())) {
				return message1;
			}
		}
		return null;
	}

	public long count(MessageSearchCriteria criteria) {
		return messageList.size();
	}
}
