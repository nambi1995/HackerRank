package com.practice.hacker_rank;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RestAPI_2 {

	public static void main(String[] args) throws IOException {
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

//		int year = Integer.parseInt(bufferedReader.readLine().trim());

		int result = Result2.getNumDraws(2011);
System.out.println(result);
//		bufferedWriter.write(String.valueOf(result));
//		bufferedWriter.newLine();

//		bufferedReader.close();
//		bufferedWriter.close();
	}

}

class Result2 {

	/*
	 * Complete the 'getNumDraws' function below.
	 *
	 * The function is expected to return an INTEGER. The function accepts INTEGER
	 * year as parameter.
	 */

	public static int getNumDraws(int year) throws IOException {
		int totNumDraws = 0;
		final String endPoint = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year;
		final int maxScore = 10;
		for (int score = 0; score <= maxScore; score++) {
			totNumDraws += getTotalDraws(String.format(endPoint + "&team1goals=%d&team2goals=%d", score, score));
		}
		return totNumDraws;
	}

	static int getTotalDraws(String req) throws IOException {
		URL url = new URL(req);
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
		script += "var total = obj.total;";
		try {
			engine.eval(script);
		} catch (ScriptException se) {
			se.printStackTrace();
		}
		return (int) engine.get("total");
	}

}
