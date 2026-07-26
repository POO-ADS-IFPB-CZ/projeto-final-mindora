package mindora.model;

public class Atividade {
    private Long id;
    private String titulo;
    private String tipo;
    private String descricao;
    private String nivel; // 'basico', 'intermediario', 'avancado'

    public Atividade() {
    }

    public Atividade(String titulo, String tipo, String descricao, String nivel) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public Atividade(Long id, String titulo, String tipo, String descricao, String nivel) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return titulo;
    }
}