package com.example.sipame.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sipame.entitys.Mensalidade;
import com.example.sipame.entitys.Pagamento;
import com.example.sipame.repositorys.PagamentoRepository;

@Service
public class PagamentoServiceImpl implements PagamentoService {

	private final PagamentoRepository pagamentoRepository;

	// ✅ Construtor necessário para injeção de dependência
	public PagamentoServiceImpl(PagamentoRepository pagamentoRepository) {
		this.pagamentoRepository = pagamentoRepository;
	}

	@Override
	public void save(Pagamento pagamento) {
		pagamentoRepository.save(pagamento);	
	}

	@Override
	public void update(Pagamento pagamento) {
		if (pagamentoRepository.existsById(pagamento.getId())) {
			pagamentoRepository.save(pagamento);
		} else {
			throw new RuntimeException("Pagamento não encontrado");
		}
	}

	@Override
	public void remove(Pagamento pagamento) {
		// Exemplo de implementação:
		if (pagamentoRepository.existsById(pagamento.getId())) {
			pagamentoRepository.delete(pagamento);
		}
	}

	@Override
	public List<Pagamento> findAll() {
		return pagamentoRepository.findAll();
	}

	@Override
	public List<Pagamento> find(String substring) {
		// Opcional: buscar por referência ou nome do aluno
		return null;
	}

	@Override
	public Pagamento findById(Integer pagamentoId) {
		return pagamentoRepository.findById(Long.valueOf(pagamentoId)).orElse(null);
	}

	@Override
	public long countPagamento() {
		return pagamentoRepository.count();
	}

	@Override
	public Pagamento create(Pagamento pagamento) {
		return pagamentoRepository.save(pagamento);
	}

	@Override
	public void delete(Integer id) {
		pagamentoRepository.deleteById(Long.valueOf(id));
	}
}
