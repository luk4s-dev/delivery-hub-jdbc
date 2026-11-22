package br.ucsal.deliveryhub.dao;

import br.ucsal.deliveryhub.database.ConnectionFactory;
import br.ucsal.deliveryhub.model.ProdutoPedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoPedidoDAO {

    public void inserir(ProdutoPedido item) {
        String sql = "INSERT INTO produto_pedido (id_pedido, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, item.getIdPedido());
            ps.setInt(2, item.getIdProduto());
            ps.setInt(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar item ao pedido: " + e.getMessage(), e);
        }
    }

    public List<ProdutoPedido> listarPorPedido(int idPedido) {
        String sql = "SELECT * FROM produto_pedido WHERE id_pedido = ?";
        List<ProdutoPedido> itens = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    itens.add(new ProdutoPedido(
                            rs.getInt("id_pedido"),
                            rs.getInt("id_produto"),
                            rs.getInt("quantidade"),
                            rs.getBigDecimal("preco_unitario")
                    ));
                }
            }
            return itens;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens do pedido: " + e.getMessage(), e);
        }
    }
}