package mindora.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import mindora.dao.AlunoDAO;
import mindora.dao.AtividadeDAO;
import mindora.dao.ProfissionalDAO;
import mindora.dao.SessaoDAO;
import mindora.model.Aluno;
import mindora.model.Atividade;
import mindora.model.Profissional;
import mindora.model.Sessao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessaoView extends VBox {

    private ComboBox<Aluno> cbAluno = new ComboBox<>();
    private ComboBox<Profissional> cbProfissional = new ComboBox<>();
    private DatePicker dpData = new DatePicker();
    private TextField txtDuracao = new TextField();
    private ComboBox<String> cbStatus = new ComboBox<>();
    private TextField txtNota = new TextField();

    private ListView<Atividade> listAtividades = new ListView<>();

    private TableView<Sessao> tabela = new TableView<>();
    private ObservableList<Sessao> listaSessoes = FXCollections.observableArrayList();

    private SessaoDAO sessaoDAO = new SessaoDAO();
    private AlunoDAO alunoDAO = new AlunoDAO();
    private ProfissionalDAO profissionalDAO = new ProfissionalDAO();
    private AtividadeDAO atividadeDAO = new AtividadeDAO();

    private Sessao sessaoSelecionada = null;

    public SessaoView() {
        setSpacing(10);
        setPadding(new Insets(15));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        cbStatus.getItems().addAll("agendada", "realizada", "cancelada");

        cbAluno.setPrefWidth(220);
        cbProfissional.setPrefWidth(220);
        dpData.setPrefWidth(220);
        txtDuracao.setPrefWidth(100);
        cbStatus.setPrefWidth(220);
        txtNota.setPrefWidth(100);

        listAtividades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listAtividades.setPrefHeight(90);
        listAtividades.setPrefWidth(220);

        Button btnRecarregarCombo = new Button("🔄");
        btnRecarregarCombo.setTooltip(new Tooltip("Recarregar Alunos, Profissionais e Atividades"));
        btnRecarregarCombo.setOnAction(e -> carregarCombos());

        grid.add(new Label("Aluno:"), 0, 0);
        HBox boxAluno = new HBox(5, cbAluno, btnRecarregarCombo);
        grid.add(boxAluno, 1, 0);

        grid.add(new Label("Profissional:"), 0, 1);
        grid.add(cbProfissional, 1, 1);

        grid.add(new Label("Data da Sessão:"), 0, 2);
        grid.add(dpData, 1, 2);

        grid.add(new Label("Duração (minutos):"), 0, 3);
        grid.add(txtDuracao, 1, 3);

        grid.add(new Label("Status:"), 0, 4);
        grid.add(cbStatus, 1, 4);

        grid.add(new Label("Nota Geral da Sessão (0 a 10):"), 0, 5);
        grid.add(txtNota, 1, 5);

        grid.add(new Label("Atividades (Opcional):"), 0, 6);
        grid.add(listAtividades, 1, 6);

        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");
        Button btnAtualizarTudo = new Button("🔄 Atualizar Dados");

        HBox boxBotoes = new HBox(10, btnSalvar, btnExcluir, btnLimpar, btnAtualizarTudo);

        TableColumn<Sessao, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Sessao, LocalDate> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Sessao, Integer> colDuracao = new TableColumn<>("Duração (min)");
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMin"));

        TableColumn<Sessao, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Sessao, Double> colNota = new TableColumn<>("Nota");
        colNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        TableColumn<Sessao, String> colAluno = new TableColumn<>("Aluno");
        colAluno.setCellValueFactory(new PropertyValueFactory<>("alunoNome"));

        TableColumn<Sessao, String> colProf = new TableColumn<>("Profissional");
        colProf.setCellValueFactory(new PropertyValueFactory<>("profissionalNome"));

        TableColumn<Sessao, String> colAtiv = new TableColumn<>("Atividades");
        colAtiv.setCellValueFactory(new PropertyValueFactory<>("atividadesTitulos"));
        colAtiv.setPrefWidth(200);

        tabela.getColumns().addAll(colId, colData, colDuracao, colStatus, colNota, colAluno, colProf, colAtiv);
        tabela.setItems(listaSessoes);

        VBox.setVgrow(tabela, Priority.ALWAYS);

        getChildren().addAll(new Label("🧠 Gestão de Sessões"), grid, boxBotoes, tabela);

        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
        btnLimpar.setOnAction(e -> limparCampos());
        btnAtualizarTudo.setOnAction(e -> recarregarTudo());

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                sessaoSelecionada = novo;
                dpData.setValue(novo.getData());
                txtDuracao.setText(String.valueOf(novo.getDuracaoMin()));
                cbStatus.setValue(novo.getStatus());
                txtNota.setText(novo.getNota() != null ? String.valueOf(novo.getNota()) : "");

                for (Aluno a : cbAluno.getItems()) {
                    if (a.getId().equals(novo.getAlunoId())) {
                        cbAluno.setValue(a);
                        break;
                    }
                }

                for (Profissional p : cbProfissional.getItems()) {
                    if (p.getId().equals(novo.getProfissionalId())) {
                        cbProfissional.setValue(p);
                        break;
                    }
                }

                listAtividades.getSelectionModel().clearSelection();
                try {
                    List<Long> ativIds = sessaoDAO.buscarAtividadesIdsPorSessao(novo.getId());
                    for (int i = 0; i < listAtividades.getItems().size(); i++) {
                        Atividade ativ = listAtividades.getItems().get(i);
                        if (ativIds.contains(ativ.getId())) {
                            listAtividades.getSelectionModel().select(i);
                        }
                    }
                } catch (SQLException e) {
                    mostrarAlerta("Erro", "Erro ao carregar atividades da sessão: " + e.getMessage());
                }
            }
        });

        recarregarTudo();
        limparCampos();
    }

    public void recarregarTudo() {
        carregarCombos();
        carregarDados();
    }

    public void carregarCombos() {
        try {
            cbAluno.setItems(FXCollections.observableArrayList(alunoDAO.listarTodos()));
            cbProfissional.setItems(FXCollections.observableArrayList(profissionalDAO.listarTodos()));
            listAtividades.setItems(FXCollections.observableArrayList(atividadeDAO.listarTodos()));
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar listas de seleção: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            listaSessoes.setAll(sessaoDAO.listarTodos());
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao carregar sessões: " + e.getMessage());
        }
    }

    private void salvar() {
        if (cbAluno.getValue() == null || cbProfissional.getValue() == null || dpData.getValue() == null || txtDuracao.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Preencha Aluno, Profissional, Data e Duração.");
            return;
        }

        int duracao;
        try {
            duracao = Integer.parseInt(txtDuracao.getText().trim());
            if (duracao <= 0) {
                mostrarAlerta("Aviso", "A duração deve ser maior que 0.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Aviso", "Informe um valor numérico válido para a duração.");
            return;
        }

        Double nota = null;
        if (!txtNota.getText().trim().isEmpty()) {
            try {
                nota = Double.parseDouble(txtNota.getText().trim().replace(",", "."));
                if (nota < 0 || nota > 10) {
                    mostrarAlerta("Aviso", "A nota deve estar entre 0 e 10.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Aviso", "Informe uma nota válida (ex: 8.5).");
                return;
            }
        }

        List<Long> ativIdsSelecionadas = new ArrayList<>();
        for (Atividade ativ : listAtividades.getSelectionModel().getSelectedItems()) {
            ativIdsSelecionadas.add(ativ.getId());
        }

        try {
            if (sessaoSelecionada == null) {
                Sessao nova = new Sessao(
                        dpData.getValue(),
                        duracao,
                        cbStatus.getValue(),
                        nota,
                        cbAluno.getValue().getId(),
                        cbProfissional.getValue().getId()
                );
                sessaoDAO.salvar(nova, ativIdsSelecionadas);
            } else {
                sessaoSelecionada.setData(dpData.getValue());
                sessaoSelecionada.setDuracaoMin(duracao);
                sessaoSelecionada.setStatus(cbStatus.getValue());
                sessaoSelecionada.setNota(nota);
                sessaoSelecionada.setAlunoId(cbAluno.getValue().getId());
                sessaoSelecionada.setProfissionalId(cbProfissional.getValue().getId());

                sessaoDAO.atualizar(sessaoSelecionada, ativIdsSelecionadas);
            }
            recarregarTudo();
            limparCampos();
            mostrarAlerta("Sucesso", "Sessão salva com sucesso!");
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao salvar sessão: " + e.getMessage());
        }
    }

    private void excluir() {
        if (sessaoSelecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma sessão na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir a sessão de ID " + sessaoSelecionada.getId() + "?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                sessaoDAO.deletar(sessaoSelecionada.getId());
                recarregarTudo();
                limparCampos();
                mostrarAlerta("Sucesso", "Sessão excluída com sucesso!");
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao excluir sessão: " + e.getMessage());
            }
        }
    }

    private void limparCampos() {
        sessaoSelecionada = null;

        cbAluno.getSelectionModel().clearSelection();
        cbAluno.setValue(null);

        cbProfissional.getSelectionModel().clearSelection();
        cbProfissional.setValue(null);

        dpData.setValue(null);
        txtDuracao.clear();
        cbStatus.getSelectionModel().clearSelection();
        cbStatus.setValue(null);
        txtNota.clear();

        listAtividades.getSelectionModel().clearSelection();
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