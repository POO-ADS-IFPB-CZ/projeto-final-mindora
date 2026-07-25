package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Responsavel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO {

    public void salvar(Responsavel responsavel) throws SQLException {
        String sql = "INSERT INTO responsavel (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, responsavel.getNome());
            stmt.setString(2, responsavel.getCpf());
            stmt.setString(3, responsavel.getTelefone());
            stmt.setString(4, responsavel.getEmail());
            stmt.executeUpdate();
        }
    }

    public List<Responsavel> listarTodos() throws SQLException {
        List<Responsavel> lista = new ArrayList<>();
        String sql = "SELECT * FROM responsavel ORDER BY id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Responsavel r = new Responsavel(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
                lista.add(r);
            }
        }
        return lista;
    }

    public void atualizar(Responsavel responsavel) throws SQLException {
        String sql = "UPDATE responsavel SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, responsavel.getNome());
            stmt.setString(2, responsavel.getCpf());
            stmt.setString(3, responsavel.getTelefone());
            stmt.setString(4, responsavel.getEmail());
            stmt.setLong(5, responsavel.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM responsavel WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}