package com.globaltech.apiTest.controller;

import com.globaltech.api.controller.PessoaController;
import com.globaltech.api.domain.models.PessoaModel;
import com.globaltech.api.services.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PessoaControllerTests {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsersReturnsUsers() {
        PessoaModel user = new PessoaModel();
        user.setId(1);
        user.setNome("João Teste");

        when(pessoaService.findAll()).thenReturn(List.of(user));

        ResponseEntity<List<PessoaModel>> response = pessoaController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("João Teste", response.getBody().get(0).getNome());
    }

    @Test
    void testGetAllUsersReturnsNotFound() {
        when(pessoaService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PessoaModel>> response = pessoaController.getAllUsers();

        assertEquals(404, response.getStatusCodeValue());
        verify(pessoaService, times(1)).findAll();
    }

    @Test
    void testGetUserByIdReturnsUser() {
        PessoaModel user = new PessoaModel();
        user.setId(1);
        user.setNome("João Teste");

        when(pessoaService.findById(1)).thenReturn(user);

        ResponseEntity<PessoaModel> response = pessoaController.getUserById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("João Teste", response.getBody().getNome());
    }

    @Test
    void testGetUserByIdReturnsNotFound() {
        when(pessoaService.findById(1)).thenReturn(null);

        ResponseEntity<PessoaModel> response = pessoaController.getUserById(1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAddUserSuccess() {
        PessoaModel user = new PessoaModel();
        user.setId(1);
        user.setNome("João Teste");

        when(pessoaService.create(any())).thenReturn(user);

        ResponseEntity<Object> response = pessoaController.addUser(null);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("João Teste", ((PessoaModel) response.getBody()).getNome());
    }

    @Test
    void testAddUserThrowsException() {
        when(pessoaService.create(any())).thenThrow(new RuntimeException("Erro ao salvar"));

        ResponseEntity<Object> response = pessoaController.addUser(null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testUpdateUserSuccess() {
        PessoaModel user = new PessoaModel();
        user.setId(1);
        user.setNome("João Atualizado");

        when(pessoaService.update(eq(1), any())).thenReturn(user);

        ResponseEntity<PessoaModel> response = pessoaController.updateUser(1, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("João Atualizado", response.getBody().getNome());
    }

    @Test
    void testUpdateUserNotFound() {
        when(pessoaService.update(eq(1), any())).thenReturn(null);

        ResponseEntity<PessoaModel> response = pessoaController.updateUser(1, null);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteUserSuccess() {
        doNothing().when(pessoaService).delete(1);

        ResponseEntity<Void> response = pessoaController.deleteUser(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(pessoaService, times(1)).delete(1);
    }

    @Test
    void testDeleteUserNotFound() {
        doThrow(new RuntimeException("Usuário não encontrado")).when(pessoaService).delete(1);

        ResponseEntity<Void> response = pessoaController.deleteUser(1);

        assertEquals(404, response.getStatusCodeValue());
    }
}
