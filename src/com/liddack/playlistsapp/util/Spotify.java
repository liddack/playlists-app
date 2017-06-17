package com.liddack.playlistsapp.util;

public class Spotify {
    
    /**
     * Converte a quantidade de tempo em milissegundos para seu
     * equivalente em minutos e segundos no formato mm:ss em forma
     * de String.
     *
     * @param milissec
     *            Um inteiro contendo o tempo em milissegundos.
     * @return Uma String contendo o valor convertido formatado em mm:ss.
     */
    public static String msToMinSec (int milissec) {
    	int sec = milissec / 1000;
    	int min = sec/60;
    	sec = sec % 60;
    	return min+":"+String.format("%1$02d", sec);
    }
    
    /**
     * Converte a quantidade de tempo em milissegundos para seu
     * equivalente em minutos e segundos no formato mm:ss em forma
     * de String.
     *
     * @param milissec
     *            Um double contendo o tempo em milissegundos.
     * @return Uma String contendo o valor convertido formatado em mm:ss.
     */
    public static String msToMinSec (double milissec) {
    	int sec = (int) milissec / 1000;
    	int min = sec/60;
    	sec = sec % 60;
    	return min+":"+String.format("%1$02d", sec);
    }
}