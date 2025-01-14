package com.globaltech.api.services.impl;

import com.globaltech.api.controller.dtos.PessoaDto;
import com.globaltech.api.domain.models.PessoaModel;
import com.globaltech.api.domain.repositories.PessoaRepository;
import com.globaltech.api.services.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaServiceImpl implements PessoaService {

    @Autowired
    private final PessoaRepository repository;


    public PessoaServiceImpl(PessoaRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<PessoaModel> findAll() {
        return repository.findAll();
    }


    @Override
    public PessoaModel findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceAccessException("Pessoa não encontrada"));
    }


    @Override
    public PessoaModel create(PessoaDto dto) {
        if (dto.nome() == null || dto.dataNasc() == null || dto.cpf() == null) {
            throw new RuntimeException("Campos obrigatórios não preenchidos");
        }

        if (dto.cpf().length() != 11) {
            throw new RuntimeException("CPF inválido");
        }

        if (dto.sexo() == null || !(dto.sexo() == 'M' || dto.sexo() == 'F')) {
            throw new RuntimeException("Sexo deve ser M (Masculino) ou F (Feminino)");
        }

        if (!isValidDate(dto.dataNasc())) {
            throw new RuntimeException("Data de nascimento inválida");
        }

        if (repository.existsByCpf(dto.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        PessoaModel newUser = new PessoaModel();
        newUser.setNome(dto.nome().toUpperCase());
        newUser.setDataNasc(dto.dataNasc());
        newUser.setCpf(dto.cpf());
        newUser.setSexo(Character.toUpperCase(dto.sexo()));
        newUser.setAltura(dto.altura());
        newUser.setPeso(dto.peso());

        return repository.save(newUser);
    }

    private boolean isValidDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        return date.before(cal.getTime());
    }

    @Override
    public PessoaModel delete(Integer id) {
        Optional<PessoaModel> user = repository.findById(id);
        if (user.isPresent()) {
            repository.delete(user.get());
            return user.get();  // Retorna o usuário excluído, ou pode ser null se não precisar retornar.
        } else {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
    }

    @Override
    public PessoaModel update(Integer id, PessoaDto entity) {
        if (entity == null) {
            throw new RuntimeException("PessoaDto não pode ser nulo");
        }

        Optional<PessoaModel> existingUser = repository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }

        PessoaModel userToUpdate = existingUser.get();

        if (entity.cpf() != null && !entity.cpf().equals(userToUpdate.getCpf())) {
            if (entity.cpf().length() != 11) {
                throw new RuntimeException("CPF inválido");
            }
            if (repository.existsByCpf(entity.cpf())) {
                throw new RuntimeException("CPF já cadastrado");
            }
            userToUpdate.setCpf(entity.cpf());
        }

        if (entity.nome() != null) {
            userToUpdate.setNome(entity.nome().toUpperCase());
        }
        if (entity.dataNasc() != null) {
            userToUpdate.setDataNasc(entity.dataNasc());
        }
        if (entity.sexo() != '\0') {
            userToUpdate.setSexo(Character.toUpperCase(entity.sexo()));
        }
        if (entity.altura() != null) {
            userToUpdate.setAltura(entity.altura());
        }
        if (entity.peso() != null) {
            userToUpdate.setPeso(entity.peso());
        }

        return repository.save(userToUpdate);
    }

}
