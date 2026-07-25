package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void salvar(Aluno aluno, Long responsavelId) throws SQLException {
        String sqlAluno = "INSERT INTO aluno (nome, data_nascimento) VALUES (?, ?) RETURNING id";
        String sqlRelacao = "INSERT INTO aluno_responsavel (aluno_id, responsavel_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtAluno = conn.prepareStatement(sqlAluno)) {

            stmtAluno.setString(1, aluno.getNome());
            stmtAluno.setDate(2, Date.valueOf(aluno.getDataNascimento()));
            ResultSet rs = stmtAluno.executeQuery();

            if (rs.next()) {
                long alunoIdGerado = rs.getLong(1);
                aluno.setId(alunoIdGerado);

                // Grava o vinculo na tabela relacional aluno_responsavel se um responsavel foi selecionado
                if (responsavelId != null) {
                    try (PreparedStatement stmtRel = conn.prepareStatement(sqlRelacao)) {
                        stmtRel.setLong(1, alunoIdGerado);
                        stmtRel.setLong(2, responsavelId);
                        stmtRel.executeUpdate();
                    }
                }
            }
        }
    }

    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        // Busca alunos e o nome do seu responsável usando a tabela relacional aluno_responsavel
        String sql = "SELECT a.id, a.nome, a.data_nascimento, r.id AS resp_id, r.nome AS resp_nome " +
                "FROM aluno a " +
                "LEFT JOIN aluno_responsavel ar ON a.id = ar.aluno_id " +
                "LEFT JOIN responsavel r ON ar.responsavel_id = r.id " +
                "ORDER BY a.id";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Date dataSql = rs.getDate("data_nascimento");
                Aluno a = new Aluno(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        dataSql != null ? dataSql.toLocalDate() : null,
                        rs.getObject("resp_id") != null ? rs.getLong("resp_id") : null,
                        rs.getString("resp_nome")
                );
                lista.add(a);
            }
        }
        return lista;
    }

    public void atualizar(Aluno aluno, Long responsavelId) throws SQLException {
        String sqlAluno = "UPDATE aluno SET nome = ?, data_nascimento = ? WHERE id = ?";
        String sqlDelRel = "DELETE FROM aluno_responsavel WHERE aluno_id = ?";
        String sqlInsRel = "INSERT INTO aluno_responsavel (aluno_id, responsavel_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtAluno = conn.prepareStatement(sqlAluno)) {

            stmtAluno.setString(1, aluno.getNome());
            stmtAluno.setDate(2, Date.valueOf(aluno.getDataNascimento()));
            stmtAluno.setLong(3, aluno.getId());
            stmtAluno.executeUpdate();

            // Atualiza o vínculo na tabela relacional
            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelRel)) {
                stmtDel.setLong(1, aluno.getId());
                stmtDel.executeUpdate();
            }

            if (responsavelId != null) {
                try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsRel)) {
                    stmtIns.setLong(1, aluno.getId());
                    stmtIns.setLong(2, responsavelId);
                    stmtIns.executeUpdate();
                }
            }
        }
    }

    public void deletar(Long id) throws SQLException {
        String sqlRel = "DELETE FROM aluno_responsavel WHERE aluno_id = ?";
        String sqlAluno = "DELETE FROM aluno WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmtRel = conn.prepareStatement(sqlRel)) {
                stmtRel.setLong(1, id);
                stmtRel.executeUpdate();
            }

            try (PreparedStatement stmtAluno = conn.prepareStatement(sqlAluno)) {
                stmtAluno.setLong(1, id);
                stmtAluno.executeUpdate();
            }
        }
    }
}