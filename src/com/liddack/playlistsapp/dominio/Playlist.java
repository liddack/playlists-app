package com.liddack.playlistsapp.dominio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class Playlist {
	private int id;
	private String username;
	private String name;
	private ArrayList<Track> tracks; 
	private Calendar creationDate;
	
	public Playlist() {
		this.id = 0;
		this.username = "";
		this.name = "";
		this.tracks = new ArrayList<Track>();
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		this.creationDate = cal;
	}
	
	/*public Playlist(String name, String username) {
		this.id = 0;
		this.username = username;
		this.name = name;
		this.tracks = new ArrayList<Track>();
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		this.creationDate = cal;
	}*/
	
	public Playlist(String name, User user) {
		this.id = 0;
		this.username = user.getUsername();
		this.name = name;
		this.tracks = new ArrayList<Track>();
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		this.creationDate = cal;
	}
	
	public Playlist(String name, String username, ArrayList<Track> playlist) {
		this.id = 0;
		this.username = username;
		this.name = name;
		this.tracks = playlist;
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		this.creationDate = cal;
	}
	
	public Playlist(String name, String username, Track[] list) {
		this.id = 0;
		this.username = username;
		this.name = name;
		this.tracks = new ArrayList<Track>();
		for (Track track : list) {
			this.tracks.add(track);
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		this.creationDate = cal;
	}
	
	public Playlist(int id, String username, String name, 
			ArrayList<Track> tracks, Calendar creationDate) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.tracks = tracks;
		this.creationDate = creationDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public double getTotalDuration() {
		double totalDuration = 0;
		Iterator<Track> itr = this.tracks.iterator();
		while (itr.hasNext()) {
			Track track = itr.next();
			totalDuration += track.getDurationMs();
		}
		return totalDuration;
	}
	
	public String getCreationDate() {
		SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		return f.format(this.creationDate.getTime());
	}
	
	public String getCreationDateAndTime() {
		SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return f.format(this.creationDate.getTime());
	}
	
	public Calendar getCreationCalendar() {
		return this.creationDate;
	}
	
	public void setCreationCalendar(Timestamp timestamp) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestamp.getTime());
		this.creationDate = cal;
	}
	
	public boolean hasTrack(Track track) {
		return tracks.contains(track);
	}
	
	public int getSize() {
		return this.tracks.size();
	}
	
	public Track getTrackAt(int index) {
		return this.tracks.get(index);
	}
	
	/**
	 * Adiciona uma faixa à playlist.
	 * 
	 * @param track
	 * 				A faixa a ser adicionada
	 * @return true se a faixa for adicionada com sucesso, false se a faixa já estiver presente na playlist
	 */
	public boolean addTrack(Track track) {
		if (this.tracks.contains(track)) {
			return false;
		} 
		this.tracks.add(track);
		return true;
	}
	
	public void removeTrack(Track track) {
		tracks.remove(track);
	}
	
	/**
	 * Adiciona uma faixa à playlist, mesmo se ela for duplicada.
	 * 
	 * @param track
	 * 				A faixa a ser adicionada
	 * @param force
	 * 				true para forçar a adição
	 */
	/*public void addTrack(Track track, boolean force) {
		if (force) {
			this.playlist.add(track);
		}
	}*/

	/**
	 * @return the playlist
	 */
	public ArrayList<Track> getTracks() {
		return this.tracks;
	}
	
	public int indexOf(Track track) {
		return this.tracks.indexOf(track);
	}
	
	public void moveTrackUp(Track track) {
		int index = this.tracks.indexOf(track);
		if (index > 0) {
			this.tracks.remove(track);
			this.tracks.add( (index - 1), track);
		}
	}
	
	public void moveTrackDown(Track track) {
		int index = this.tracks.indexOf(track);
		if (index < (this.tracks.size() - 1)) {
			this.tracks.remove(track);
			this.tracks.add( (index + 1), track);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
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
		if (getClass() != obj.getClass())
			return false;
		Playlist other = (Playlist) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (this.hashCode() != obj.hashCode())
			return false;
		return true;
	}
}
