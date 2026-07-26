package mindora.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import mindora.dao.AtividadeDAO;
import mindora.model.Atividade;

import java.sql.SQLException;

public class AtividadeView extends VBox {

    private TextField txtTitulo = new TextField();
    private TextField txtTipo = new TextField();
    private ComboBox<String> cbNivel = new ComboBox<>();
    private TextField txtDescricao = new TextField();

    private TableView<Atividade> tabela = new TableView<>();
    private ObservableList<Atividade> listaAtividades = FXCollections.observableArrayList();
    private AtividadeDAO atividadeDAO = new AtividadeDAO();
    private Atividade atividadeSelecionada = null;

    public AtividadeView() {
        setSpacing(10);
        setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        cbNivel.getItems().addAll("basico", "intermediario", "avancado");

        grid.add(new Label("Título:"), 0, 0);
        grid.add(txtTitulo, 1, 0);
        grid.add(new Label("Tipo/Categoria:"), 0, 1);
        grid.add(txtTipo, 1, 1);
        grid.add(new Label("Nível:"), 0, 2);
        grid.add(cbNivel, 1, 2);
        grid.add(new Label("Descrição:"), 0, 3);
        grid.add(txtDescricao, 1, 3);

        txtTitulo.setPrefWidth(250);
        txtTipo.setPrefWidth(250);
        txtDescricao.setPrefWidth(250);

        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        HBox boxBotoes = new HBox(10, btnSalvar, btnExcluir, btnLimpar);

        TableColumn<Atividade, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Atividade, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Atividade, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Atividade, String> colNivel = new TableColumn<>("Nível");
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));

        TableColumn<Atividade, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabela.getColumns().addAll(colId, colTitulo, colTipo, colNivel, colDesc);
        tabela.setItems(listaAtividades);

        getChildren().addAll(new Label("🧩 Gestão de Atividades"), grid, boxBotoes, tabela);

        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limparCampos());

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                atividadeSelecionada = novo;
                txtTitulo.setText(novo.getTitulo());
                txtTipo.setText(novo.getTipo());
                cbNivel.setValue(novo.getNivel());
                txtDescricao.setText(novo.getDescricao() != null ? novo.getDescricao() : "");
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            listaAtividades.setAll(atividadeDAO.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar atividades: " + e.getMessage());
        }
    }

    private void salvar() {
        if (txtTitulo.getText().isEmpty() || txtTipo.getText().isEmpty() || cbNivel.getValue() == null) {
            mostrarAlerta("Aviso", "Preencha Título, Tipo e selecione o Nível.");
            return;
        }

        try {
            if (atividadeSelecionada == null) {
                Atividade nova = new Atividade(txtTitulo.getText(), txtTipo.getText(), txtDescricao.getText(), cbNivel.getValue());
                atividadeDAO.salvar(nova);
            } else {
                atividadeSelecionada.setTitulo(txtTitulo.getText());
                atividadeSelecionada.setTipo(txtTipo.getText());
                atividadeSelecionada.setNivel(cbNivel.getValue());
                atividadeSelecionada.setDescricao(txtDescricao.getText());
                atividadeDAO.atualizar(atividadeSelecionada);
            }
            limparCampos();
            carregarDados();
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao salvar atividade: " + e.getMessage());
        }
    }

    private void excluir() {
        if (atividadeSelecionada != null) {
            try {
                atividadeDAO.deletar(atividadeSelecionada.getId());
                limparCampos();
                carregarDados();
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao excluir atividade: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        atividadeSelecionada = null;
        txtTitulo.clear();
        txtTipo.clear();
        cbNivel.setValue(null);
        txtDescricao.clear();
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