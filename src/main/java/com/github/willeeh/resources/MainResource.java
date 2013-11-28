package com.github.willeeh.resources;

import com.github.willeeh.model.Player;
import com.github.willeeh.services.ScoreService;
import com.github.willeeh.services.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Set;

import static com.github.willeeh.util.Parameters.*;

public class MainResource implements HttpHandler
{
	private final ScoreService scoreService;
	private final UserService userService;

	public MainResource(ScoreService scoreService, UserService userService)
	{
		this.scoreService = scoreService;
		this.userService = userService;
	}

	public void handle(HttpExchange httpExchange) throws IOException
	{
		String response = "";
		int httpCode = 200;

		httpExchange.getResponseHeaders().add("Content-Type", "text/plain");

		Map<String, String> params = (Map<String, String>)httpExchange.getAttribute(PARAMETERS);

		try
		{
			if ( LOGIN.equals(params.get(REQUEST_TYPE)) )
			{
				final int userId = Integer.parseInt(params.get(USER_ID));
				response = userService.login(userId);
			}
			else if ( SCORE.equals(params.get(REQUEST_TYPE)) )
			{
				Integer userId = userService.auth(params.get(SESSION_KEY));
				if ( userId != null )
				{
					final int score = Integer.parseInt(params.get(SCORE));
					final int levelId = Integer.parseInt(params.get(LEVEL_ID));
					scoreService.postScore(userId, levelId, score);
				}
				else
				{
					response = "Session key not found or expired";
					httpCode = 401;
				}
			}
			else if ( HIGHSCORE_LIST.equals(params.get(REQUEST_TYPE)) )
			{
				final int levelId = Integer.parseInt(params.get(LEVEL_ID));
				final Set<Player> leaderboard = scoreService.highscores(levelId);
				response = leaderboard.toString().replace("[", "").replace("]", "");
				httpExchange.getResponseHeaders().add("Content-Type", "text/csv");
			}
			else
			{
				response = "NOT FOUND";
				httpCode = 404;
			}

		}
		catch (IllegalArgumentException iaex)
		{
			response = "Precondition failed";
			httpCode = 412;
		}
		catch (Exception ex)
		{
			response = "Something wrong happened";
			httpCode = 500;
		}

		httpExchange.sendResponseHeaders(httpCode, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

}
