# Mindora - Sistema de Gestão e Acompanhamento de Alunos com TEA

O **Mindora** é uma aplicação desktop desenvolvida para auxiliar no acompanhamento e na gestão clínico-pedagógica de alunos com **TEA (Transtorno do Espectro Autista)**. O sistema permite o cadastro centralizado de **Alunos**, **Responsáveis**, **Profissionais Multidisciplinares**, **Atividades** e o agendamento/registro de **Sessões de Atendimento**.

Projeto desenvolvido como requisito de avaliação da disciplina de **Programação Orientada a Objetos (POO)**.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Interface Gráfica:** JavaFX 21
- **Gerenciador de Dependências:** Apache Maven
- **Banco de Dados:** PostgreSQL
- **Persistência de Dados:** Java Database Connectivity (JDBC)
- **Arquitetura:** Model-View-Controller (MVC) + Data Access Object (DAO)

---

<details>
  <summary><b>📸 Clique aqui para ver a interface do sistema</b></summary>
  <br>
  <p align="center">
    <img src="ImagensReadme/img.png" width="350" alt="Tela 1">
    <img src="ImagensReadme/img_1.png" width="350" alt="Tela 2">
  </p>
  <p align="center">
    <img src="ImagensReadme/img_2.png" width="350" alt="Tela 3">
    <img src="ImagensReadme/img_3.png" width="350" alt="Tela 4">
  </p>
  <p align="center">
    <img src="ImagensReadme/img_4.png" width="350" alt="Tela 5">
  </p>
</details>

---

## 📂 Estrutura do Projeto

```text
mindora/
├── sql/
│   ├── 01_create_database.sql
│   └── 02_create_tables.sql
├── src/
│   └── main/
│       ├── java/
│       │   └── mindora/
│       │       ├── config/
│       │       │   └── ConnectionFactory.java
│       │       ├── dao/
│       │       │   ├── AlunoDAO.java
│       │       │   ├── AtividadeDAO.java
│       │       │   ├── ProfissionalDAO.java
│       │       │   ├── ResponsavelDAO.java
│       │       │   └── SessaoDAO.java
│       │       ├── model/
│       │       │   ├── Aluno.java
│       │       │   ├── Atividade.java
│       │       │   ├── Profissional.java
│       │       │   ├── Responsavel.java
│       │       │   └── Sessao.java
│       │       ├── view/
│       │       │   ├── AlunoView.java
│       │       │   ├── AtividadeView.java
│       │       │   ├── ProfissionalView.java
│       │       │   ├── ResponsavelView.java
│       │       │   └── SessaoView.java
│       │       ├── Applauncher.java
│       │       └── Main.java
│       └── resources/
│           ├── images/
│           │   └── LogoMindora64px.jpg
│           ├── db.properties
│           └── db.properties.example
├── .gitignore
├── pom.xml
└── README.md
```

---

## 🚀 Como Executar o Projeto

### Pre-requisitos
Antes de começar, você precisará ter instalado em sua máquina:
- **Java JDK 21**
- **Apache Maven**
- **PostgreSQL**

### 1. Configuração do Banco de Dados
1. Abra o seu gerenciador de banco de dados (ex: pgAdmin) e crie um banco chamado `mindora`.
2. Execute os scripts SQL localizados na pasta `sql/` para criar a estrutura:
    - Primeiro execute o script `01_create_database.sql`.
    - Depois execute o script `02_create_tables.sql`.

### 2. Configuração das Credenciais
1. Vá até a pasta `src/main/resources/`.
2. Duplique o arquivo `db.properties.example` e renomeie a cópia para `db.properties`.
3. Abra o arquivo `db.properties` e insira o usuário e a senha do seu PostgreSQL local:
   ```properties
   db.url=jdbc:postgresql://localhost:5432/mindora
   db.user=seu_usuario
   db.password=sua_senha
   ```

### 3. Compilação e Execução
Abra o terminal na raiz do projeto e execute os comandos abaixo para compilar e iniciar a aplicação através do `Applauncher`:

```bash
# Baixar as dependências e compilar o projeto
mvn clean compile

# Executar a aplicação apontando para a classe principal
mvn exec:java -Dexec.mainClass="mindora.Applauncher"
```

> 📌 **Dica:** Você também pode importar o projeto em sua IDE de preferência (como IntelliJ IDEA ou Eclipse), aguardar o Maven carregar as dependências e executar diretamente o arquivo `src/main/java/mindora/Applauncher.java` clicando com o botão direito e selecionando **Run**.

---
## 📝 Licença e Autoria

Este projeto foi idealizado e desenvolvido por **João Victor Batista de Araújo Abrantes** como requisito de avaliação da disciplina de **Programação Orientada a Objetos (POO)**.

Copyright © 2026 **[JotaveHub]**. Todos os direitos reservados.
