package mindora.dao;

import mindora.config.ConnectionFactory;
import mindora.model.Sessao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessaoDAO {

    public void salvar(Sessao sessao, List<Long> atividadeIds) throws SQLException {
        String sqlSessao = "INSERT INTO sessao (data, duracao_min, status, nota, aluno_id, profissional_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        String sqlSessaoAtividade = "INSERT INTO sessao_atividade (sessao_id, atividade_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                long sessaoIdGerada;
                try (PreparedStatement stmtSessao = conn.prepareStatement(sqlSessao)) {
                    stmtSessao.setDate(1, Date.valueOf(sessao.getData()));
                    stmtSessao.setInt(2, sessao.getDuracaoMin());
                    stmtSessao.setString(3, sessao.getStatus());

                    if (sessao.getNota() != null) {
                        stmtSessao.setDouble(4, sessao.getNota());
                    } else {
                        stmtSessao.setNull(4, Types.NUMERIC);
                    }

                    stmtSessao.setLong(5, sessao.getAlunoId());
                    stmtSessao.setLong(6, sessao.getProfissionalId());

                    ResultSet rs = stmtSessao.executeQuery();
                    if (rs.next()) {
                        sessaoIdGerada = rs.getLong(1);
                        sessao.setId(sessaoIdGerada);
                    } else {
                        throw new SQLException("Falha ao agendar sessão.");
                    }
                }
                
                if (atividadeIds != null && !atividadeIds.isEmpty()) {
                    try (PreparedStatement stmtRel = conn.prepareStatement(sqlSessaoAtividade)) {
                        for (Long atividadeId : atividadeIds) {
                            stmtRel.setLong(1, sessaoIdGerada);
                            stmtRel.setLong(2, atividadeId);
                            stmtRel.executeUpdate();
                        }
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public List<Sessao> listarTodos() throws SQLException {
        List<Sessao> lista = new ArrayList<>();
        String sql = "SELECT s.id, s.data, s.duracao_min, s.status, s.nota, " +
                "s.aluno_id, a.nome AS aluno_nome, " +
                "s.profissional_id, p.nome AS profissional_nome, " +
                "STRING_AGG(at.titulo, ', ') AS atividades_titulos " +
                "FROM sessao s " +
                "JOIN aluno a ON s.aluno_id = a.id " +
                "JOIN profissional p ON s.profissional_id = p.id " +
                "LEFT JOIN sessao_atividade sa ON s.id = sa.sessao_id " +
                "LEFT JOIN atividade at ON sa.atividade_id = at.id " +
                "GROUP BY s.id, s.data, s.duracao_min, s.status, s.nota, s.aluno_id, a.nome, s.profissional_id, p.nome " +
                "ORDER BY s.id";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Double notaObj = rs.getObject("nota") != null ? rs.getDouble("nota") : null;
                Sessao s = new Sessao(
                        rs.getLong("id"),
                        rs.getDate("data").toLocalDate(),
                        rs.getInt("duracao_min"),
                        rs.getString("status"),
                        notaObj,
                        rs.getLong("aluno_id"),
                        rs.getString("aluno_nome"),
                        rs.getLong("profissional_id"),
                        rs.getString("profissional_nome"),
                        rs.getString("atividades_titulos") != null ? rs.getString("atividades_titulos") : "Nenhuma"
                );
                lista.add(s);
            }
        }
        return lista;
    }

    public List<Long> buscarAtividadesIdsPorSessao(Long sessaoId) throws SQLException {
        List<Long> ids = new ArrayList<>();
        String sql = "SELECT atividade_id FROM sessao_atividade WHERE sessao_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, sessaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong("atividade_id"));
                }
            }
        }
        return ids;
    }

    public void atualizar(Sessao sessao, List<Long> atividadeIds) throws SQLException {
        String sqlSessao = "UPDATE sessao SET data = ?, duracao_min = ?, status = ?, nota = ?, aluno_id = ?, profissional_id = ? WHERE id = ?";
        String sqlDelAtiv = "DELETE FROM sessao_atividade WHERE sessao_id = ?";
        String sqlInsAtiv = "INSERT INTO sessao_atividade (sessao_id, atividade_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                try (PreparedStatement stmtSessao = conn.prepareStatement(sqlSessao)) {
                    stmtSessao.setDate(1, Date.valueOf(sessao.getData()));
                    stmtSessao.setInt(2, sessao.getDuracaoMin());
                    stmtSessao.setString(3, sessao.getStatus());

                    if (sessao.getNota() != null) {
                        stmtSessao.setDouble(4, sessao.getNota());
                    } else {
                        stmtSessao.setNull(4, Types.NUMERIC);
                    }

                    stmtSessao.setLong(5, sessao.getAlunoId());
                    stmtSessao.setLong(6, sessao.getProfissionalId());
                    stmtSessao.setLong(7, sessao.getId());
                    stmtSessao.executeUpdate();
                }

                try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelAtiv)) {
                    stmtDel.setLong(1, sessao.getId());
                    stmtDel.executeUpdate();
                }

                if (atividadeIds != null && !atividadeIds.isEmpty()) {
                    try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsAtiv)) {
                        for (Long ativId : atividadeIds) {
                            stmtIns.setLong(1, sessao.getId());
                            stmtIns.setLong(2, ativId);
                            stmtIns.executeUpdate();
                        }
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }

    public void deletar(Long id) throws SQLException {
        String sqlDelAtiv = "DELETE FROM sessao_atividade WHERE sessao_id = ?";
        String sqlDelSessao = "DELETE FROM sessao WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                try (PreparedStatement stmtDelAtiv = conn.prepareStatement(sqlDelAtiv)) {
                    stmtDelAtiv.setLong(1, id);
                    stmtDelAtiv.executeUpdate();
                }

                try (PreparedStatement stmtDelSessao = conn.prepareStatement(sqlDelSessao)) {
                    stmtDelSessao.setLong(1, id);
                    stmtDelSessao.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }
}