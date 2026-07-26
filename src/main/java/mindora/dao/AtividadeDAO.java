package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Atividade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAO {

    public void salvar(Atividade atividade) throws SQLException {
        String sql = "INSERT INTO atividade (titulo, tipo, descricao, nivel) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, atividade.getTitulo());
            stmt.setString(2, atividade.getTipo());
            stmt.setString(3, atividade.getDescricao());
            stmt.setString(4, atividade.getNivel());
            stmt.executeUpdate();
        }
    }

    public List<Atividade> listarTodos() throws SQLException {
        List<Atividade> lista = new ArrayList<>();
        String sql = "SELECT * FROM atividade ORDER BY id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Atividade a = new Atividade(
                        rs.getLong("id"),
                        rs.getString("titulo"),
                        rs.getString("tipo"),
                        rs.getString("descricao"),
                        rs.getString("nivel")
                );
                lista.add(a);
            }
        }
        return lista;
    }

    public void atualizar(Atividade atividade) throws SQLException {
        String sql = "UPDATE atividade SET titulo = ?, tipo = ?, descricao = ?, nivel = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, atividade.getTitulo());
            stmt.setString(2, atividade.getTipo());
            stmt.setString(3, atividade.getDescricao());
            stmt.setString(4, atividade.getNivel());
            stmt.setLong(5, atividade.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM atividade WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}