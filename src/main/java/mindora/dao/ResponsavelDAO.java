package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Responsavel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO {

    public void salvar(Responsavel responsavel) throws SQLException {
        String sqlResponsavel = "INSERT INTO responsavel (nome, email) VALUES (?, ?) RETURNING id";
        String sqlTelefone = "INSERT INTO responsavel_telefone (responsavel_id, telefone) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtResp = conn.prepareStatement(sqlResponsavel)) {

            stmtResp.setString(1, responsavel.getNome());
            stmtResp.setString(2, responsavel.getEmail());

            ResultSet rs = stmtResp.executeQuery();

            if (rs.next()) {
                long idGerado = rs.getLong(1);
                responsavel.setId(idGerado);

                // Se informou telefone, insere na tabela responsavel_telefone
                if (responsavel.getTelefone() != null && !responsavel.getTelefone().trim().isEmpty()) {
                    try (PreparedStatement stmtTel = conn.prepareStatement(sqlTelefone)) {
                        stmtTel.setLong(1, idGerado);
                        stmtTel.setString(2, responsavel.getTelefone());
                        stmtTel.executeUpdate();
                    }
                }
            }
        }
    }

    public List<Responsavel> listarTodos() throws SQLException {
        List<Responsavel> lista = new ArrayList<>();
        // Faz LEFT JOIN para buscar o telefone na tabela responsavel_telefone
        String sql = "SELECT r.id, r.nome, r.email, rt.telefone " +
                "FROM responsavel r " +
                "LEFT JOIN responsavel_telefone rt ON r.id = rt.responsavel_id " +
                "ORDER BY r.id";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Responsavel r = new Responsavel(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                lista.add(r);
            }
        }
        return lista;
    }

    public void atualizar(Responsavel responsavel) throws SQLException {
        String sqlResp = "UPDATE responsavel SET nome = ?, email = ? WHERE id = ?";
        String sqlDelTel = "DELETE FROM responsavel_telefone WHERE responsavel_id = ?";
        String sqlInsTel = "INSERT INTO responsavel_telefone (responsavel_id, telefone) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtResp = conn.prepareStatement(sqlResp)) {

            stmtResp.setString(1, responsavel.getNome());
            stmtResp.setString(2, responsavel.getEmail());
            stmtResp.setLong(3, responsavel.getId());
            stmtResp.executeUpdate();

            // Remove o telefone antigo e re-insere o novo (caso preenchido)
            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelTel)) {
                stmtDel.setLong(1, responsavel.getId());
                stmtDel.executeUpdate();
            }

            if (responsavel.getTelefone() != null && !responsavel.getTelefone().trim().isEmpty()) {
                try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsTel)) {
                    stmtIns.setLong(1, responsavel.getId());
                    stmtIns.setString(2, responsavel.getTelefone());
                    stmtIns.executeUpdate();
                }
            }
        }
    }

    public void deletar(Long id) throws SQLException {
        String sqlTel = "DELETE FROM responsavel_telefone WHERE responsavel_id = ?";
        String sqlResp = "DELETE FROM responsavel WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            // Remove da tabela dependente primeiro para respeitar a Foreign Key
            try (PreparedStatement stmtTel = conn.prepareStatement(sqlTel)) {
                stmtTel.setLong(1, id);
                stmtTel.executeUpdate();
            }

            try (PreparedStatement stmtResp = conn.prepareStatement(sqlResp)) {
                stmtResp.setLong(1, id);
                stmtResp.executeUpdate();
            }
        }
    }
}