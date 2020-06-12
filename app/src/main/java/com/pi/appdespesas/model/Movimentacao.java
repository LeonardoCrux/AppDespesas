package com.pi.appdespesas.model;

public class Movimentacao {

    private double valor;
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private String id;

    public Movimentacao(){

    }

    public Movimentacao(double valor, String data, String categoria, String descricao, String tipo) {
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.tipo =  tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
