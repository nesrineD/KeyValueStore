package de.tub.aec.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.PropertyConfigurator;

import edu.kit.aifb.dbe.hermes.SimpleFileLogger;

public class TestServer {
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			String line = br.readLine();
			String[] split = line.split(" ");
			
			@SuppressWarnings("unused")
			Server server = new Server(split[0],split[1], Integer.parseInt(split[2]), 2, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}