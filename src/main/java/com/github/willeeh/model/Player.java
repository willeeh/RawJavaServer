package com.github.willeeh.model;

public class Player implements Comparable<Player>
{
	private int userId;

	private int score;

	public Player(int userId, int score)
	{
		this.userId = userId;
		this.score = score;
	}

	public int getUserId()
	{
		return userId;
	}

	public int getScore()
	{
		return score;
	}

	@Override
	public String toString()
	{
		return userId + "=" + score;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Player)) return false;

		Player player = (Player) o;

		if (userId != player.userId) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		return userId;
	}

	public int compareTo(Player player)
	{
		int comparison = ((Integer) player.getScore()).compareTo(this.getScore());

		if (comparison == 0) //If both players got same score, it's ordered by user id
		{
			comparison = ((Integer)this.getUserId()).compareTo(player.getUserId());
		}

		return comparison;
	}
}
