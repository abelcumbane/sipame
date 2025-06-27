package com.example.sipame.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sipame.entitys.Mensalidade;
import com.example.sipame.repositorys.MensalidadeRepository;

@Service
public class MensalidadeServiceImpl implements MensalidadeService {

    private final MensalidadeRepository mensalidadeRepository;

    public MensalidadeServiceImpl(MensalidadeRepository mensalidadeRepository) {
        this.mensalidadeRepository = mensalidadeRepository;
    }

    @Override
    public void save(Mensalidade mensalidade) {
        mensalidadeRepository.save(mensalidade);
    }

    @Override
    public void update(Mensalidade mensalidade) {
        if (mensalidadeRepository.existsById(mensalidade.getId())) {
            mensalidadeRepository.save(mensalidade);
        } else {
            throw new RuntimeException("Mensalidade não encontrada");
        }
    }

    @Override
    public void remove(Mensalidade mensalidade) {
        mensalidadeRepository.delete(mensalidade);
    }

    @Override
    public List<Mensalidade> findAll() {
        return mensalidadeRepository.findAll();
    }

    @Override
    public List<Mensalidade> find(String substring) {
        return mensalidadeRepository.findByAluno_NomeContainingIgnoreCase(substring);
    }

    @Override
    public Mensalidade findById(Integer mensalidadeId) {
        return mensalidadeRepository.findById(mensalidadeId.longValue()).orElse(null);
    }

    @Override
    public long countMensalidade() {
        return mensalidadeRepository.count();
    }

    @Override
    public Mensalidade create(Mensalidade mensalidade) {
        return mensalidadeRepository.save(mensalidade);
    }

    @Override
    public void delete(Integer id) {
        mensalidadeRepository.deleteById(id.longValue());
    }
}
