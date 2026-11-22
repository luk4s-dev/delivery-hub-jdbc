package br.ucsal.deliveryhub.service;

import br.ucsal.deliveryhub.dao.*;
import br.ucsal.deliveryhub.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DeliveryService {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final EntregadorDAO entregadorDAO = new EntregadorDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ProdutoPedidoDAO produtoPedidoDAO = new ProdutoPedidoDAO();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    public Pedido criarPedido(int idCliente, int idEndereco) {
        Endereco endereco = enderecoDAO.buscarPorId(idEndereco);
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não encontrado.");
        }
        // Regra de negócio: endereço deve pertencer ao cliente do pedido
        if (endereco.getIdCliente() != idCliente) {
            throw new IllegalArgumentException("O endereço selecionado não pertence a este cliente!");
        }

        Pedido pedido = new Pedido();
        pedido.setIdCliente(idCliente);
        pedido.setIdEndereco(idEndereco);
        pedido.setIdEntregador(null); // Começa sem entregador
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setValorTotal(BigDecimal.ZERO);

        pedidoDAO.inserir(pedido);
        return pedido;
    }

    public void adicionarItemAoPedido(int idPedido, int idProduto, int quantidade) {
        // Regra de negócio: quantidade > 0
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do produto deve ser maior que zero.");
        }

        Produto produto = produtoDAO.buscarPorId(idProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        // Regra de negócio: preserva o preço do produto no momento da compra
        ProdutoPedido item = new ProdutoPedido(idPedido, idProduto, quantidade, produto.getPreco());
        produtoPedidoDAO.inserir(item);
    }

    public void atribuirEntregador(int idPedido, int idEntregador) {
        Entregador entregador = entregadorDAO.buscarPorId(idEntregador);
        if (entregador == null) {
            throw new IllegalArgumentException("Entregador não encontrado.");
        }
        // Regra de negócio: entregador indisponível não pode receber pedidos
        if (!"DISPONIVEL".equalsIgnoreCase(entregador.getStatus())) {
            throw new IllegalArgumentException("Este entregador está INDISPONÍVEL no momento.");
        }

        pedidoDAO.atribuirEntregador(idPedido, idEntregador);
        entregadorDAO.atualizarStatus(idEntregador, "INDISPONIVEL");
    }

    public void registrarPagamento(int idPedido, String formaPagamento, BigDecimal valor) {
        if (pagamentoDAO.buscarPorPedido(idPedido) != null) {
            throw new IllegalStateException("Este pedido já possui um pagamento registrado.");
        }

        Pagamento pag = new Pagamento();
        pag.setIdPedido(idPedido);
        pag.setFormaPagamento(formaPagamento);
        pag.setValor(valor);
        pag.setStatus("APROVADO");
        pag.setDataPagamento(LocalDateTime.now());

        pagamentoDAO.inserir(pag);
    }

    public void validarAntesDeFinalizar(int idPedido) {
        List<ProdutoPedido> itens = produtoPedidoDAO.listarPorPedido(idPedido);
        if (itens.isEmpty()) {
            throw new IllegalStateException("O pedido precisa ter pelo menos 1 item para ser finalizado.");
        }
        if (pagamentoDAO.buscarPorPedido(idPedido) == null) {
            throw new IllegalStateException("O pedido só pode ser finalizado após possuir pagamento registrado.");
        }
    }
}