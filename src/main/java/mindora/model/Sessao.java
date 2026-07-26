package mindora.model;

import java.time.LocalDate;

public class Sessao {
    private Long id;
    private LocalDate data;
    private Integer duracaoMin;
    private String status; // 'agendada', 'realizada', 'cancelada'
    private Double nota;   // 0 a 10 (opcional / null)
    private Long alunoId;
    private String alunoNome;
    private Long profissionalId;
    private String profissionalNome;
    private String atividadesTitulos; // Nomes das atividades vinculadas

    public Sessao() {
    }

    public Sessao(LocalDate data, Integer duracaoMin, String status, Double nota, Long alunoId, Long profissionalId) {
        this.data = data;
        this.duracaoMin = duracaoMin;
        this.status = status;
        this.nota = nota;
        this.alunoId = alunoId;
        this.profissionalId = profissionalId;
    }

    public Sessao(Long id, LocalDate data, Integer duracaoMin, String status, Double nota,
                  Long alunoId, String alunoNome, Long profissionalId, String profissionalNome, String atividadesTitulos) {
        this.id = id;
        this.data = data;
        this.duracaoMin = duracaoMin;
        this.status = status;
        this.nota = nota;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.profissionalId = profissionalId;
        this.profissionalNome = profissionalNome;
        this.atividadesTitulos = atividadesTitulos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(Integer duracaoMin) {
        this.duracaoMin = duracaoMin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public String getProfissionalNome() {
        return profissionalNome;
    }

    public void setProfissionalNome(String profissionalNome) {
        this.profissionalNome = profissionalNome;
    }

    public String getAtividadesTitulos() {
        return atividadesTitulos;
    }

    public void setAtividadesTitulos(String atividadesTitulos) {
        this.atividadesTitulos = atividadesTitulos;
    }
}