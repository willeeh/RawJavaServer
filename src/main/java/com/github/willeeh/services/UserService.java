package com.github.willeeh.services;

public interface UserService
{
	String login(int userId);

	Integer auth(String sessionKey);
}
