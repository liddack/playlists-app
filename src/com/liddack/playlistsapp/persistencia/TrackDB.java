package com.liddack.playlistsapp.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.liddack.playlistsapp.dominio.Track;

/**
 * A classe TrackDB é responsável por abstrair toda a conexão
 * com o banco de dados em relação a objetos do tipo {@link Track}.
 * 
 */
public class TrackDB implements GenericDB<Track, String>{
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	private PreparedStatement pstm;
	
	/**
	 * Este construtor, além de criar um objeto {@link TrackDB},
	 * cria a tabela <b><code>track</code></b> no banco de dados 
	 * caso ela não exista.
	 */
	public TrackDB() {
		try {
			con = ConnectionFactory.getConnection();
			/* Cria a tabela Track se ela não existir */
			String sql = "CREATE TABLE IF NOT EXISTS track ("
					+ "id VARCHAR( 30 ), "
					+ "name VARCHAR( 150 ), "
					+ "artist VARCHAR( 100 ), "
					+ "album VARCHAR( 100 ), "
					+ "duration_ms INTEGER, "
					+ "album_image_url VARCHAR( 200 ), "
					+ "preview_url VARCHAR( 200 ), "
					+ "PRIMARY KEY ( id ));";
			stm = con.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("\nErro ao criar conexão/tabela Tracks");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, stm, con);
		}
	}

	
	/**
	 * Adiciona uma faixa ao banco de dados.
	 * 
	 * @param track
	 * 				A {@link Track} a ser persistida
	 */
	@Override
	public void inserir(Track track) {
		try {
			con = ConnectionFactory.getConnection();
			
			String sql = "INSERT INTO track"
					+ "(id, name, artist, album, duration_ms, album_image_url, preview_url) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
					
			pstm = con.prepareStatement(sql);
			pstm.setString(1, track.getId());
			pstm.setString(2, track.getName());
			pstm.setString(3, track.getArtist());
			pstm.setString(4, track.getAlbum());
			pstm.setInt(5, track.getDurationMs());
			pstm.setString(6, track.getAlbumImageUrl().toString());
			if (track.getPreviewUrl() == null) pstm.setString(7, "");
			else pstm.setString(7, track.getPreviewUrl().toString());
			
			
			System.out.print("Inserindo faixa "+ track.toSimpleString() +"... ");
			pstm.executeUpdate();
			
			System.out.println("Dados inseridos!");
		} catch (SQLException e) {
			System.out.println("\nErro ao inserir faixa:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, stm, con);
		}
		
	}
	
	/**
	 * Modifica uma faixa no banco de dados.
	 * 
	 * @param track
	 * 				A {@link Track} a ser modificada
	 */
	@Override
	public void modificar(Track track) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "UPDATE track SET "
					+ "name = ?, "
					+ "artist = ?, "
					+ "album = ?, "
					+ "duration_ms = ?, "
					+ "album_image_url = ?, "
					+ "preview_url = ? "
					+ "WHERE id = ?";
			
			pstm = con.prepareStatement(sql);
			pstm.setString(1, track.getName());
			pstm.setString(2, track.getArtist());
			pstm.setString(3, track.getAlbum());
			pstm.setInt(4, track.getDurationMs());
			pstm.setString(5, track.getAlbumImageUrl().toString());
			if (track.getPreviewUrl() == null) pstm.setString(6, "");
			else pstm.setString(6, track.getPreviewUrl().toString());
			pstm.setString(7, track.getId());
			
			System.out.print("Modificando faixa "+ track.toSimpleString() +"... ");
			pstm.executeUpdate();
			
			System.out.println("Dados inseridos!");
		} catch (SQLException e) {
			System.out.println("\nErro ao modificar faixa:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, stm, con);
		}
		
	}
	
	/**
	 * Exclui uma faixa do banco de dados.
	 * 
	 * @param track
	 * 				A {@link Track} a ser excluída
	 */
	@Override
	public void excluir(Track track) {
		try {
			con = ConnectionFactory.getConnection();
			String sql = "DELETE FROM track "
					+ "WHERE id = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, track.getId());
			
			System.out.print("Excluindo faixa "+ track +"... ");
			pstm.executeUpdate();
			
			System.out.println("Faixa excluída!");
		} catch (SQLException e) {
			System.out.println("\nErro ao excluir faixa:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		
	}
	
	/**
	 * Retorna todas as faixas registradas no banco de dados.
	 * 
	 * @return uma {@link List} de {@link Track} contendo todas as faixas retornadas pela busca
	 */
	@Override
	public List<Track> buscarTodos() {
		List<Track> list = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select * from track";
			pstm = con.prepareStatement(sql);
			
			System.out.print("Buscando todas as faixas... ");
			rs = pstm.executeQuery();
			list = new ArrayList<Track>();
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String artist = rs.getString("artist");
				String album = rs.getString("album");
				int durationMs = rs.getInt("duration_ms");
				String albumImageUrl = rs.getString("album_image_url");
				String previewUrl = rs.getString("preview_url");
				list.add(new Track(id, name, artist, album, durationMs, albumImageUrl, previewUrl));
			}

			System.out.println("Busca concluída!");
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar faixas:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return list;
	}
	
	/**
	 * Retorna uma faixa registrada no banco de dados pela sua ID.
	 * @param id
	 * 			A ID da {@link Track} a ser recuperada do BD
	 * @return a {@link Track} identificada pela ID
	 */
	@Override
	public Track buscarPorID(String id) {
		Track track = null;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM track WHERE id = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, id);
			
			System.out.print("Buscando faixa... ");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String artist = rs.getString("artist");
				String album = rs.getString("album");
				int durationMs = rs.getInt("duration_ms");
				String albumImageUrl = rs.getString("album_image_url");
				String previewUrl = rs.getString("preview_url");
				track = new Track(id, name, artist, album, durationMs, albumImageUrl, previewUrl);
			}

			System.out.println("Busca concluída!");
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar faixa:");
			e.printStackTrace();
		} finally {
			this.fecha(rs, pstm, pstm, con);
		}
		return track;
	}
	
	/**
	 * Verifica se a faixa identificada pelo <code>id</code> recebido
	 * está registrada no banco de dados.
	 * @param id
	 * 			A ID da {@link Track} a ser verificada
	 * @return <code>true</code> se a faixa está registrada no banco
	 * de dados, e <code>false</code> caso contrário.
	 */
	public boolean jaExiste(String id) {
		boolean existe = false;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT * FROM track WHERE id = ?";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, id);
			System.out.print("Verificando se a faixa existe... ");
			rs = pstm.executeQuery();
			if (rs.next()) existe = true;
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar faixa:");
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

}
