package com.liddack.playlistsapp.util.threads;

import java.awt.Image;
import java.net.URL;

import com.liddack.playlistsapp.ui.MainUI;
import com.liddack.playlistsapp.util.NetworkUtils;


public class ImageConnectionThread implements Runnable {
	URL imageUrl = null;
	
	public ImageConnectionThread(URL url) {
		this.imageUrl = url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Image image = NetworkUtils.getImage(imageUrl);
		MainUI.albumImageReceiver(image);
	}
}
