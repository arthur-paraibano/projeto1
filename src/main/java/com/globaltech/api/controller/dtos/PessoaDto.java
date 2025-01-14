package com.globaltech.api.controller.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record PessoaDto(
        @NotBlank(message = "O nome não deve estar em branco") String nome,
        @NotNull(message = "A data de nascimento não deve estar em branco") Date dataNasc,
        @CPF(message = "CPF inválido") @NotBlank(message = "O CPF não deve estar em branco") String cpf,
        @NotNull(message = "O sexo não deve estar em branco") Character sexo,
        @NotNull(message = "A altura não deve estar em branco") @DecimalMin(value = "0.0") Double altura,
        @NotNull(message = "O peso não deve estar em branco") @DecimalMin(value = "0.0") Double peso
) {
}