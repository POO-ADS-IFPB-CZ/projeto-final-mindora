package mindora;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import mindora.view.AlunoView;
import mindora.view.ProfissionalView;
import mindora.view.ResponsavelView;
import mindora.view.AtividadeView;
import javafx.scene.image.Image;
import java.io.InputStream;

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

        // Aba 3: Responsáveis
        Tab tabResponsaveis = new Tab("Responsáveis", new ResponsavelView());
        tabResponsaveis.setClosable(false);

        // Aba 4: Atividades
        Tab tabAtividades = new Tab("Atividades", new AtividadeView());
        tabAtividades.setClosable(false);

        tabPane.getTabs().addAll(tabAlunos, tabProfissionais, tabResponsaveis, tabAtividades);

        Scene scene = new Scene(tabPane, 850, 600);

        InputStream iconStream = getClass().getResourceAsStream("/images/LogoMindora64px.jpg");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Aviso: Logo não encontrado em /images/LogoMindora64px.jpg");
        }

        primaryStage.setTitle("Mindora - Sistema de Gestão");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}