package br.ucsal.deliveryhub.dao;

import br.ucsal.deliveryhub.database.ConnectionFactory;
import br.ucsal.deliveryhub.model.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void inserir(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, id_endereco, id_entregador, data_pedido, status, valor_total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pedido.getIdCliente());
            ps.setInt(2, pedido.getIdEndereco());

            // Trata id_entregador nulo quando o pedido acaba de ser criado
            if (pedido.getIdEntregador() != null) {
                ps.setInt(3, pedido.getIdEntregador());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setTimestamp(4, Timestamp.valueOf(pedido.getDataPedido()));
            ps.setString(5, pedido.getStatus());
            ps.setBigDecimal(6, pedido.getValorTotal());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pedido.setIdPedido(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage(), e);
        }
    }

    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPedido(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido: " + e.getMessage(), e);
        }
    }

    public List<Pedido> listarTodos() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearPedido(rs));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage(), e);
        }
    }

    public void atribuirEntregador(int idPedido, int idEntregador) {
        String sql = "UPDATE pedido SET id_entregador = ?, status = 'EM_ENTREGA' WHERE id_pedido = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntregador);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atribuir entregador: " + e.getMessage(), e);
        }
    }

    public void atualizarStatus(int idPedido, String status) {
        String sql = "UPDATE pedido SET status = ? WHERE id_pedido = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do pedido: " + e.getMessage(), e);
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("id_pedido"));
        p.setIdCliente(rs.getInt("id_cliente"));
        p.setIdEndereco(rs.getInt("id_endereco"));

        int idEntregador = rs.getInt("id_entregador");
        p.setIdEntregador(rs.wasNull() ? null : idEntregador);

        p.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
        p.setStatus(rs.getString("status"));
        p.setValorTotal(rs.getBigDecimal("valor_total"));
        return p;
    }
}
