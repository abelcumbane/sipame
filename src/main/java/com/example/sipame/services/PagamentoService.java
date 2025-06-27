package com.example.sipame.services;

import java.util.List;

import com.example.sipame.entitys.Pagamento;

public interface PagamentoService {
	
	public void save(Pagamento pagamento);
	public void update(Pagamento pagamento);
	public void remove(Pagamento pagamento);
	public List<Pagamento> findAll();
	public List<Pagamento> find(String substring);
	public Pagamento findById(Integer pagamentoId);
	public long countPagamento();
	public Pagamento create(Pagamento pagamento);
	public void delete(Integer id);

}
