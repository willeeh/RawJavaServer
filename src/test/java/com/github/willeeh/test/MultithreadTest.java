package com.github.willeeh.test;

import com.github.willeeh.model.Player;
import com.github.willeeh.services.ScoreService;
import com.github.willeeh.services.ScoreServiceImpl;
import com.github.willeeh.services.UserService;
import com.github.willeeh.services.UserServiceImpl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class MultithreadTest extends Assert
{
	public static final int NTHREDS = 200;
	public static final int NITEMS = 10000;

	private ExecutorService executor;
	private UserService userService;
	private ScoreService scoreService;

	@BeforeClass
	public void setup()
	{
		executor = Executors.newFixedThreadPool(NTHREDS);
		userService = new UserServiceImpl();
		scoreService = new ScoreServiceImpl();
	}

	@AfterMethod
	public void tearDown() {
		setup();
	}

	@Test
	public void mainTest()
	{
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();

		for (int i = 0; i < NITEMS; i++)
		{
			Future<Integer> future = executor.submit( new ScoreTask(scoreService, i*2) );
			futures.add(future);
		}

		//Wait for all futures
		for (Future<Integer> future : futures)
		{
			try
			{
				future.get();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				e.printStackTrace();
			}
		}

		Set<Player> right = new LinkedHashSet<Player>();

		for (int i = NITEMS-1; i >= NITEMS-15 ; i--)
		{
			right.add(new Player(i, (NITEMS-1)*2));
		}

		assertEquals(scoreService.highscores(1), right);
	}

	class ScoreTask implements Callable<Integer>
	{
		private final ScoreService scoreService;

		private final Integer multiplier;

		public ScoreTask(ScoreService scoreService, Integer multiplier)
		{
			this.scoreService= scoreService;
			this.multiplier = multiplier;
		}

		public Integer call() throws Exception
		{
			for (int i = 0; i < NITEMS; i++)
			{
				scoreService.postScore(i, 1, i*multiplier);
			}

			return multiplier;
		}
	}
}
