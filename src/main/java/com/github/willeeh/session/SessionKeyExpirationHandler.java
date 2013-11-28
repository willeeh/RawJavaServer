package com.github.willeeh.session;

import com.github.willeeh.model.Session;

import java.util.Map;

public class SessionKeyExpirationHandler extends Thread
{
	private final static long CLEAN_UP_TIME = 15*60*1000;

	private final Map<String, Session> session;

	public SessionKeyExpirationHandler(Map<String, Session> session)
	{
		this.session = session;
	}

	public void run()
	{
		do
		{
			try
			{
				for (String sessionKey : session.keySet())
				{
					Session currentSession = session.get(sessionKey);
					if ((SessionKey.isExpired(currentSession.getAccessAt())))
					{
						session.remove(sessionKey);
					}
				}

				sleep(CLEAN_UP_TIME);
			}
			catch(Exception e)
			{

			}
		} while (true);
	}
}
