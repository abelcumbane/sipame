package com.example.sipame.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.sipame.entitys.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento,Long> {
	
	//JPQL to find all the Produtos that includes the substring the user defined.
	@Query("select p from Pagamento p where lower(p.estado)  like lower(concat('%', :substring, '%'))")
	List<Pagamento> findPagamentos(String substring);

}
