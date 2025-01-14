package com.globaltech.api.services;

import com.globaltech.api.controller.dtos.PessoaDto;
import com.globaltech.api.domain.models.PessoaModel;
import org.springframework.data.jpa.repository.Query;

public interface PessoaService extends CrudService<PessoaModel, Integer, PessoaDto>{
}
