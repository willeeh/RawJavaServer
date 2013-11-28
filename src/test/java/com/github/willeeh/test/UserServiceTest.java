package com.github.willeeh.test;

import com.github.willeeh.services.UserService;
import com.github.willeeh.services.UserServiceImpl;
import com.github.willeeh.session.SessionKey;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

public class UserServiceTest extends Assert
{
	private UserService userService;

	@BeforeClass
	public void setup()
	{
		userService = new UserServiceImpl();
	}

	@AfterMethod
	public void tearDown() {
		setup();
	}

	@Test
	public void testLogin()
	{
		String sessionKey = userService.login(1);
		assertEquals(userService.auth(sessionKey), new Integer(1));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testWrongLogin()
	{
		userService.login(-1);
	}

	@Test(dependsOnMethods = {"testLogin"})
	public void testAuth()
	{
		String sessionKey = userService.login(1);
		assertEquals(userService.auth(sessionKey), new Integer(1));
		assertNull(userService.auth("fake"));
	}

	@Test(enabled = false, description = "Make sure SessionKey.MAX_VALID_TIME is a reasonable time to test this feature")
	public void testExpiration() throws Exception
	{
		List<String> sessionKeys = new LinkedList<String>();
		for (int i = 0; i < 1000; i++)
		{
			sessionKeys.add(userService.login(i));
		}

		for (int i = 0; i < 1000; i++)
		{
			assertEquals(userService.auth(sessionKeys.get(i)), new Integer(i));
		}

		Thread.sleep(SessionKey.MAX_VALID_TIME + 1000);

		for (String key : sessionKeys)
		{
			assertNull(userService.auth(key));
		}
	}
}
