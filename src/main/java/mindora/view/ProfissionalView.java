package mindora.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import mindora.dao.ProfissionalDAO;
import mindora.model.Profissional;

import java.sql.SQLException;

public class ProfissionalView extends VBox {

    private TextField txtNome = new TextField();
    private TextField txtEspecialidade = new TextField();
    private TextField txtRegistro = new TextField();

    private TableView<Profissional> tabela = new TableView<>();
    private ObservableList<Profissional> listaProfissionais = FXCollections.observableArrayList();

    private ProfissionalDAO profissionalDAO = new ProfissionalDAO();
    private Profissional profissionalSelecionado = null;

    public ProfissionalView() {
        setSpacing(10);
        setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);

        grid.add(new Label("Especialidade:"), 0, 1);
        grid.add(txtEspecialidade, 1, 1);

        grid.add(new Label("Registro Profissional:"), 0, 2);
        grid.add(txtRegistro, 1, 2);

        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        HBox boxBotoes = new HBox(10, btnSalvar, btnExcluir, btnLimpar);

        TableColumn<Profissional, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Profissional, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Profissional, String> colEsp = new TableColumn<>("Especialidade");
        colEsp.setCellValueFactory(new PropertyValueFactory<>("especialidade"));

        TableColumn<Profissional, String> colReg = new TableColumn<>("Registro");
        colReg.setCellValueFactory(new PropertyValueFactory<>("registroProfissional"));

        tabela.getColumns().addAll(colId, colNome, colEsp, colReg);
        tabela.setItems(listaProfissionais);

        VBox.setVgrow(tabela, Priority.ALWAYS);

        getChildren().addAll(new Label("🩺 Gestão de Profissionais"), grid, boxBotoes, tabela);

        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limparCampos());

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                profissionalSelecionado = novo;
                txtNome.setText(novo.getNome());
                txtEspecialidade.setText(novo.getEspecialidade());
                txtRegistro.setText(novo.getRegistro());
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            listaProfissionais.setAll(profissionalDAO.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar profissionais: " + e.getMessage());
        }
    }

    private void salvar() {
        if (txtNome.getText().isEmpty() || txtEspecialidade.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Preencha o Nome e a Especialidade.");
            return;
        }

        try {
            if (profissionalSelecionado == null) {
                Profissional novo = new Profissional(txtNome.getText(), txtEspecialidade.getText(), txtRegistro.getText());
                profissionalDAO.salvar(novo);
            } else {
                profissionalSelecionado.setNome(txtNome.getText());
                profissionalSelecionado.setEspecialidade(txtEspecialidade.getText());
                profissionalSelecionado.setRegistro(txtRegistro.getText());
                profissionalDAO.atualizar(profissionalSelecionado);
            }
            limparCampos();
            carregarDados();
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao salvar profissional: " + e.getMessage());
        }
    }

    private void excluir() {
        if (profissionalSelecionado != null) {
            try {
                profissionalDAO.deletar(profissionalSelecionado.getId());
                limparCampos();
                carregarDados();
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao excluir profissional: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        profissionalSelecionado = null;
        txtNome.clear();
        txtEspecialidade.clear();
        txtRegistro.clear();
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