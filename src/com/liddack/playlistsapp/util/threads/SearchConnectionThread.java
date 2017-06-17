package com.liddack.playlistsapp.util.threads;

import java.util.ArrayList;
import java.util.List;

import com.liddack.playlistsapp.ui.MainUI;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.liddack.playlistsapp.dominio.Track;
import com.liddack.playlistsapp.spotifyAuth.SpotifyAuthorizationFlow;

public class SearchConnectionThread implements Runnable {
	private String searchQuery;
	private SpotifyAuthorizationFlow spotifyApi;
	
	public SearchConnectionThread(String query) {
		this.searchQuery = query;
		spotifyApi = MainUI.getSpotifyApi();
	}

	@Override
	public void run() {
		this.search();
	}
	
	private void search() {
		Api api = spotifyApi.getApi();
		TrackSearchRequest request = api.searchTracks(searchQuery).build();
		ArrayList<Track> tracksList = new ArrayList<Track>();
		try {
		   final Page<com.wrapper.spotify.models.Track> trackSearchResult = request.get();
		   List<com.wrapper.spotify.models.Track> tracks = trackSearchResult.getItems();
		   for (com.wrapper.spotify.models.Track track : tracks) {
			   Track appTrack = new Track(
					   track.getId(),
					   track.getName(),
					   track.getArtists().get(0).getName(),
					   track.getAlbum().getName(),
					   track.getDuration(),
					   track.getAlbum().getImages().get(0).getUrl(),
					   track.getPreviewUrl()
			   );
			   tracksList.add(appTrack);
		   }
		   MainUI.searchResultsReceiver(tracksList);
		} catch (Exception e) {
		    System.out.println("Token de acesso inválido ou expirou! Gerando um novo...");
		    if (spotifyApi.refreshAccessToken()) this.search();
		}
	}
	
}