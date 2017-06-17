package com.liddack.playlistsapp.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;

public interface GenericDB<T, ID> {
	public void inserir(T obj);
	public void modificar(T obj);
	public void excluir(T obj);
	public List<T> buscarTodos();
	public T buscarPorID(ID id);
	
	public void fecha(
			ResultSet rs,
			PreparedStatement pstm,
			Statement stm, 
			Connection con
	);
}
