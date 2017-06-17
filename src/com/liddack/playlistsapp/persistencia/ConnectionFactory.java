package com.liddack.playlistsapp.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionFactory {
	// Ambiente localhost
	private static final String DB_URI = "jdbc:mysql://localhost/playlists-app";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URI, USER, PASSWORD);
	}
}
