package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Aluno;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // 1. CREATE - Salvar um novo aluno na base de dados
    public void salvar(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO aluno (nome, data_nasc, nivel_sup, observacao) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, aluno.getDataNasc() != null ? Date.valueOf(aluno.getDataNasc()) : null);
            stmt.setString(3, aluno.getNivelSup());
            stmt.setString(4, aluno.getObservacao());

            stmt.executeUpdate();
        }
    }

    // 2. READ - Listar todos os alunos cadastrados
    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno ORDER BY id";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nome = rs.getString("nome");
                Date dateVal = rs.getDate("data_nasc");
                LocalDate dataNasc = dateVal != null ? dateVal.toLocalDate() : null;
                String nivelSup = rs.getString("nivel_sup");
                String observacao = rs.getString("observacao");

                alunos.add(new Aluno(id, nome, dataNasc, nivelSup, observacao));
            }
        }
        return alunos;
    }

    // 3. UPDATE - Atualizar dados de um aluno existente
    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE aluno SET nome = ?, data_nasc = ?, nivel_sup = ?, observacao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, aluno.getDataNasc() != null ? Date.valueOf(aluno.getDataNasc()) : null);
            stmt.setString(3, aluno.getNivelSup());
            stmt.setString(4, aluno.getObservacao());
            stmt.setLong(5, aluno.getId());

            stmt.executeUpdate();
        }
    }

    // 4. DELETE - Remover um aluno pelo ID
    public void deletar(Long id) throws SQLException {
        String sql = "DELETE FROM aluno WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}