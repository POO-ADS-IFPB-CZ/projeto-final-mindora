package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Profissional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfissionalDAO {

    public void salvar(Profissional profissional) throws SQLException {
        String sql = "INSERT INTO profissional (nome, especialidade, registro) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profissional.getNome());
            stmt.setString(2, profissional.getEspecialidade());
            stmt.setString(3, profissional.getRegistro());
            stmt.executeUpdate();
        }
    }

    public List<Profissional> listarTodos() throws SQLException {
        List<Profissional> lista = new ArrayList<>();
        String sql = "SELECT * FROM profissional ORDER BY id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Profissional p = new Profissional(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("especialidade"),
                        rs.getString("registro")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public void atualizar(Profissional profissional) throws SQLException {
        String sql = "UPDATE profissional SET nome = ?, especialidade = ?, registro = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profissional.getNome());
            stmt.setString(2, profissional.getEspecialidade());
            stmt.setString(3, profissional.getRegistro());
            stmt.setLong(4, profissional.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM profissional WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}