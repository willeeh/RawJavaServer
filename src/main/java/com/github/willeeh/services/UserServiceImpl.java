package com.github.willeeh.services;

import com.github.willeeh.model.Session;
import com.github.willeeh.session.SessionKey;
import com.github.willeeh.session.SessionKeyExpirationHandler;


import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class UserServiceImpl implements UserService
{
	private final Map<String, Session> session;

	public UserServiceImpl()
	{
		session = Collections.synchronizedMap(new WeakHashMap<String, Session>());

		SessionKeyExpirationHandler expirationHandler = new SessionKeyExpirationHandler(session);
		expirationHandler.start();
	}

	public String login(int userId)
	{
		if (userId < 0) throw new IllegalArgumentException();
		final String sessionKey = SessionKey.genRandom32Hex();
		session.put(sessionKey, new Session(userId, SessionKey.now()));
		return sessionKey;
	}

	public Integer auth(String sessionKey)
	{
		Session currentSession = session.get(sessionKey);

		if (currentSession == null)
		{
			return null;
		}
		else if (SessionKey.isExpired(currentSession.getAccessAt()))
		{
			session.remove(currentSession);
			return null;
		}

		return currentSession.getUserId();
	}

}
