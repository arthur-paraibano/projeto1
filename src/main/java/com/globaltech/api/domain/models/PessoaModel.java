package com.globaltech.api.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pessoa")
public class PessoaModel extends RepresentationModel<PessoaModel> implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Temporal(TemporalType.DATE) 
    @Column(name = "dataNasc")
    private Date dataNasc;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "sexo",length = 1)
    private Character sexo;

    @Column(name = "altura")
    private Double altura;
    
    @Column(name = "peso")
    private Double peso;
    
    public PessoaModel() {}
    
    public PessoaModel(String pNome, Date pData, String pCPF, char pSexo, double pAltura, double pPeso) {
        this.nome = pNome;
        this.dataNasc = pData;
        this.cpf = pCPF;
        this.sexo = pSexo;
        this.altura = pAltura;
        this.peso = pPeso;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Date getDataNasc() {
        return dataNasc;
    }
    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public Character getSexo() {
        return sexo;
    }
    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }
    public Double getAltura() {
        return altura;
    }
    public void setAltura(Double altura) {
        this.altura = altura;
    }
    public Double getPeso() {
        return peso;
    }
    public void setPeso(Double peso) {
        this.peso = peso;
    }
}