package br.ucsal.deliveryhub.dao;

import br.ucsal.deliveryhub.database.ConnectionFactory;
import br.ucsal.deliveryhub.model.Pagamento;
import java.sql.*;

public class PagamentoDAO {

    public void inserir(Pagamento pag) {
        String sql = "INSERT INTO pagamento (id_pedido, forma_pagamento, valor, status, data_pagamento) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pag.getIdPedido());
            ps.setString(2, pag.getFormaPagamento());
            ps.setBigDecimal(3, pag.getValor());
            ps.setString(4, pag.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(pag.getDataPagamento()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pag.setIdPagamento(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar pagamento: " + e.getMessage(), e);
        }
    }

    public Pagamento buscarPorPedido(int idPedido) {
        String sql = "SELECT * FROM pagamento WHERE id_pedido = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pagamento p = new Pagamento();
                    p.setIdPagamento(rs.getInt("id_pagamento"));
                    p.setIdPedido(rs.getInt("id_pedido"));
                    p.setFormaPagamento(rs.getString("forma_pagamento"));
                    p.setValor(rs.getBigDecimal("valor"));
                    p.setStatus(rs.getString("status"));
                    p.setDataPagamento(rs.getTimestamp("data_pagamento").toLocalDateTime());
                    return p;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pagamento do pedido: " + e.getMessage(), e);
        }
    }
}