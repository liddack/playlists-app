package com.liddack.playlistsapp.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class OptionsDB {
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	private PreparedStatement pstm;

	/**
	 * Este construtor, além de criar um objeto {@link OptionsDB},
	 * cria a app_options <b><code>playlist</code></b> e 
	 * <b><code>playlist_track</code></b> no banco de dados 
	 * caso elas não existam.
	 */
	public OptionsDB() {
		try {
			con = ConnectionFactory.getConnection();
			/* Verifica se a tabela 'app_options' existe */
			String sql = "SHOW TABLES LIKE 'app_options';";
			stm = con.createStatement();
			rs = stm.executeQuery(sql);
			if (!rs.next()) {
				// Se não existir, a tabela é criada junto com suas opções
				sql = "CREATE TABLE app_options ("
						+ "option_name varchar(30), "
						+ "option_value VARCHAR( 500 ),"
						+ "primary key(option_name)); ";
				stm.addBatch(sql);
				sql = "INSERT INTO app_options (option_name, option_value) "
						+ "VALUES ('authcode', ''); ";
				stm.addBatch(sql);
				sql = "INSERT INTO app_options (option_name, option_value) "
						+ "VALUES ('access_token', ''); ";
				stm.addBatch(sql);
				sql = "INSERT INTO app_options (option_name, option_value) "
						+ "VALUES ('refresh_token', '');";
				stm.addBatch(sql);
				stm.executeBatch();
			}
		} catch (SQLException e) {
			System.out.println("\nErro ao criar tabela app_options: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, stm, con);
		}
	}

	/**
	 * Retorna o código de autenticação do Spotify armazenado no BD.
	 * 
	 * @return uma {@link String} contendo o código de autenticação
	 */
	public String getAuthCode() {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM app_options WHERE option_name = 'authcode';";
			stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			String code = null;
			while (rs.next()) {
				code = rs.getString("option_value");
			}
			return code;
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir playlist: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return null;

	}
	
	/**
	 * Armazena um novo código de autenticação do Spotify no BD.
	 * 
	 * @param code - o código a ser armazenado
	 */
	public void setAuthCode(String code) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "UPDATE app_options SET option_value = ? WHERE option_name = 'authcode';";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, code);
			pstm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir código de auth: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
	}
	
	/**
	 * Retorna o token de acesso do Spotify armazenado no BD.
	 * 
	 * @return uma {@link String} contendo o token de acesso
	 */
	public String getAccessToken() {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM app_options WHERE option_name = 'access_token';";
			stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			String accessToken = null;
			while (rs.next()) {
				accessToken = rs.getString("option_value");
			}
			return accessToken;
		} catch (SQLException e) {
			System.out.println("\nErro ao pegar token de acesso: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return null;

	}
	
	/**
	 * Armazena um novo token de acesso do Spotify no BD.
	 * 
	 * @param accessToken - o token de acesso a ser armazenado
	 */
	public void setAccessToken(String accessToken) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "UPDATE app_options SET option_value = ? WHERE option_name = 'access_token';";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, accessToken);
			pstm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir token de acesso: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
	}
	
	/**
	 * Retorna o token de renovação do Spotify armazenado no BD.
	 * 
	 * @return uma {@link String} contendo o token de autenticação
	 */
	public String getRefreshToken() {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM app_options WHERE option_name = 'refresh_token';";
			stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			String refreshToken = null;
			while (rs.next()) {
				refreshToken = rs.getString("option_value");
			}
			return refreshToken;
		} catch (SQLException e) {
			System.out.println("\nErro ao pegar token de atualização: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return null;

	}
	
	/**
	 * Armazena um novo token de renovação do Spotify no BD.
	 * 
	 * @param refreshToken - o token de renovação a ser armazenado
	 */
	public void setRefreshToken(String refreshToken) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "UPDATE app_options SET option_value = ? WHERE option_name = 'refresh_token';";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, refreshToken);
			pstm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir token de atualização: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
	}
	
	/**
	 * Fecha todas as conexões que precisam ser fechadas.
	 * @param rs
	 * 			uma instância aberta de {@link ResultSet}
	 * @param pstm
	 * 			uma instância aberta de {@link PreparedStatement}
	 * @param stm
	 * 			uma instância aberta de {@link Statement}
	 * @param con
	 * 			uma instância aberta de {@link Connection}
	 */
	public void fecha(ResultSet rs, PreparedStatement pstm, Statement stm,
			Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e){

			}
		}
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e){

			}
		}
		if (stm != null) {
			try {
				stm.close();
			} catch (SQLException e){

			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e){

			}
		}
	}

}
