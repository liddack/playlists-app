package com.liddack.playlistsapp.dominio;
import java.net.MalformedURLException;
import java.net.URL;


public class Track {
	// Atributos
	private String id;
	private String name;
	private String artist;
	private String album;
	private int durationMs;
	private URL albumImageUrl;
	private URL previewUrl;
	
	// Construtores
	public Track() {}
	
	public Track(String name) {
		super();
		this.name = name;
	}
	
	public Track(String id, String name, String artist, String album, int durationMs,
			String albumImageUrl, String previewUrl) {
		super();
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.durationMs = durationMs;
		try {
			this.albumImageUrl = new URL(albumImageUrl);
			if (previewUrl.equals("")) this.previewUrl = null;
			else this.previewUrl = new URL(previewUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Prévia da faixa "+ this.name + " não disponível");
		}
	}
	
	
	// Métodos
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public int getDurationMs() {
		return durationMs;
	}
	
	public void setDurationMs(int durationMs) {
		this.durationMs = durationMs;
	}
	
	public URL getAlbumImageUrl() {
		return albumImageUrl;
	}
	
	public void setAlbumImageUrl(URL albumImageUrl) {
		this.albumImageUrl = albumImageUrl;
	}
	
	public void setAlbumImageUrl(String albumImageUrl) {
		try {
			this.albumImageUrl = new URL(albumImageUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public URL getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(URL previewUrl) {
		this.previewUrl = previewUrl;
	}
	
	public void setPreviewUrl(String previewUrl) {
		if (previewUrl.equals(""))
			this.previewUrl = null;
		else {
			try {
				this.previewUrl = new URL(previewUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Retorna uma String em formato HTML estilizado representando a faixa.
	 * Feito para ser usado por classes de UI.
	 * 
	 *  @return uma string no padrão "Nome da Faixa - Artista" 
	 */
	@Override
	public String toString() {
		return ""
			+ "<html>"
				+ "<strong>"
					+ "<font color='#333333'>"+ this.name + "</font>"
				+ "</strong>"
				+ " - "
				+ "<font color='#616161'>" + this.artist + "</font>"
			+ "</html>";
	}
	
	/**
	 * Retorna uma String em forma de texto simples representando a faixa.
	 * 
	 *  @return uma string no padrão "Nome da Faixa - Artista" 
	 */
	public String toSimpleString() {
		return "Faixa ("+this.id + ") [" + this.name + " - " + this.artist + "]";
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Track))
			return false;
		else {
			if (this.id == ((Track)obj).id) {
				return true;
			}
		}
		Track other = (Track) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
