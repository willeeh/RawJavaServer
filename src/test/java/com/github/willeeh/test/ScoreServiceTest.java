package com.github.willeeh.test;

import com.github.willeeh.model.Player;
import com.github.willeeh.services.ScoreService;
import com.github.willeeh.services.ScoreServiceImpl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedHashSet;
import java.util.Set;

public class ScoreServiceTest extends Assert
{
	private ScoreService scoreService;

	@BeforeClass
	public void setup()
	{
		scoreService = new ScoreServiceImpl();
	}

	@AfterMethod
	public void tearDown() {
		setup();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void postScoreTestWrongUser()
	{
		scoreService.postScore(-1, 1, 50);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void postScoreTestWrongLevel()
	{
		scoreService.postScore(1, -1, 100);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void postScoreTestWrongScore()
	{
		scoreService.postScore(1, 2, -1);
	}

	@Test
	public void postScoreTestSameUserDifferentLevels()
	{
		scoreService.postScore(1,3,20);
		scoreService.postScore(1,2,50);
		scoreService.postScore(1,1,100);
		Set<Player> right = new LinkedHashSet<Player>();
		right.add(new Player(1,100));
		assertEquals(scoreService.highscores(1), right);
	}

	@Test
	public void leaderboardTest()
	{
		scoreService.postScore(1,1,50);
		scoreService.postScore(2,1,100);
		scoreService.postScore(3,1,20);
		scoreService.postScore(4,1,200);
		scoreService.postScore(5,1,80);
		scoreService.postScore(6,1,35);
		scoreService.postScore(7,1,35);

		Set<Player> right = new LinkedHashSet<Player>();
		right.add(new Player(4,200));
		right.add(new Player(2,100));
		right.add(new Player(5,80));
		right.add(new Player(1,50));
		right.add(new Player(6,35));
		right.add(new Player(7,35));
		right.add(new Player(3,20));

		assertEquals(scoreService.highscores(1), right);
	}

	@Test
	public void leaderboardUpdateScoresTest()
	{
		scoreService.postScore(1,1,50);
		scoreService.postScore(1,1,100);
		scoreService.postScore(1,1,20);
		scoreService.postScore(2,1,200);
		scoreService.postScore(2,1,80);
		scoreService.postScore(2,1,35);
		scoreService.postScore(3,1,35);
		scoreService.postScore(3,1,40);
		scoreService.postScore(3,1,20);

		Set<Player> right = new LinkedHashSet<Player>();
		right.add(new Player(2,200));
		right.add(new Player(1,100));
		right.add(new Player(3,40));

		assertEquals(scoreService.highscores(1), right);
	}

	@Test
	public void leaderboard1000Scores()
	{
		for (int i = 0; i < 1000; i++)
		{
			scoreService.postScore(i, 1, i*2);
		}

		Set<Player> right = new LinkedHashSet<Player>();

		for (int i = 999; i >= 999-14 ; i--)
		{
			right.add(new Player(i, i*2));
		}

		assertEquals(scoreService.highscores(1), right);
	}
}
