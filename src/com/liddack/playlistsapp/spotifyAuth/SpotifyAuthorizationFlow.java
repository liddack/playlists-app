package com.liddack.playlistsapp.spotifyAuth;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.liddack.playlistsapp.persistencia.OptionsDB;
import com.liddack.playlistsapp.ui.MainUI;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.authentication.RefreshAccessTokenRequest;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.RefreshAccessTokenCredentials;

public class SpotifyAuthorizationFlow {
	private final static String clientId = "575bdac18e78489d9a705049f483ad23";
	private final static String clientSecret = "58443a82c46a4fedb5012bfeccca98d3";
	private final static String redirectURI = "http://localhost:8080/spotifyauth.php";
	private String accessToken = "";
	private String refreshToken = "";
	private String CODE = "";
	private static Api api;
	private OptionsDB odb;
	
	public SpotifyAuthorizationFlow() {
		api = Api.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.redirectURI(redirectURI)
				.build();
		
		odb = new OptionsDB();
		accessToken = odb.getAccessToken();
		refreshToken = odb.getRefreshToken();
		if (accessToken != null && refreshToken != null) {
			useTokens(accessToken, refreshToken);;
		} else {
			accessToken = "";
			refreshToken = "";
		};
	}
	
	public void authorizeFlow() {
		/* Set the necessary scopes that the application will need from the user */
		final List<String> scopes = Arrays.asList("user-read-private", "user-read-email");

		/* Set a state. This is used to prevent cross site request forgeries. */
		final String state = "commLuckuba";

		String authorizeURL = api.createAuthorizeURL(scopes, state);

		/* Continue by sending the user to the authorizeURL, which will look something like
				   https://accounts.spotify.com:443/authorize?client_id=5fe01282e44241328a84e7c5cc169165&response_type=code&redirect_uri=https://example.com/callback&scope=user-read-private%20user-read-email&state=some-state-of-my-choice
		 */
		
		try {
			openWebpage(new URL(authorizeURL));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		CODE = MainUI.requestAuthorizationCode();
		generateTokens(CODE);
	}
	
	private void generateTokens(final String code) {
		/* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
		 * are made with the .get method. This holds for all type of requests. */
		final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
		  @Override
		  public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
		    /* The tokens were retrieved successfully! */
		    System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
		    System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
		    System.out.println("Luckily, I can refresh it using this refresh token! " +     authorizationCodeCredentials.getRefreshToken());
		  
		    /* Set the access token and refresh token so that they are used whenever needed */
		    api.setAccessToken(authorizationCodeCredentials.getAccessToken());
		    api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
		    odb.setAuthCode(code);
		    odb.setAccessToken(authorizationCodeCredentials.getAccessToken());
		    odb.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
		    accessToken = authorizationCodeCredentials.getAccessToken();
		    refreshToken = authorizationCodeCredentials.getRefreshToken();
		    MainUI.showMessage("Agora você pode pesquisar faixas!");
		  }

		  @Override
		  public void onFailure(Throwable throwable) {
		    /* Let's say that the client id is invalid, or the code has been used more than once,
		     * the request will fail. Why it fails is written in the throwable's message. */
			  System.out.println("Não foi possível receber os tokens do Spotify:");
			  throwable.printStackTrace();
		  }
		});
	}
	
	private void useTokens(String accessToken, String refreshToken) {
		api.setAccessToken(accessToken);
	    api.setRefreshToken(refreshToken);
	    System.out.println("Token de acesso existente: " + accessToken);
	    System.out.println("Token de atualização existente: " +  refreshToken);
	}
	
	public Api getApi() {
		return api;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public boolean refreshAccessToken() {
		RefreshAccessTokenRequest request = api.refreshAccessToken().build();
		try {
			RefreshAccessTokenCredentials credentials = request.get();
			String accessToken = credentials.getAccessToken();
			api.setAccessToken(accessToken);
			odb.setAccessToken(accessToken);
			System.out.println("Token de acesso atualizado!");
		} catch (IOException | WebApiException e) {
			MainUI.showMessage("Falha ao atualizar a chave de acesso de pesquisa de músicas.\nVerifique sua conexão.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean isAuth() {
		return !accessToken.equals("") && !refreshToken.equals("");
	}
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
}
