package mindora.model;

import java.time.LocalDate;

public class Aluno {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Long responsavelId;
    private String responsavelNome;

    public Aluno() {
    }

    public Aluno(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public Aluno(Long id, String nome, LocalDate dataNascimento, Long responsavelId, String responsavelNome) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.responsavelId = responsavelId;
        this.responsavelNome = responsavelNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    @Override
    public String toString() {
        return nome;
    }
}