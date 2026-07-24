package mindora;

public class AppLauncher {
    public static void main(String[] args) {
        Main.main(args);
    }
}

/*A partir do Java 11, o JavaFX deixou de fazer parte do JDK padrão e passou a ser uma biblioteca modular externa (gerenciada por ferramentas como o Maven).

Quando executa o mét0do main diretamente dentro de uma classe que estende javafx.application.Application (como a classe Main), acontece o seguinte:

A JVM verifica se os módulos nativos do JavaFX já foram carregados pelo sistema operacional antes mesmo de executar a primeira linha de código.

Se a aplicação não for iniciada com parâmetros avançados de linha de comando (VM Options / --module-path), a JVM bloqueia o programa e lança o erro:

Error: JavaFX runtime components are missing, and are required to run this application

Ao usar o Launcher:

Bypass na verificação inicial: Como a classe AppLauncher é uma classe Java comum (não estende Application),
 a JVM inicia a aplicação normalmente como qualquer programa Java tradicional.
Carregamento pelo Maven: Durante a inicialização, o Maven disponibiliza automaticamente todas as bibliotecas do JavaFX listadas no seu pom.xml na memória (classpath).
Execução bem-sucedida: Quando o mét0do AppLauncher.main chama o Main.main(args), todas as dependências do JavaFX já estão carregadas e prontas na memória,
 permitindo que a interface gráfica abra sem erros.
*/