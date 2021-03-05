package com.practice.hacker_rank;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RestAPI_1 {

	public static void main(String[] args) throws IOException {
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

//		String team = bufferedReader.readLine();

//		int year = Integer.parseInt(bufferedReader.readLine().trim());

		int result = Result1.getTotalGoals("Barcelona", 2011);
		System.out.println(result);
//		bufferedWriter.write(String.valueOf(result));
//		bufferedWriter.newLine();

//		bufferedReader.close();
//		bufferedWriter.close();

	}

}

class Result1 {

	/*
	 * Complete the 'getTotalGoals' function below.
	 *
	 * The function is expected to return an INTEGER. The function accepts following
	 * parameters: 1. STRING team 2. INTEGER year
	 */

	public static int getTotalGoals(String team, int year) throws IOException {

		String url = "https://jsonmock.hackerrank.com/api/football_matches";
		int home = getGoalCount(String.format(url + "?team1=%s&year=%d", URLEncoder.encode(team, "UTF-8"), year),
				"team1", 1);
		int away = getGoalCount(String.format(url + "?team2=%s&year=%d", URLEncoder.encode(team, "UTF-8"), year),
				"team2", 1);
		return home + away;

	}

	static int getGoalCount(String req, String team, int page) throws IOException {
		URL url = new URL(req + "&page=" + page);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("Content-Type", "application/json");
		int status = urlConnection.getResponseCode();

		InputStream in = (status < 200 || status > 209) ? urlConnection.getErrorStream()
				: urlConnection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String resLine;
		StringBuffer resContent = new StringBuffer();

		while ((resLine = br.readLine()) != null)
			resContent.append(resLine);

		br.close();
		urlConnection.disconnect();

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		String script = "var obj=JSON.parse('" + resContent.toString() + "');";
		script += "var total_pages = obj.total_pages;";
		script += "var total_goals = obj.data.reduce(function(accumulator,current){return accumulator+parseInt(current."
				+ team + "goals);},0);";
		try {
			engine.eval(script);
		} catch (ScriptException se) {
			se.printStackTrace();
		}
		int totalPages = (int) engine.get("total_pages");
		int goals = (int) Double.parseDouble(engine.get("total_goals").toString());
		return (page < totalPages) ? getGoalCount(req, team, page + 1) : goals;
	}

}
