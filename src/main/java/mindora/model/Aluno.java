package mindora.model;

import java.time.LocalDate;

public class Aluno {
    private Long id;
    private String nome;
    private LocalDate dataNasc;
    //Nivel de supervisão do aluno TEA
    private String nivelSup;
    private String observacao;

    public Aluno() {}

    // Construtor para novos cadastros (sem ID, pois o banco gera com SERIAL)
    public Aluno(String nome, LocalDate dataNasc, String nivelSup, String observacao) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.nivelSup = nivelSup;
        this.observacao = observacao;
    }

    // Construtor completo com ID (para consultas e atualizações no banco)
    public Aluno(Long id, String nome, LocalDate dataNasc, String nivelSup, String observacao) {
        this.id = id;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.nivelSup = nivelSup;
        this.observacao = observacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataNasc() { return dataNasc; }
    public void setDataNasc(LocalDate dataNasc) { this.dataNasc = dataNasc; }

    public String getNivelSup() { return nivelSup; }
    public void setNivelSup(String nivelSup) { this.nivelSup = nivelSup; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}