package br.ucsal.deliveryhub.dao;

import br.ucsal.deliveryhub.database.ConnectionFactory;
import br.ucsal.deliveryhub.model.Entregador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregadorDAO {

    public void inserir(Entregador entregador) {
        String sql = "INSERT INTO entregador (nome, telefone, veiculo, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entregador.getNome());
            ps.setString(2, entregador.getTelefone());
            ps.setString(3, entregador.getVeiculo());
            ps.setString(4, entregador.getStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entregador.setIdEntregador(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir entregador: " + e.getMessage(), e);
        }
    }

    public Entregador buscarPorId(int id) {
        String sql = "SELECT * FROM entregador WHERE id_entregador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Entregador ent = new Entregador();
                    ent.setIdEntregador(rs.getInt("id_entregador"));
                    ent.setNome(rs.getString("nome"));
                    ent.setTelefone(rs.getString("telefone"));
                    ent.setVeiculo(rs.getString("veiculo"));
                    ent.setStatus(rs.getString("status"));
                    return ent;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar entregador: " + e.getMessage(), e);
        }
    }

    public List<Entregador> listarTodos() {
        String sql = "SELECT * FROM entregador";
        List<Entregador> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Entregador ent = new Entregador();
                ent.setIdEntregador(rs.getInt("id_entregador"));
                ent.setNome(rs.getString("nome"));
                ent.setTelefone(rs.getString("telefone"));
                ent.setVeiculo(rs.getString("veiculo"));
                ent.setStatus(rs.getString("status"));
                lista.add(ent);
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar entregadores: " + e.getMessage(), e);
        }
    }

    public void atualizarStatus(int idEntregador, String novoStatus) {
        String sql = "UPDATE entregador SET status = ? WHERE id_entregador = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            ps.setInt(2, idEntregador);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do entregador: " + e.getMessage(), e);
        }
    }
}