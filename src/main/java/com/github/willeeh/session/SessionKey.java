package com.github.willeeh.session;

import java.security.SecureRandom;

public class SessionKey
{
	private static volatile SecureRandom numberGenerator = null;
	private static final long MSB = 0x8000000000000000L;

	public final static long MAX_VALID_TIME = 10*60*1000;

	public static String genRandom32Hex()
	{
		SecureRandom ng = numberGenerator;

		if (ng == null)
		{
			numberGenerator = ng = new SecureRandom();
		}

		return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
	}

	public static boolean isExpired(long yourDate)
	{
		return now() - MAX_VALID_TIME > yourDate;
	}

	public static long now()
	{
		return System.currentTimeMillis();
	}
}
