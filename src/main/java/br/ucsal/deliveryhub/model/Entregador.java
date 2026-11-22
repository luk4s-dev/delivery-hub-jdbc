package br.ucsal.deliveryhub.model;

public class Entregador {
    private int idEntregador;
    private String nome;
    private String telefone;
    private String veiculo;
    private String status;

    public Entregador() {}

    public Entregador(String nome, String telefone, String veiculo, String status) {
        this.nome = nome;
        this.telefone = telefone;
        this.veiculo = veiculo;
        this.status = status;
    }

    public int getIdEntregador() { return idEntregador }
    public void setIdEntregador(int idEntregador) { this.idEntregador = idEntregador; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
