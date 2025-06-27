package com.example.sipame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sipame.entitys.Aluno;
import com.example.sipame.repositorys.AlunoRepository;

@Service
public class AlunoServiceImpl  implements AlunoService{

	@Autowired
	private AlunoRepository alunoRepository;
	
	@Override
	public void save(Aluno aluno) {
		alunoRepository.save(aluno)	;	
	}

	@Override
	public void update(Aluno aluno) {
		if(alunoRepository.existsById(aluno.getId())) {
			alunoRepository.save(aluno);
		}else {
			throw new RuntimeException("Aluno/a nao encontrado/a");
		}
	}

	@Override
	public void remove(Aluno aluno) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Aluno> findAll() {
		return alunoRepository.findAll();
	}

	@Override
	public List<Aluno> find(String substring) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Aluno findById(Integer alunoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countAluno() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Aluno create(Aluno aluno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

}
