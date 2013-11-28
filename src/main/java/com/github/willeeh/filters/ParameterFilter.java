package com.github.willeeh.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.willeeh.util.Parameters.*;

public class ParameterFilter extends Filter
{
	public static final String POST = "POST";
	public static final String UTF_8 = "UTF-8";

	@Override
	public String description() {
		return "This parses request parameters";
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parseGetParameters(exchange, parameters);
		parsePostParameters(exchange, parameters);
		exchange.setAttribute(PARAMETERS, parameters);
		chain.doFilter(exchange);
	}

	private void parseGetParameters(HttpExchange exchange, Map<String, Object> parameters) throws UnsupportedEncodingException
	{
		URI requestedUri = exchange.getRequestURI();
		String query = requestedUri.getRawQuery();
		parseQuery(query, parameters);
		String path = requestedUri.getRawPath();
		parsePath(path, parameters);
	}

	private void parsePostParameters(HttpExchange exchange, Map<String, Object> parameters) throws IOException
	{
		if (POST.equalsIgnoreCase(exchange.getRequestMethod()))
		{
			InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), UTF_8);
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
			if (query != null)
			{
				parameters.put(SCORE, query);
			}
		}
	}

	private void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException
	{
		if (query != null)
		{
			String pairs[] = query.split("[&]");

			for (String pair : pairs)
			{
				String param[] = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 0)
				{
					key = URLDecoder.decode(param[0], UTF_8);
				}

				if (param.length > 1)
				{
					value = URLDecoder.decode(param[1], UTF_8);
				}

				if (parameters.containsKey(key))
				{
					Object obj = parameters.get(key);
					if(obj instanceof List<?>)
					{
						List<String> values = (List<String>)obj;
						values.add(value);
					}
					else if(obj instanceof String)
					{
						List<String> values = new ArrayList<String>();
						values.add((String)obj);
						values.add(value);
						parameters.put(key, values);
					}
				}
				else
				{
					parameters.put(key, value);
				}
			}
		}
	}

	private void parsePath(String path, Map<String, Object> parameters)
	{
		if (path != null)
		{
			final String[] pathParameters = path.split("/");
			if (pathParameters.length == 3)
			{
				if(pathParameters[2].equals(SCORE) || pathParameters[2].equals(HIGHSCORE_LIST))
				{
					parameters.put(LEVEL_ID, pathParameters[1]);
					parameters.put(REQUEST_TYPE,pathParameters[2]);
				}
				else if(pathParameters[2].equals(LOGIN))
				{
					parameters.put(USER_ID, pathParameters[1]);
					parameters.put(REQUEST_TYPE, pathParameters[2]);
				}

			}
		}
	}
}
