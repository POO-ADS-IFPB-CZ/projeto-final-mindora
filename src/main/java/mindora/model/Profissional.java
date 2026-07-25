package mindora.model;

public class Profissional {
    private Long id;
    private String nome;
    private String especialidade;
    private String registro;
    
    public Profissional() {
    }

    public Profissional(String nome, String especialidade, String registro) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.registro = registro;
    }

    public Profissional(Long id, String nome, String especialidade, String registro) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.registro = registro;
    }

    // Getters e Setters
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}