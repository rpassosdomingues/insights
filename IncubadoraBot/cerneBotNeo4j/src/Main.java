package src;

import org.neo4j.driver.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // Classe interna para conectar ao Neo4j
    static class Neo4jConnector {
        private final Driver driver;

        // Construtor que inicializa a conexão com o banco de dados Neo4j
        public Neo4jConnector(String uri, String user) {
            // Lê a senha do Neo4j da variável de ambiente
            String password = System.getenv("NEO4J_PASSWORD");
            if (password == null) {
                throw new IllegalStateException("A variável de ambiente NEO4J_PASSWORD não está definida!");
            }
            this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        }

        // Método para retornar a sessão do Neo4j
        public Session getSession() {
            return driver.session();
        }

        // Método para fechar a conexão com o banco de dados
        public void close() {
            driver.close();
        }
    }

    // Método para adicionar uma palavra ao banco
    private void addWord(Neo4jConnector neo4jConnector, String word, Label resultLabel) {
        try (Session session = neo4jConnector.getSession()) {
            // Converter a palavra para minúsculas para garantir que não seja duplicada
            word = word.toLowerCase();

            // Consultar se a palavra já existe
            String queryCheck = "MATCH (w:Word {name: $name}) RETURN w";
            Result resultCheck = session.run(queryCheck, Values.parameters("name", word));
            
            if (resultCheck.hasNext()) {
                // Se a palavra já existe
                resultLabel.setText("A palavra '" + word + "' já existe.");
            } else {
                // Adicionar a palavra ao banco
                String queryAdd = "CREATE (w:Word {name: $name})";
                session.run(queryAdd, Values.parameters("name", word));
                resultLabel.setText("Palavra '" + word + "' adicionada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erro ao adicionar a palavra.");
        }
    }

    // Método para remover uma palavra do banco
    private void removeWord(Neo4jConnector neo4jConnector, String word, Label resultLabel) {
        try (Session session = neo4jConnector.getSession()) {
            // Converter a palavra para minúsculas para garantir que a remoção seja insensível a maiúsculas/minúsculas
            word = word.toLowerCase();

            // Consultar se a palavra existe
            String queryCheck = "MATCH (w:Word {name: $name}) RETURN w";
            Result resultCheck = session.run(queryCheck, Values.parameters("name", word));
            
            if (resultCheck.hasNext()) {
                // Se a palavra existe, apagá-la
                String queryDelete = "MATCH (w:Word {name: $name}) DELETE w";
                session.run(queryDelete, Values.parameters("name", word));
                resultLabel.setText("Palavra '" + word + "' removida.");
            } else {
                // Se a palavra não for encontrada
                resultLabel.setText("Palavra '" + word + "' não encontrada para remoção.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erro ao remover a palavra.");
        }
    }

    // Método para buscar palavras no banco
    private void searchWord(Neo4jConnector neo4jConnector, String word, Label resultLabel) {
        try (Session session = neo4jConnector.getSession()) {
            // Converter a palavra para minúsculas para garantir busca insensível a maiúsculas/minúsculas
            word = word.toLowerCase();

            // Consulta para verificar se a palavra existe
            String query = "MATCH (w:Word {name: $name}) RETURN w";
            Result result = session.run(query, Values.parameters("name", word));
            
            // Verificando se o resultado tem o nó
            if (result.hasNext()) {
                resultLabel.setText("Palavra '" + word + "' encontrada.");
            } else {
                resultLabel.setText("Palavra '" + word + "' não encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Erro ao buscar a palavra.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Conectar ao Neo4j usando a variável de ambiente para a senha
        Neo4jConnector neo4jConnector = new Neo4jConnector("bolt://localhost:7687", "neo4j");

        // Campos de texto para digitar palavras
        TextField wordInput = new TextField();
        wordInput.setPromptText("Digite a palavra");
        wordInput.setVisible(false); // Inicialmente invisível

        // Rótulo para exibir o resultado da operação
        Label resultLabel = new Label();

        // Botões de operação
        Button addButton = new Button("Adicionar Palavra");
        Button removeButton = new Button("Remover Palavra");
        Button searchButton = new Button("Buscar Palavra");

        // Variável para controlar qual operação está sendo feita
        final String[] operation = {""};

        // Definir o comportamento dos botões
        addButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText(""); // Limpar o resultado
            operation[0] = "add";  // Definir a operação como "adicionar"
            enableButtons(addButton, removeButton, searchButton, false);  // Desabilitar botões
        });

        removeButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText(""); // Limpar o resultado
            operation[0] = "remove";  // Definir a operação como "remover"
            enableButtons(addButton, removeButton, searchButton, false);  // Desabilitar botões
        });

        searchButton.setOnAction(e -> {
            wordInput.setVisible(true);
            resultLabel.setText(""); // Limpar o resultado
            operation[0] = "search";  // Definir a operação como "buscar"
            enableButtons(addButton, removeButton, searchButton, false);  // Desabilitar botões
        });

        // Quando o usuário pressiona enter no campo de texto, realiza a operação
        wordInput.setOnAction(e -> {
            String word = wordInput.getText();
            if (!word.isEmpty()) {
                if ("add".equals(operation[0])) {
                    addWord(neo4jConnector, word, resultLabel);
                } else if ("remove".equals(operation[0])) {
                    removeWord(neo4jConnector, word, resultLabel);
                } else if ("search".equals(operation[0])) {
                    searchWord(neo4jConnector, word, resultLabel);
                }
                wordInput.clear();
                wordInput.setVisible(false); // Ocultar o campo de texto após operação
                enableButtons(addButton, removeButton, searchButton, true); // Reabilitar botões
            }
        });

        // Layout da interface gráfica
        VBox layout = new VBox(10, addButton, removeButton, searchButton, wordInput, resultLabel);
        layout.setAlignment(Pos.CENTER);

        // Configuração da cena
        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setTitle("Neo4j Word Operations");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para habilitar ou desabilitar os botões de operação
    private void enableButtons(Button addButton, Button removeButton, Button searchButton, boolean enable) {
        addButton.setDisable(!enable);
        removeButton.setDisable(!enable);
        searchButton.setDisable(!enable);
    }

    public static void main(String[] args) {
        // Iniciar a aplicação JavaFX
        launch(args);
    }
}
