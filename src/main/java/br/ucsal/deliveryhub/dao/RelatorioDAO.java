package br.ucsal.deliveryhub.dao;

import br.ucsal.deliveryhub.database.ConnectionFactory;
import java.math.BigDecimal;
import java.sql.*;

public class RelatorioDAO {

    // 1. Chamada à VIEW vw_pedidos_detalhados
    public void listarPedidosDetalhadosView() {
        String sql = "SELECT * FROM vw_pedidos_detalhados";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== PEDIDOS DETALHADOS (VIEW) ===");
            while (rs.next()) {
                // Ajuste os nomes das colunas conforme os aliases que você definiu na sua VIEW
                System.out.printf("Pedido #%d | Cliente: %s | Entregador: %s | Status: %s | Total: R$ %.2f%n",
                        rs.getInt("id_pedido"),
                        rs.getString("cliente"),
                        rs.getString("entregador"),
                        rs.getString("status"),
                        rs.getBigDecimal("valor_total")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar vw_pedidos_detalhados: " + e.getMessage(), e);
        }
    }

    // 2. Chamada à FUNCTION calcular_total_pedido(id_pedido)
    public BigDecimal calcularTotalPedidoFunction(int idPedido) {
        String sql = "SELECT calcular_total_pedido(?) AS total";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
            return BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar function calcular_total_pedido: " + e.getMessage(), e);
        }
    }

    // 3. Chamada à PROCEDURE finalizar_pedido(id_pedido) via CallableStatement
    public void finalizarPedidoProcedure(int idPedido) {
        String sql = "{CALL finalizar_pedido(?)}";
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idPedido);
            cs.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar procedure finalizar_pedido: " + e.getMessage(), e);
        }
    }
}