package com.example.sipame.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sipame.entitys.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno,Long> {
	//JPQL to find all the Produtos that includes the substring the user defined.
			@Query("select a from Aluno a where lower(a.nome)  like lower(concat('%', :substring, '%'))")
			List<Aluno> findAlunos(String substring);

}
