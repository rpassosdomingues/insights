package src;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainGUI extends Application {
    private ArrayList<SolicitacaoPecas> pecas; // Inicializando a lista de solicitações
    private VBox controlPanel;
    private VBox subMenuPanel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pecas = new ArrayList<>(); // Inicializando a lista
        primaryStage.setTitle("CERNEBot");

        SplitPane splitPane = new SplitPane(); // Usando SplitPane para dividir a janela
        // Definindo a posição do divisor (0.35 significa 35% da largura para controlPanel)
        splitPane.setDividerPositions(0.35);

        controlPanel = new VBox(10);
        controlPanel.setPadding(new Insets(15));

        subMenuPanel = new VBox(); // Área para exibir os detalhes
        subMenuPanel.setPadding(new Insets(15));
        subMenuPanel.setSpacing(10);

        // Criando os botões
        criarBotoes();

        splitPane.getItems().addAll(controlPanel, subMenuPanel);

        // Adicionando o SplitPane à cena
        Scene scene = new Scene(splitPane, 1200, 600); // Defina o tamanho da janela conforme necessário
        primaryStage.setScene(scene);
        primaryStage.show(); // Mostra a janela
    }

    private void criarBotoes() {
        // Definindo os botões e adicionando ao painel de controle
        adicionarBotao("Cadastrar Projeto", e -> abrirCadastroProjeto());
        adicionarBotao("Agendar Evento", e -> abrirAgendarEvento());
        adicionarBotao("Registrar Rodada de Monitoramento", e -> abrirMonitoramento());
        adicionarBotao("Reservar Sala", e -> abrirReservaSala());
        adicionarBotao("Solicitar Fabricação de Peça", e -> solicitarFabricacaoPecas());
        adicionarBotao("Sair", e -> System.exit(0));
    }

    private void adicionarBotao(String texto, EventHandler<ActionEvent> evento) {
        Button button = new Button(texto);
        button.setOnAction(evento);
        HBox hBox = new HBox(button);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));
        controlPanel.getChildren().add(hBox);
    }

    // Janela de Cadastro de Projeto
    private void abrirCadastroProjeto() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding
    
        Label titleLabel = new Label("Cadastrar Projeto");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        // ComboBox para seleção de projetos
        Label lblProjetos = new Label("Selecione um Projeto:");
        ComboBox<CAEX> cmbProjetos = new ComboBox<>();
        cmbProjetos.getItems().addAll(CAEX.values()); // Adiciona todos os projetos

        // Campo para descrição do projeto
        Label lblDescricao = new Label("Descrição do Projeto:");
        TextField txtDescricao = new TextField();

        // Campo para data de início do projeto
        Label lblDataInicio = new Label("Data de Início:");
        DatePicker txtDataInicio = new DatePicker();

        // Campo para data de término do projeto
        Label lblDataTermino = new Label("Data de Término:");
        DatePicker txtDataTermino = new DatePicker();

        // Botão para salvar o projeto
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String descricao = txtDescricao.getText();
            String dataInicio = (txtDataInicio != null) ? txtDataInicio.getValue().toString() : "";
            String dataTermino = (txtDataTermino != null) ? txtDataTermino.getValue().toString() : "";
            CAEX projetoSelecionado = cmbProjetos.getValue(); // Obtendo o projeto selecionado

            // Atualiza o subMenuPanel com a mensagem de sucesso
            Label successLabel = new Label("Projeto cadastrado com sucesso!\nProjeto: " + projetoSelecionado +
                                           "\nDescrição: " + descricao + 
                                           "\nData de Início: " + dataInicio + 
                                           "\nData de Término: " + dataTermino);
            successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Estilo opcional para destacar a mensagem
            subMenuPanel.getChildren().add(successLabel);
        }); 

        // Adiciona os elementos ao grid
        grid.add(lblProjetos, 0, 0);
        grid.add(cmbProjetos, 1, 0);
        grid.add(lblDescricao, 0, 1);
        grid.add(txtDescricao, 1, 1);
        grid.add(lblDataInicio, 0, 2);
        grid.add(txtDataInicio, 1, 2);
        grid.add(lblDataTermino, 0, 3); // Adiciona rótulo para a data de término
        grid.add(txtDataTermino, 1, 3); // Adiciona campo para a data de término
        grid.add(btnSalvar, 1, 4);

        subMenuPanel.getChildren().add(grid); // Adiciona o grid à área de subMenu
    }

    // Janela de Agendamento de Evento
    private void abrirAgendarEvento() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding
        
        Label titleLabel = new Label("Agendar Evento");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblNome = new Label("Nome do Evento:");
        TextField txtNome = new TextField();

        Label lblDetalhes = new Label("Detalhes do Evento:");
        TextField txtDetalhes = new TextField();

        Label lblData = new Label("Data do Evento:");
        DatePicker dataEvento = new DatePicker();

        Label lblHoraInicio = new Label("Hora de Início:");
        ComboBox<Integer> comboHoraInicio = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            comboHoraInicio.getItems().add(i);
        }

        ComboBox<Integer> comboMinutoInicio = new ComboBox<>();
        for (int i = 0; i < 60; i += 5) {
            comboMinutoInicio.getItems().add(i);
        }

        Label lblHoraFim = new Label("Hora de Fim:");
        ComboBox<Integer> comboHoraFim = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            comboHoraFim.getItems().add(i);
        }

        ComboBox<Integer> comboMinutoFim = new ComboBox<>();
        for (int i = 0; i < 60; i += 5) {
            comboMinutoFim.getItems().add(i);
        }

        Label lblLocal = new Label("Local do Evento:");
        TextField txtLocal = new TextField();

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Capturando as informações do evento
            String nomeEvento = txtNome.getText();
            String detalhesEvento = txtDetalhes.getText();
            LocalDate data = dataEvento.getValue();

            Integer horaInicio = comboHoraInicio.getValue();
            Integer minutoInicio = comboMinutoInicio.getValue();
            String horarioInicioStr = String.format("%02d:%02d", horaInicio, minutoInicio);

            Integer horaFim = comboHoraFim.getValue();
            Integer minutoFim = comboMinutoFim.getValue();
            String horarioFimStr = String.format("%02d:%02d", horaFim, minutoFim);

            String localEvento = txtLocal.getText();

            // Exibindo confirmação
            Label successLabel = new Label("Evento cadastrado com sucesso!\n" +
                            "Nome: " + nomeEvento + "\n" +
                            "Detalhes: " + detalhesEvento + "\n" +
                            "Data: " + data + "\n" +
                            "Início: " + horarioInicioStr + "\n" +
                            "Fim: " + horarioFimStr + "\n" +
                            "Local: " + localEvento);
            successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Estilo opcional para destacar a mensagem
            subMenuPanel.getChildren().add(successLabel);
        });

        // Adicionando os componentes ao GridPane
        grid.add(lblNome, 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(lblDetalhes, 0, 1);
        grid.add(txtDetalhes, 1, 1);
        grid.add(lblData, 0, 2);
        grid.add(dataEvento, 1, 2);
        grid.add(lblHoraInicio, 0, 3);
        grid.add(comboHoraInicio, 1, 3);
        grid.add(comboMinutoInicio, 2, 3);
        grid.add(lblHoraFim, 0, 4);
        grid.add(comboHoraFim, 1, 4);
        grid.add(comboMinutoFim, 2, 4);
        grid.add(lblLocal, 0, 5);
        grid.add(txtLocal, 1, 5);
        grid.add(btnSalvar, 1, 6);

        subMenuPanel.getChildren().add(grid); // Adiciona o grid à área de subMenu
    }

    // Janela de Registro de Monitoramento
    private void abrirMonitoramento() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding

        Label titleLabel = new Label("Registrar Rodada de Monitoramento");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label lblEmpresa = new Label("Escolha a empresa:");
        Button btnPolygon = new Button("Polygon");
        Button btnICRO = new Button("ICRO Digital");
        Button btnSMART = new Button("SMARTComerci");
        Button btnILegis = new Button("iLegis");

        btnPolygon.setOnAction(e -> abrirDetalhesMonitoramento("Polygon"));
        btnICRO.setOnAction(e -> abrirDetalhesMonitoramento("ICRO Digital"));
        btnSMART.setOnAction(e -> abrirDetalhesMonitoramento("SMARTComerci"));
        btnILegis.setOnAction(e -> abrirDetalhesMonitoramento("iLegis"));

        vbox.getChildren().addAll(lblEmpresa, btnPolygon, btnICRO, btnSMART, btnILegis);

        subMenuPanel.getChildren().add(vbox); // Adiciona o VBox à área de subMenu
    }

    // Submenu de Detalhes de Monitoramento
    private void abrirDetalhesMonitoramento(String empresa) {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding

        Label titleLabel = new Label("Monitoramento " + empresa);
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblDataHora = new Label("Data e Hora:");
        DatePicker dataHora = new DatePicker();

        Label lblDocumento = new Label("Upload de Documento:");
        Button btnUpload = new Button("Upload PDF");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Exibindo confirmação
            Label successLabel = new Label("Monitoramento para " + empresa + " registrado com sucesso!");
            successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Estilo opcional para destacar a mensagem
            subMenuPanel.getChildren().add(successLabel);
        });

        grid.add(lblDataHora, 0, 0);
        grid.add(dataHora, 1, 0);
        grid.add(lblDocumento, 0, 1);
        grid.add(btnUpload, 1, 1);
        grid.add(btnSalvar, 1, 2);

        subMenuPanel.getChildren().add(grid); // Adiciona o grid à área de subMenu
    }

    // Janela de Reserva de Sala
    private void abrirReservaSala() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding

        Label titleLabel = new Label("Reserva de Sala");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Button btnReuniao = new Button("Sala de Reuniões");
        btnReuniao.setOnAction(e -> abrirSalaReunioes());

        Button btnCoworking = new Button("Coworking");
        btnCoworking.setOnAction(e -> abrirCoworking());

        vbox.getChildren().addAll(btnReuniao, btnCoworking);

        subMenuPanel.getChildren().add(vbox); // Adiciona o vbox à área de subMenu
    }

    // Método para abrir a janela de Reserva de Sala de Reuniões
    private void abrirSalaReunioes() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding

        Label titleLabel = new Label("Reservar Sala de Reuniões");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblDataHoraInicio = new Label("Data e Hora de Início:");
        DatePicker dataHoraInicio = new DatePicker();

        Label lblDataHoraFim = new Label("Data e Hora de Fim:");
        DatePicker dataHoraFim = new DatePicker();

        Label lblTransmissao = new Label("Haverá transmissão ao vivo?");
        CheckBox checkTransmissao = new CheckBox();

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Exibindo confirmação
            Label successLabel = new Label("Reserva da Sala de Reuniões feita com sucesso!");
            successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Estilo opcional para destacar a mensagem
            subMenuPanel.getChildren().add(successLabel);
        });

        grid.add(lblDataHoraInicio, 0, 0);
        grid.add(dataHoraInicio, 1, 0);
        grid.add(lblDataHoraFim, 0, 1);
        grid.add(dataHoraFim, 1, 1);
        grid.add(lblTransmissao, 0, 2);
        grid.add(checkTransmissao, 1, 2);
        grid.add(btnSalvar, 1, 3);

        subMenuPanel.getChildren().add(grid); // Adiciona o grid à área de subMenu
    }

    // Método para abrir a janela de Reserva de Coworking
    private void abrirCoworking() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding

        Label titleLabel = new Label("Reservar Coworking");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblDataHoraInicio = new Label("Data e Hora de Início:");
        DatePicker dataHoraInicio = new DatePicker();

        Label lblDataHoraFim = new Label("Data e Hora de Fim:");
        DatePicker dataHoraFim = new DatePicker();

        Label lblProjetor = new Label("Vai precisar do Projetor?");
        CheckBox checkProjetor = new CheckBox();

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Exibindo confirmação
            Label successLabel = new Label("Reserva do Coworking feita com sucesso!");
            successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Estilo opcional para destacar a mensagem
            subMenuPanel.getChildren().add(successLabel);
        });

        grid.add(lblDataHoraInicio, 0, 0);
        grid.add(dataHoraInicio, 1, 0);
        grid.add(lblDataHoraFim, 0, 1);
        grid.add(dataHoraFim, 1, 1);
        grid.add(lblProjetor, 0, 2);
        grid.add(checkProjetor, 1, 2);
        grid.add(btnSalvar, 1, 3); // Corrigido para adicionar na linha correta

        subMenuPanel.getChildren().add(grid); // Adiciona o grid à área de subMenu
    }

    // Janela de Solicitação de Fabricação de Peça
    private void solicitarFabricacaoPecas() {
        subMenuPanel.getChildren().clear(); // Limpa a área de detalhes
        subMenuPanel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;"); // Adiciona uma borda e padding

        Label titleLabel = new Label("Solicitar Fabricação de Peça");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;"); // Estilo do título
        subMenuPanel.getChildren().add(titleLabel); // Adiciona o título ao painel

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lblSolicitante = new Label("Solicitante:");
        TextField txtSolicitante = new TextField();

        Label lblCampus = new Label("Campus:");
        ComboBox<String> cmbCampus = new ComboBox<>();
        cmbCampus.getItems().addAll("Alfenas", "Poços de Caldas", "Varginha"); // Adiciona opções de campus

        Label lblAreaOrigem = new Label("Área de Origem:");
        ComboBox<Departamento> cmbAreaOrigem = new ComboBox<>();
        cmbAreaOrigem.getItems().addAll(Departamento.values()); // Adiciona todos os departamentos

        Label lblMaterial = new Label("Material:");
        ComboBox<String> cmbMaterial = new ComboBox<>();
        cmbMaterial.getItems().addAll("ABS", "PLA", "MDF"); // Adiciona opções de material

        Label lblEquipamento = new Label("Equipamento:");
        ComboBox<String> cmbEquipamento = new ComboBox<>();
        cmbEquipamento.getItems().addAll("Impressora 3D", "Cortadora a Laser"); // Adiciona opções de equipamento

        Button btnEnviar = new Button("Enviar Solicitação");
        btnEnviar.setOnAction(e -> {
            String solicitante = txtSolicitante.getText();
            String campus = cmbCampus.getValue(); // Pega o valor selecionado no ComboBox
            Departamento areaOrigem = cmbAreaOrigem.getValue(); // Pega o departamento selecionado
            String material = cmbMaterial.getValue(); // Pega o material selecionado
            String equipamento = cmbEquipamento.getValue(); // Pega o equipamento selecionado
            LocalDateTime dataSolicitacao = LocalDateTime.now(); // Captura a data/hora da solicitação

            // Criação da solicitação de peça
            SolicitacaoPecas solicitacao = new SolicitacaoPecas(solicitante, campus, areaOrigem.name(), material, equipamento, dataSolicitacao, "Aprovado", "Em Andamento", "Concluído");
            pecas.add(solicitacao); // Adiciona à lista de solicitações

            // Exibindo confirmação com detalhes da solicitação
            Label successLabel = new Label("Solicitação enviada com sucesso!\n" +
                    "Solicitante: " + solicitante + "\n" +
                    "Campus: " + campus + "\n" +
                    "Área de Origem: " + areaOrigem.name() + "\n" +
                    "Material: " + material + "\n" +
                    "Equipamento: " + equipamento + "\n" +
                    "Data da Solicitação: " + dataSolicitacao.toLocalDate() + " " + dataSolicitacao.toLocalTime());
            successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Estilo opcional para destacar a mensagem
            subMenuPanel.getChildren().add(successLabel);
        });

        grid.add(lblSolicitante, 0, 0);
        grid.add(txtSolicitante, 1, 0);
        grid.add(lblCampus, 0, 1);
        grid.add(cmbCampus, 1, 1); // Adiciona ComboBox de campus ao grid
        grid.add(lblAreaOrigem, 0, 2);
        grid.add(cmbAreaOrigem, 1, 2); // Adiciona ComboBox de área de origem ao grid
        grid.add(lblMaterial, 0, 3);
        grid.add(cmbMaterial, 1, 3); // Adiciona ComboBox de material ao grid
        grid.add(lblEquipamento, 0, 4);
        grid.add(cmbEquipamento, 1, 4); // Adiciona ComboBox de equipamento ao grid
        grid.add(btnEnviar, 1, 5);

        subMenuPanel.getChildren().add(grid); // Adiciona o grid à área de subMenu
    }

}
