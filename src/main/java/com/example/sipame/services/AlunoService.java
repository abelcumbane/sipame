package com.example.sipame.services;

import java.util.List;

import com.example.sipame.entitys.Aluno;


public interface AlunoService {
	
	public void save(Aluno aluno);
	public void update(Aluno aluno);
	public void remove(Aluno aluno);
	public List<Aluno> findAll();
	public List<Aluno> find(String substring);
	public Aluno findById(Integer alunoId);
	public long countAluno();
	public Aluno create(Aluno aluno);
	public void delete(Integer id);

}
