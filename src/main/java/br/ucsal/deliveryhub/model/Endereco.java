package br.ucsal.deliveryhub.model;

public class Endereco {
    private int idEndereco;
    private int idCliente;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String referencia;
    private String tipo;
    private boolean principal;

    public Endereco() {}

    public int getIdEndereco() { return idEndereco; }

    public void setIdEndereco(int idEndereco) { this.idEndereco = idEndereco; }

    public int getIdCliente() { return idCliente; }

    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getCep() { return cep; }

    public void setCep(String cep) { this.cep = cep; }

    public String getLogradouro() { return logradouro; }

    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getNumero() { return numero; }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() { return complemento; }

    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }

    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }

    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public String getReferencia() { return referencia; }

    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getTipo() { return tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isPrincipal() { return principal; }

    public void setPrincipal(boolean principal) { this.principal = principal; }
}
