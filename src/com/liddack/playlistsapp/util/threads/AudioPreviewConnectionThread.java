package com.liddack.playlistsapp.util.threads;

import java.net.URL;

import com.liddack.playlistsapp.ui.MainUI;
import com.liddack.playlistsapp.util.NetworkUtils;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class AudioPreviewConnectionThread implements Runnable {
		URL previewUrl = null;
		AdvancedPlayer player = null;
		
		public AudioPreviewConnectionThread(URL previewUrl) {
			// TODO Auto-generated constructor stub
			this.previewUrl = previewUrl;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			player = NetworkUtils.getAudioPlayer(previewUrl);
			MainUI.playerReceiver(player);
		}
		
	}
