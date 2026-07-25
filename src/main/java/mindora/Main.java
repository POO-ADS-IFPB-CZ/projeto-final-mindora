package mindora;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import mindora.view.AlunoView;
import mindora.view.ProfissionalView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Aba 1: Alunos
        Tab tabAlunos = new Tab("Alunos", new AlunoView());
        tabAlunos.setClosable(false);

        // Aba 2: Profissionais
        Tab tabProfissionais = new Tab("Profissionais", new ProfissionalView());
        tabProfissionais.setClosable(false);

        tabPane.getTabs().addAll(tabAlunos, tabProfissionais);

        Scene scene = new Scene(tabPane, 850, 600);
        primaryStage.setTitle("Mindora - Sistema de Gestão");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}