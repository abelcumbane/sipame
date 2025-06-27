package com.example.sipame.services;

import java.util.List;

import com.example.sipame.entitys.Mensalidade;


public interface MensalidadeService {
	
	public void save(Mensalidade mensalidade);
	public void update(Mensalidade mensalidade);
	public void remove(Mensalidade mensalidade);
	public List<Mensalidade> findAll();
	public List<Mensalidade> find(String substring);
	public Mensalidade findById(Integer mensalidadeId);
	public long countMensalidade();
	public Mensalidade create(Mensalidade mensalidade);
	public void delete(Integer id);

}
