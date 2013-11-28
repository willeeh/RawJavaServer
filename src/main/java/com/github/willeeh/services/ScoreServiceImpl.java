package com.github.willeeh.services;

import com.github.willeeh.model.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ScoreServiceImpl implements ScoreService
{
	private final int MAX_ITEMS = 15;

	private ConcurrentMap<Integer, HashMap<Integer, Player>> currentScorePlayers;
	private ConcurrentMap<Integer, TreeSet<Player>> leaderboards;

	public ScoreServiceImpl()
	{
		currentScorePlayers = new ConcurrentHashMap<Integer, HashMap<Integer, Player>>();
		leaderboards = new ConcurrentHashMap<Integer, TreeSet<Player>>();
	}

	public synchronized void postScore(int userId, int levelId, int score)
	{
		if (userId < 0 || levelId < 0 || score < 0) throw new IllegalArgumentException();

		TreeSet<Player> leaderboard = leaderboards.get(levelId);

		if (leaderboard == null)
		{
			leaderboard = new TreeSet<Player>();
		}

		HashMap<Integer, Player> players = currentScorePlayers.get(levelId);

		if (players == null)
		{
			players = new HashMap<Integer, Player>();
		}

		if ( saveNewHighScore(leaderboard, players, new Player(userId, score)) )
		{
			leaderboards.put(levelId, leaderboard);
			currentScorePlayers.put(levelId, players);
		}
	}

	public Set<Player> highscores(int levelId)
	{
		if (levelId < 0) throw new IllegalArgumentException();

		final TreeSet<Player> leaderboard = leaderboards.get(levelId);
		final Iterator<Player> iterator = leaderboard.iterator();

		Set<Player> result = new LinkedHashSet<Player>();

		int i = 0;
		while (iterator.hasNext() && i < MAX_ITEMS)
		{
			result.add(iterator.next());
			i++;
		}

		return result;
	}

	private boolean saveNewHighScore(TreeSet<Player> leaderboard, HashMap<Integer, Player> players, Player player)
	{
		boolean dataChaged = true;

		Player playersAlreadyStored = players.get(player.getUserId());

		if (playersAlreadyStored != null && playersAlreadyStored.getScore() < player.getScore())
		{
			players.put(player.getUserId(), player);
			leaderboard.remove(playersAlreadyStored);
			leaderboard.add(player);
		}
		else if (playersAlreadyStored == null)
		{
			players.put(player.getUserId(), player);
			leaderboard.add(player);
		}
		else
		{
			dataChaged = false;
		}

		return dataChaged;
	}

}
