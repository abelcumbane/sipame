package com.example.sipame.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.sipame.entitys.Mensalidade;

public interface MensalidadeRepository  extends JpaRepository<Mensalidade,Long>{
	//JPQL to find all the Produtos that includes the substring the user defined.
	//@Query("select m from Mensalidade m where lower(m.valor)  like lower(concat('%', :substring, '%'))")
	//List<Mensalidade> findMensalidades(String substring);
    List<Mensalidade> findByAluno_NomeContainingIgnoreCase(String nome);


}
