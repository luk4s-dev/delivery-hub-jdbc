package br.ucsal.deliveryhub.dao;

import br.ucsal.deliveryhub.database.ConnectionFactory;
import br.ucsal.deliveryhub.model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    public void inserir(Endereco end) {
        String sql = "INSERT INTO endereco (id_cliente, cep, logradouro, numero, complemento, bairro, cidade, estado, referencia, tipo, principal) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, end.getIdCliente());
            ps.setString(2, end.getCep());
            ps.setString(3, end.getLogradouro());
            ps.setString(4, end.getNumero());
            ps.setString(5, end.getComplemento());
            ps.setString(6, end.getBairro());
            ps.setString(7, end.getCidade());
            ps.setString(8, end.getEstado());
            ps.setString(9, end.getReferencia());
            ps.setString(10, end.getTipo());
            ps.setBoolean(11, end.isPrincipal());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) end.setIdEndereco(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir endereço: " + e.getMessage(), e);
        }
    }

    public Endereco buscarPorId(int idEndereco) {
        String sql = "SELECT * FROM endereco WHERE id_endereco = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEndereco);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearEndereco(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar endereço: " + e.getMessage(), e);
        }
    }

    public List<Endereco> listarPorCliente(int idCliente) {
        String sql = "SELECT * FROM endereco WHERE id_cliente = ?";
        List<Endereco> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearEndereco(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar endereços do cliente: " + e.getMessage(), e);
        }
    }

    public void deletar(int idEndereco) {
        String sql = "DELETE FROM endereco WHERE id_endereco = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEndereco);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar endereço: " + e.getMessage(), e);
        }
    }

    private Endereco mapearEndereco(ResultSet rs) throws SQLException {
        Endereco e = new Endereco();
        e.setIdEndereco(rs.getInt("id_endereco"));
        e.setIdCliente(rs.getInt("id_cliente"));
        e.setCep(rs.getString("cep"));
        e.setLogradouro(rs.getString("logradouro"));
        e.setNumero(rs.getString("numero"));
        e.setComplemento(rs.getString("complemento"));
        e.setBairro(rs.getString("bairro"));
        e.setCidade(rs.getString("cidade"));
        e.setEstado(rs.getString("estado"));
        e.setReferencia(rs.getString("referencia"));
        e.setTipo(rs.getString("tipo"));
        e.setPrincipal(rs.getBoolean("principal"));
        return e;
    }
}