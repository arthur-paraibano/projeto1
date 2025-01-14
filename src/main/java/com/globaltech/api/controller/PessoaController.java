package com.globaltech.api.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globaltech.api.controller.dtos.PessoaDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.globaltech.api.domain.models.PessoaModel;
import com.globaltech.api.services.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/cadastro", produces = {"application/json"})
@Tag(name = "Pessoa API", description = "API para operações de inclusão, alteração, deleção e update")
@CrossOrigin(origins = "http://localhost:4200")
public class PessoaController {

    @Autowired
    private final PessoaService service;


    public PessoaController(PessoaService service) {
        this.service = service;
    }


    @GetMapping("/all")
    @Operation(summary = "Listar todos cadastrados", description = "Retorna uma lista de todos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de nomes retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum retorno encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<List<PessoaModel>> getAllUsers() {
        List<PessoaModel> usersList = service.findAll();
        if (!usersList.isEmpty()) {
            for (PessoaModel user : usersList) {
                Integer id = user.getId();
                user.add(linkTo(methodOn(PessoaController.class).getUserById(id)).withSelfRel());
            }
            return ResponseEntity.status(HttpStatus.OK).body(usersList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<PessoaModel> getUserById(@PathVariable Integer id) {
        PessoaModel user = service.findById(id);
        if (user != null) {
            user.add(linkTo(methodOn(PessoaController.class).getAllUsers()).withRel("Pessoa List"));
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar usuário por nome", description = "Retorna um usuário específico pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity getUserByName(@PathVariable String name)  {
        PessoaModel user = service.findByNome(name);
        if (user != null) {
            user.add(linkTo(methodOn(PessoaController.class).getAllUsers()).withRel("Pessoa List"));
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Adicionar novo usuário", description = "Exemplo: { \"nome\": \"João Teste\", \"dataNasc\": \"1999-11-10\", \"cpf\": \"12345678901\", \"sexo\": \"M\", \"altura\": 1.70, \"peso\": 80.5 }")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Object> addUser(@Validated @RequestBody PessoaDto dto) {
    	System.out.println("Dados recebidos na API: " + dto);
        try {
            PessoaModel newUser = service.create(dto);
            newUser.add(linkTo(methodOn(PessoaController.class).getUserById(newUser.getId())).withSelfRel());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<PessoaModel> updateUser(@PathVariable Integer id, @RequestBody @Validated PessoaDto dto) {
        PessoaModel updatedUser = service.update(id, dto);
        if (updatedUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/peso-ideal/{altura}/{genero}")
    public ResponseEntity<?> calcularPesoIdeal(@PathVariable double altura, @PathVariable String genero) {
        if (altura <= 0) {
            return ResponseEntity.badRequest().body("Altura deve ser maior que zero.");
        }

        double pesoIdeal;
        if (genero.equalsIgnoreCase("m")) {
            pesoIdeal = (72.7 * altura) - 58;
        } else if (genero.equalsIgnoreCase("f")) {
            pesoIdeal = (62.1 * altura) - 44.7;
        } else {
            return ResponseEntity.badRequest().body("Gênero inválido. Use 'M' ou 'F'.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("altura", altura);
        response.put("genero", genero);
        response.put("pesoIdeal", pesoIdeal);

        return ResponseEntity.ok(response);
    }
}
