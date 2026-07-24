package mindora.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import mindora.dao.AlunoDAO;
import mindora.model.Aluno;

import java.sql.SQLException;
import java.time.LocalDate;

public class AlunoView extends VBox {

    // Componentes do Formulário
    private TextField txtNome = new TextField();
    private DatePicker dpDataNasc = new DatePicker();
    // Nível de suporte do aluno.
    private TextField txtNivelSup = new TextField();
    private TextField txtObservacao = new TextField();

    // Tabela e Dados
    private TableView<Aluno> tabela = new TableView<>();
    private ObservableList<Aluno> listaAlunos = FXCollections.observableArrayList();
    private AlunoDAO alunoDAO = new AlunoDAO();
    private Aluno alunoSelecionado = null;

    public AlunoView() {
        setSpacing(10);
        setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Data de Nascimento:"), 0, 1);
        grid.add(dpDataNasc, 1, 1);
        grid.add(new Label("Nível de Suporte:"), 0, 2);
        grid.add(txtNivelSup, 1, 2);
        grid.add(new Label("Observação:"), 0, 3);
        grid.add(txtObservacao, 1, 3);

        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        HBox boxBotoes = new HBox(10, btnSalvar, btnExcluir, btnLimpar);

        // Configuração das Colunas da Tabela
        TableColumn<Aluno, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Aluno, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Aluno, LocalDate> colData = new TableColumn<>("Data Nasc.");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));

        TableColumn<Aluno, String> colNivel = new TableColumn<>("Nível de Suporte");
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivelSup"));

        TableColumn<Aluno, String> colObs = new TableColumn<>("Observação");
        colObs.setCellValueFactory(new PropertyValueFactory<>("observacao"));

        tabela.getColumns().addAll(colId, colNome, colData, colNivel, colObs);
        tabela.setItems(listaAlunos);

        // Adiciona tudo ao VBox principal
        getChildren().addAll(new Label("📋 Gestão de Alunos"), grid, boxBotoes, tabela);


        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limparCampos());

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                alunoSelecionado = novo;
                txtNome.setText(novo.getNome());
                dpDataNasc.setValue(novo.getDataNasc());
                txtNivelSup.setText(novo.getNivelSup());
                txtObservacao.setText(novo.getObservacao());
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            listaAlunos.setAll(alunoDAO.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar alunos: " + e.getMessage());
        }
    }

    private void salvar() {
        if (txtNome.getText().isEmpty() || dpDataNasc.getValue() == null) {
            mostrarAlerta("Aviso", "Preencha o Nome e a Data de Nascimento.");
            return;
        }

        try {
            if (alunoSelecionado == null) {
                // Inserir Novo Aluno
                Aluno novo = new Aluno(txtNome.getText(), dpDataNasc.getValue(), txtNivelSup.getText(), txtObservacao.getText());
                alunoDAO.salvar(novo);
            } else {
                // Atualizar Existente
                alunoSelecionado.setNome(txtNome.getText());
                alunoSelecionado.setDataNasc(dpDataNasc.getValue());
                alunoSelecionado.setNivelSup(txtNivelSup.getText());
                alunoSelecionado.setObservacao(txtObservacao.getText());
                alunoDAO.atualizar(alunoSelecionado);
            }
            limparCampos();
            carregarDados();
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao salvar aluno: " + e.getMessage());
        }
    }

    private void excluir() {
        if (alunoSelecionado != null) {
            try {
                alunoDAO.deletar(alunoSelecionado.getId());
                limparCampos();
                carregarDados();
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao excluir aluno: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        alunoSelecionado = null;
        txtNome.clear();
        dpDataNasc.setValue(null);
        txtNivelSup.clear();
        txtObservacao.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}