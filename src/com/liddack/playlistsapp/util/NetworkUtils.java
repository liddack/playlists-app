package com.liddack.playlistsapp.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;


public class NetworkUtils {
	/**
     * Realiza a conexão com o URL fornecido, retornando os dados obtidos do mesmo.
     *
     * @param url
     *            O URL do qual se deseja obter resultados
     * @return Uma String contendo a resposta do servidor
     * @throws IOException
     * 				caso haja um erro na conexão.
     */
	public static String getResponseFromHttpUrl(URL url) throws IOException {
		System.out.println("Abrindo URL '"+url+"'...");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (urlConnection.getResponseCode() == 404) return null;
        Scanner scanner = null;
        try {
            InputStream in = urlConnection.getInputStream();

            scanner = new Scanner(in, "UTF-8");
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
        	return null;
        }
        finally {
        	scanner.close();
            urlConnection.disconnect();
        }
    }
	
	/**
     * Retorna um objeto Image contendo a imagem localizada no URL passado.
     *
     * @param imageUrl
     *            O URL da imagem a ser obtida
     * @return Um objeto Imagecontendo a imagem em questão
     */
	public static Image getImage(final URL imageUrl) {
		Image image = null;
		try {
		    image = ImageIO.read(imageUrl);
		} catch (IOException e) {
		}
		return image;
	}
	
	/**
     * Gera um novo objeto AdvancedPlayer a partir do URL passado.
     *
     * @param audioPreviewUrl
     *            Um URL que aponta para um arquivo de áudio compatível
     * @return Um objeto AdvancedPlayer contendo o áudio do arquivo
     */
	public static AdvancedPlayer getAudioPlayer(URL audioPreviewUrl) {
		AdvancedPlayer player = null;
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) audioPreviewUrl.openConnection();
			InputStream in = urlConnection.getInputStream();
			player = new AdvancedPlayer(in);
		} catch (IOException | JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return player;
	}
}
