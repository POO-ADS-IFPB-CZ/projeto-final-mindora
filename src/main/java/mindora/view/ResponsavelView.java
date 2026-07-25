package mindora.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import mindora.dao.ResponsavelDAO;
import mindora.model.Responsavel;

import java.sql.SQLException;

public class ResponsavelView extends VBox {

    private TextField txtNome = new TextField();
    private TextField txtEmail = new TextField();

    private TableView<Responsavel> tabela = new TableView<>();
    private ObservableList<Responsavel> listaResponsaveis = FXCollections.observableArrayList();
    private ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    private Responsavel responsavelSelecionado = null;

    public ResponsavelView() {
        setSpacing(10);
        setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("E-mail:"), 0, 1);
        grid.add(txtEmail, 1, 1);

        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        HBox boxBotoes = new HBox(10, btnSalvar, btnExcluir, btnLimpar);

        TableColumn<Responsavel, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Responsavel, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Responsavel, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tabela.getColumns().addAll(colId, colNome, colEmail);
        tabela.setItems(listaResponsaveis);

        getChildren().addAll(new Label("👨‍👩‍👧 Gestão de Responsáveis"), grid, boxBotoes, tabela);

        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limparCampos());

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                responsavelSelecionado = novo;
                txtNome.setText(novo.getNome());
                txtEmail.setText(novo.getEmail());
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            listaResponsaveis.setAll(responsavelDAO.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar responsáveis: " + e.getMessage());
        }
    }

    private void salvar() {
        if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Preencha o Nome e o E-mail.");
            return;
        }

        try {
            if (responsavelSelecionado == null) {
                Responsavel novo = new Responsavel(txtNome.getText(), txtEmail.getText());
                responsavelDAO.salvar(novo);
            } else {
                responsavelSelecionado.setNome(txtNome.getText());
                responsavelSelecionado.setEmail(txtEmail.getText());
                responsavelDAO.atualizar(responsavelSelecionado);
            }
            limparCampos();
            carregarDados();
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao salvar responsável: " + e.getMessage());
        }
    }

    private void excluir() {
        if (responsavelSelecionado != null) {
            try {
                responsavelDAO.deletar(responsavelSelecionado.getId());
                limparCampos();
                carregarDados();
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao excluir responsável: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        responsavelSelecionado = null;
        txtNome.clear();
        txtEmail.clear();
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