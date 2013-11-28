package com.github.willeeh.services;

import com.github.willeeh.model.Player;

import java.util.Set;

public interface ScoreService
{
	void postScore(int userId, int levelId, int score);

	Set<Player> highscores(int levelId);
}
