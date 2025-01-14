package com.globaltech.api.domain.repositories;

import com.globaltech.api.domain.models.PessoaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaModel, Integer> {
    boolean existsByCpf(String cpf);
}