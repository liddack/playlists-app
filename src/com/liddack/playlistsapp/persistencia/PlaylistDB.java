package com.liddack.playlistsapp.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.liddack.playlistsapp.dominio.Playlist;
import com.liddack.playlistsapp.dominio.Track;
import com.liddack.playlistsapp.dominio.User;

/**
 * A classe playlistDB é responsável por abstrair toda a conexão
 * com o banco de dados em relação a objetos do tipo {@link Playlist}.
 * Ela abrange duas tabelas: <b><code>playlist</code></b> e <b><code>playlist_track</code></b>.
 */
public class PlaylistDB implements GenericDB<Playlist, Integer> {
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	private PreparedStatement pstm;
	
	/**
	 * Este construtor, além de criar um objeto {@link PlaylistDB},
	 * cria as tabelas <b><code>playlist</code></b> e 
	 * <b><code>playlist_track</code></b> no banco de dados 
	 * caso elas não existam.
	 */
	public PlaylistDB() {
		try {
			con = ConnectionFactory.getConnection();
			/* Cria a tabela 'playlist' se ela não existir */
			String sql = "CREATE TABLE IF NOT EXISTS playlist ("
					+ "id INTEGER AUTO_INCREMENT, "
					+ "username VARCHAR( 60 ), "
					+ "name VARCHAR( 60 ), "
					+ "creation_date TIMESTAMP, "
					+ "PRIMARY KEY ( id ),"
					+ "FOREIGN KEY ( username ) REFERENCES user ( username ));";
			stm = con.createStatement();
			stm.executeUpdate(sql);
			
			/* Cria a tabela 'playlist_track' se ela não existir */
			sql = "CREATE TABLE IF NOT EXISTS playlist_track ("
					+ "playlist_id INTEGER, "
					+ "track_id VARCHAR( 30 ), "
					+ "track_index INTEGER, "
					+ "FOREIGN KEY( playlist_id ) REFERENCES playlist( id ),"
					+ "FOREIGN KEY( track_id ) REFERENCES track( id ));";
			stm = con.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("\nErro ao criar conexão/tabelas Playlist/Playlist_Track: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, stm, con);
		}
	}
	
	/**
	 * Adiciona uma playlist ao banco de dados.
	 * 
	 * @param playlist
	 * 				A {@link Playlist} a ser persistida
	 */
	@Override
	public void inserir(Playlist playlist) {
		// inserindo dados na tabela Playlist
		try {
			con = ConnectionFactory.getConnection();
			String sql = "INSERT INTO playlist(id, username, name, creation_date)"
					+ "VALUES(?, ?, ?, ?);";
			playlist.setId(generateId(6));
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, playlist.getId());
			pstm.setString(2, playlist.getUsername());
			pstm.setString(3,  playlist.getName());
			pstm.setTimestamp(4, new Timestamp(playlist.getCreationCalendar().getTimeInMillis()));
			
			System.out.print("Inserindo " 
						+ "Playlist [Nome " + playlist.getName() + ", Usuário = " + playlist.getUsername() + "]... ");
			pstm.executeUpdate();
			
			
			
			// Inserindo dados na tabela Playlist_Track
			sql = "INSERT INTO playlist_track(playlist_id, track_id, track_index)"
					+ "VALUES(?, ?, ?);";
			pstm = con.prepareStatement(sql);
			ArrayList<Track> tracks = playlist.getTracks();
			Iterator<Track> itr = tracks.iterator();
			System.out.print("Inserindo faixas da playlist... ");
			while (itr.hasNext()) {
				Track atual = itr.next();
				pstm.setInt(1, playlist.getId());
				pstm.setString(2, atual.getId());
				pstm.setInt(3, playlist.indexOf(atual));
				System.out.println(pstm.toString());
				System.out.println("INSERT INTO playlist_track(playlist_id, track_id, track_index)"
						+ "VALUES("+playlist.getId() +", '"+atual.getId()+"', "+playlist.indexOf(atual)+");");
				pstm.executeUpdate();
			}
			
			System.out.println("Dados inseridos!");
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir playlist: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		
	}

	/**
	 * Modifica uma playlist no banco de dados.
	 * 
	 * @param playlist
	 * 				A {@link Playlist} a ser modificada
	 */
	@Override
	public void modificar(Playlist playlist) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "DELETE FROM playlist_track WHERE playlist_id = " + playlist.getId();
			stm = con.createStatement();
			System.out.println("Excluindo faixas atuais da playlist...");
			stm.execute(sql);
			
			sql = "UPDATE playlist SET "
					+ "username = ?, "
					+ "name = ?, "
					+ "creation_date = ? "
					+ "WHERE id = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, playlist.getUsername());
			pstm.setString(2,  playlist.getName());
			pstm.setTimestamp(3, new Timestamp(playlist.getCreationCalendar().getTimeInMillis()));
			pstm.setInt(4, playlist.getId());
			
			System.out.print("Modificando " 
						+ "Playlist " + playlist.getName() + "... ");
			pstm.executeUpdate();
			
			sql = "INSERT INTO playlist_track(playlist_id, track_id, track_index)"
					+ "VALUES(?, ?, ?);";
			pstm = con.prepareStatement(sql);
			ArrayList<Track> tracks = playlist.getTracks();
			Iterator<Track> itr = tracks.iterator();
			while (itr.hasNext()) {
				Track atual = itr.next();
				pstm.setInt(1, playlist.getId());
				pstm.setString(2, atual.getId());
				pstm.setInt(3, playlist.indexOf(atual));
				pstm.executeUpdate();
			}
			System.out.println("Dados inseridos!");
		} catch (SQLException e) {
			System.out.println("Erro ao modificar playlist: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
	}

	/**
	 * Exclui uma playlist do banco de dados.
	 * 
	 * @param playlist
	 * 				A {@link Playlist} a ser excluída
	 */
	@Override
	public void excluir(Playlist playlist) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "DELETE FROM playlist_track "
					+ "WHERE playlist_id = " + playlist.getId();
			stm = con.createStatement();
			System.out.print("Excluindo músicas da playlist " + playlist.getName() + "... ");
			stm.execute(sql);
			sql = "DELETE FROM playlist "
					+ "WHERE id = " + playlist.getId();
			System.out.print("Excluindo playlist " + playlist.getName() + "... ");
			stm.execute(sql);
			System.out.println("Playlist excluída!");
		} catch (SQLException e) {
			System.out.println("Erro ao excluir playlist: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
	}

	/**
	 * Retorna todas as playlists registradas no banco de dados.
	 * 
	 * @return uma {@link List} de {@link Playlist} contendo todas as playlists retornadas pela busca
	 */
	@Override
	public List<Playlist> buscarTodos() {
		ArrayList<Playlist> playlists = null;
		Playlist playlist = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM playlist";
			stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			System.out.print("Buscando playlists... ");
			while (rs.next()) {
				playlist = new Playlist();
				playlist.setId(rs.getInt("id"));
				playlist.setUsername(rs.getString("username"));
				playlist.setName(rs.getString("name"));
				playlist.setCreationCalendar(rs.getTimestamp("creation_date"));
				if (playlists == null) playlists = new ArrayList<Playlist>();
				playlists.add(playlist);
			}
			if (playlists!= null) {
				Iterator<Playlist> itr = playlists.iterator();
				int i = 0;
				while (itr.hasNext()) {
					playlist = itr.next();
					sql = "SELECT * FROM playlist_track"
							+ " WHERE playlist_id = " + playlist.getId()
							+ " ORDER BY track_index ASC";
					rs = stm.executeQuery(sql); 
					TrackDB trackDB = new TrackDB();
					while (rs.next()) {
						playlist.addTrack(trackDB.buscarPorID(
								rs.getString("track_id")));
					}
					playlists.set(i++, playlist);
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar playlists: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return playlists;
	}
	
	/**
	 * Retorna todas as playlists de um certo usuário registradas no banco de dados.
	 * 
	 * @param user - O {@link User} dono das playlists a serem recuperadas
	 * 
	 * @return uma {@link List} de {@link Playlist} contendo todas as playlists retornadas pela busca
	 */
	public List<Playlist> buscarTodosDe(User user) {
		ArrayList<Playlist> playlists = null;
		Playlist playlist = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM playlist where username = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, user.getUsername());
			ResultSet rs = pstm.executeQuery();
			System.out.print("Buscando playlists... ");
			while (rs.next()) {
				playlist = new Playlist();
				playlist.setId(rs.getInt("id"));
				playlist.setUsername(rs.getString("username"));
				playlist.setName(rs.getString("name"));
				playlist.setCreationCalendar(rs.getTimestamp("creation_date"));
				// TODO remover depois
				System.out.println(playlist.getCreationCalendar().getTimeInMillis());
				if (playlists == null) playlists = new ArrayList<Playlist>();
				playlists.add(playlist);
			}
			
			if (playlists!= null) {
				Iterator<Playlist> itr = playlists.iterator();
				int i = 0;
				while (itr.hasNext()) {
					playlist = itr.next();
					sql = "SELECT * FROM playlist_track"
							+ " WHERE playlist_id = " + playlist.getId()
							+ " ORDER BY track_index ASC";
					rs = pstm.executeQuery(sql); 
					TrackDB trackDB = new TrackDB();
					while (rs.next()) {
						playlist.addTrack(trackDB.buscarPorID(
								rs.getString("track_id")));
					}
					playlists.set(i++, playlist);
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar playlists: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return playlists;
	}

	/**
	 * Retorna uma playlist registrada no banco de dados pela sua ID.
	 * @param id
	 * 			A ID ({@link Integer}) da {@link Playlist} a ser recuperada do BD
	 * @return a {@link Playlist} identificada pela ID
	 */
	@Override
	public Playlist buscarPorID(Integer id) {
		Playlist playlist = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT FROM playlist WHERE id = " + id;
			stm = con.createStatement();
			rs = stm.executeQuery(sql);
			System.out.print("Buscando playlist ... ");
			while (rs.next()) {
				playlist = new Playlist();
				playlist.setId(id);
				playlist.setUsername(rs.getString("username"));
				playlist.setName(rs.getString("name"));
				playlist.setCreationCalendar(rs.getTimestamp("creation_date"));
				
				sql = "SELECT * FROM playlist_track"
						+ " WHERE playlist_id = " + playlist.getId()
						+ " ORDER BY track_index ASC";
				rs = stm.executeQuery(sql); 
				TrackDB trackDB = new TrackDB();
				while (rs.next()) {
					playlist.addTrack(trackDB.buscarPorID(
							rs.getString("track_id")));
				}
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar playlist: ");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return playlist;
	}
	
	public boolean jaExiste(int id) {
		boolean existe = false;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM playlist WHERE id = ?";
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, id);
			System.out.print("Verificando se a playlist existe... ");
			rs = pstm.executeQuery();
			if (rs.next()) existe = true;
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar playlist:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return existe;
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
	
	private int generateId(int length) {
		int max = (int) Math.pow(10, length);
		return ThreadLocalRandom.current().nextInt(0, max - 1);
	}

	

}
