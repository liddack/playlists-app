package com.liddack.playlistsapp.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.liddack.playlistsapp.dominio.User;

/**
 * A classe UserDB é responsável por abstrair toda a conexão
 * com o banco de dados em relação a objetos do tipo {@link User}.
 * 
 */
public class UserDB implements GenericDB<User, String> {
	private Connection con;
	private PreparedStatement pstm;
	private Statement stm;
	private ResultSet rs;
	
	/**
	 * Este construtor, além de criar um objeto UserDB,
	 * cria a tabela <b><code>user</code></b> no banco de dados caso ela não exista.
	 */
	public UserDB() throws SQLException {
		/* Cria a tabela User se ela não existir */
		String sql = "CREATE TABLE IF NOT EXISTS user("
				+ "username varchar( 60 ), "
				+ "password VARCHAR( 100 ) not null, "
				+ "PRIMARY KEY ( username ));";
		con = ConnectionFactory.getConnection();
		stm = con.createStatement();
		stm.executeUpdate(sql);
		//finally {
			this.fecha(rs, pstm, stm, con);
		//}
	}
	
	/**
	 * Adiciona um usuário ao banco de dados.
	 * 
	 * @param user
	 * 				O {@link User} a ser persistido
	 */
	@Override
	public void inserir(User user) {
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "insert into user(username, password) "
					+ "values (?, ?);";
			pstm = con.prepareStatement(sql);
			String encodedPassword = new String(
					Base64.getEncoder()
					.encode(user.getPassword()
					.getBytes()));
			pstm.setString(1, user.getUsername());
			pstm.setString(2, encodedPassword);
			
			System.out.print("Inserindo "+ user +"... ");
			pstm.executeUpdate();
			
			System.out.println("Dados inseridos!");
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir usuário:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, stm, con);
		}
		
	}
	
	/**
	 * Altera a senha de um usuário no banco de dados.
	 * 
	 * @param user
	 * 				O {@link User} a ser modificado
	 */
	@Override
	public void modificar(User user) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "UPDATE user "
					+ "SET password = ? "
					+ "WHERE username = ?";
			pstm = con.prepareStatement(sql);
			String encodedPassword = new String(
					Base64.getEncoder()
					.encode(user.getPassword()
					.getBytes()));
			pstm.setString(1, encodedPassword);
			pstm.setString(2, user.getUsername());
			
			System.out.print("Modificando "+ user +"... ");
			pstm.executeUpdate();
			
			System.out.println("Senha alterada!");
		} catch (SQLException e) {
			System.out.println("\nErro ao modificar usuário:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		
	}
	
	/**
	 * Exclui um usuário do banco de dados.
	 * 
	 * @param user
	 * 				O {@link User} a ser excluído
	 */
	@Override
	public void excluir(User user) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "DELETE FROM user "
					+ "WHERE username = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, user.getUsername());
			
			System.out.print("Excluindo "+ user +"... ");
			pstm.executeUpdate();
			
			System.out.println("Usuário excluído!");
		} catch (SQLException e) {
			System.out.println("\nErro ao excluir usuário:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
	}
	
	/**
	 * Retorna todos os usuários registrados no banco de dados.
	 * 
	 * @return uma {@link ArrayList} de {@link User} contendo todos os usuários retornados pela busca
	 */
	@Override
	public List<User> buscarTodos() {
		List<User> list = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select * from user";
			pstm = con.prepareStatement(sql);
			
			System.out.print("Buscando todos os usuários... ");
			rs = pstm.executeQuery();
			list = new ArrayList<User>();
			while (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				password = new String(Base64.getDecoder().decode(password.getBytes()));
				list.add(new User(username, password));
			}

			System.out.println("Busca concluída!");
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar usuários:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return list;
	}
	
	/**
	 * Retorna um usuário registrado no banco de dados pelo seu username.
	 * @param username
	 * 			O nome de usuário do {@link User} a ser recuperado do BD
	 * @return o {@link User} com o username recebido pelo método
	 */
	@Override
	public User buscarPorID(String username) {
		User user = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select * from user where username = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, username);
			
			System.out.print("Buscando usuário... ");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String password = new String(Base64.getDecoder()
									.decode(rs.getString("password")
									.getBytes()));
				user = new User(username, password);
			}
			System.out.println("Busca concluída!");
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar usuário:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return user;
	}
	
	/**
	 * Fecha todas as conexões que precisam ser fechadas.
	 * @param rs
	 * 			{@link ResultSet} aberto
	 * @param pstm
	 * 			{@link PreparedStatement} aberto
	 * @param stm
	 * 			{@link Statement} aberto
	 * @param con
	 * 			{@link Connection} aberta
	 */
	@Override
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
