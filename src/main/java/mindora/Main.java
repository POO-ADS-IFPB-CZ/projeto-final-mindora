package mindora;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import mindora.view.AlunoView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Aba do Aluno
        Tab tabAlunos = new Tab("Alunos", new AlunoView());
        tabAlunos.setClosable(false);

        tabPane.getTabs().add(tabAlunos);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Mindora - Sistema de Gestão");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}