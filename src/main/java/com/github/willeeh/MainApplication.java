package com.github.willeeh;

import com.github.willeeh.filters.ParameterFilter;
import com.github.willeeh.resources.MainResource;
import com.github.willeeh.services.ScoreServiceImpl;
import com.github.willeeh.services.UserServiceImpl;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication
{
	public static void main(String[] args) throws Exception
	{
		HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
		HttpContext serverContext = server.createContext("/",
				new MainResource(new ScoreServiceImpl(), new UserServiceImpl()));
		serverContext.getFilters().add(new ParameterFilter());
		ExecutorService executor = Executors.newCachedThreadPool();
		server.setExecutor(executor);
		server.start();
	}

}
