package com.github.maximilientyc.conversations.restadapter;

import com.github.maximilientyc.conversations.domain.UserService;

/**
 * Created by @maximilientyc on 09/05/2016.
 */
public class SampleUserService implements UserService {

	@Override
	public String getLoggedInUserId() {
		return "max";
	}
}
