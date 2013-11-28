package com.github.willeeh.model;

public class Session
{
	int userId;
	long accessAt;

	public Session(int userId, long accessAt)
	{
		this.userId = userId;
		this.accessAt = accessAt;
	}

	public int getUserId()
	{
		return userId;
	}

	public long getAccessAt()
	{
		return accessAt;
	}

}
