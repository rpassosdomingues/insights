package src;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    private static final float alturaPagina = PageSize.A4.getHeight();  
    private static final float larguraPagina = PageSize.A4.getWidth(); 

    @Override
    public void start(Stage primaryStage) {
        // Criando os campos com ícones e estilos personalizados
        VBox parceirosField = criaPostIt("Parceiros", "green", "partners.png");
        VBox segmentoClientesField = criaPostIt("Segmento de Clientes", "green", "customers.png");
        VBox atividadesField = criaPostIt("Atividades", "orange", "activities.png");
        VBox relacionamentoField = criaPostIt("Relacionamento", "yellow", "relationship.png");
        VBox recursosField = criaPostIt("Recursos", "purple", "resources.png");
        VBox canaisField = criaPostIt("Canais de Venda", "red", "sales.png");
        VBox propostaValorField = criaPostIt("Proposta de Valor", "blue", "value.png");
        VBox custosField = criaPostIt("Estrutura de Custos", "gray", "costs.png");
        VBox fontesRendaField = criaPostIt("Fontes de Renda", "lightblue", "income.png");

        // Layout do JavaFX
        HBox topBox = new HBox(parceirosField, segmentoClientesField);
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(20);

        HBox middleBox = new HBox(atividadesField, propostaValorField, relacionamentoField);
        middleBox.setAlignment(Pos.CENTER);
        middleBox.setSpacing(20);

        HBox bottomMiddleBox = new HBox(recursosField, canaisField);
        bottomMiddleBox.setAlignment(Pos.CENTER);
        bottomMiddleBox.setSpacing(20);

        HBox bottomBox = new HBox(custosField, fontesRendaField);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(20);

        // Layout principal
        VBox layout = new VBox(topBox, middleBox, bottomMiddleBox, bottomBox);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(15);

        // Botão para exportar o modelo para PDF
        Button exportButton = new Button("Exportar PDF");
        exportButton.setOnAction(e -> exportaPDF(parceirosField, propostaValorField, segmentoClientesField,
                atividadesField, relacionamentoField, recursosField, canaisField, custosField, fontesRendaField));

        VBox root = new VBox(layout, exportButton);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        // Configurando a cena e o palco do JavaFX
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Modelo de Negócio - Canvas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox criaPostIt(String title, String color, String iconName) {
        // Carregando o ícone associado ao campo
        ImageView icon = new ImageView(new Image("file:e/cache/NidusBOT/canvaBot/icons/" + iconName));
        icon.setFitHeight(30);
        icon.setFitWidth(30);

        // Criando o campo de texto com o título e cor de fundo
        TextField textField = new TextField();
        textField.setPromptText(title);
        textField.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");

        // Empacotando o ícone e o campo de texto em um VBox
        VBox vBox = new VBox(icon, textField);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        return vBox;
    }

    /**
    * Este método exporta um documento PDF contendo um modelo de Canvas, que é gerado dinamicamente 
    * a partir dos campos fornecidos como parâmetros. O documento é salvo na pasta 'out' com um nome 
    * único baseado no timestamp de quando o arquivo é gerado. O método segue as etapas:
    * 
    * 1. **Verificação e Criação do Diretório 'out'**:
    *    - Antes de salvar o arquivo PDF, o código verifica se o diretório 'out' existe na raiz do projeto.
    *    - Caso o diretório não exista, ele é criado automaticamente utilizando o método `mkdir()`.
    * 
    * 2. **Configuração do Documento PDF**:
    *    - O documento é configurado com o tamanho da página A4 no formato paisagem (`PageSize.A4.rotate()`).
    *    - O `PdfWriter` é instanciado com um caminho de saída, que inclui um timestamp gerado dinamicamente para garantir 
    *      que o nome do arquivo seja único, evitando sobrescritas de arquivos existentes.
    * 
    * 3. **Abertura do Documento**:
    *    - O método chama `document.open()` para preparar o documento PDF para inserção de conteúdo.
    * 
    * 4. **Desenhando e Preenchendo o Canvas**:
    *    - O `PdfContentByte canvas` é utilizado para desenhar no documento PDF.
    *    - O método `desenhaCanvas(canvas, fields)` é chamado para desenhar os post-its no canvas. Este método é responsável 
    *      por desenhar os campos do modelo de Canvas nas posições e com as cores apropriadas, conforme especificado no código.
    *    - Após isso, o método `escreveNoPostIt(canvas, fields)` é chamado para inserir o texto nos post-its já desenhados no canvas.
    * 
    * 5. **Fechamento do Documento**:
    *    - Após adicionar todo o conteúdo, o método chama `document.close()` para finalizar e salvar o arquivo PDF gerado.
    * 
    * 6. **Mensagens de Log**:
    *    - Uma mensagem de sucesso é exibida no console, informando o caminho onde o arquivo PDF foi salvo, incluindo o nome 
    *      gerado com o timestamp (ex: "Modelo Canvas gerado com sucesso em: out/20231217123045_modelo-canvas.pdf").
    * 
    * 7. **Tratamento de Exceções**:
    *    - O método trata as exceções `DocumentException` e `IOException`. Caso algum erro ocorra durante a criação ou escrita 
    *      do documento, ele é capturado e exibido no console com `e.printStackTrace()`.
    * 
    * @param fields - Vários objetos do tipo VBox, contendo os dados a serem desenhados nos post-its do modelo Canvas.
    *                Cada campo é desenhado e preenchido conforme a posição e as cores definidas no código.
    * 
    * O arquivo PDF gerado será salvo na pasta 'out' com o nome do arquivo baseado no timestamp atual (ex: 'out/20231217123045_modelo-canvas.pdf').
    */
    private void exportaPDF(VBox... fields) {
        // Cria o diretório /out, se não existir
        File outputDir = new File("out");
        if (!outputDir.exists()) {
            outputDir.mkdir(); // Cria o diretório
        }

        Document document = new Document(PageSize.A4.rotate(), 0, 0, 0, 0);

        try {
            // Gera o nome do arquivo com base no timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String outputPath = "out/" + timestamp + "_modelo-canvas.pdf"; // Caminho completo para salvar o arquivo PDF

            // Cria o escritor de PDF e o documento
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            PdfContentByte canvas = writer.getDirectContent();

            // Chama o método para desenhar os post-its no canvas
            desenhaCanvas(canvas, fields);

            // Escreve o texto nos post-its
            escreveNoPostIt(canvas, fields);

            // Fecha o documento e salva o arquivo
            document.close();
            System.out.println("Modelo Canvas gerado com sucesso em: " + outputPath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Este método desenha um layout com vários post-its em um canvas PDF, distribuídos em uma página A4 no formato paisagem.
    * A página é dividida em uma linha superior (70% da altura da página) e uma linha inferior (30% da altura da página),
    * com post-its distribuídos nas colunas e linhas da página de acordo com a estrutura especificada.
    * 
    * A função utiliza as dimensões da página A4 no formato paisagem e divide a largura da página em 5 colunas iguais.
    * Em seguida, posiciona os post-its em locais específicos para representar diferentes áreas de um modelo de negócios (ex: Parceiros,
    * Segmento de Clientes, Proposta de Valor, etc.), e insere o conteúdo de cada campo (campo de entrada de texto) dentro de cada post-it.
    * 
    * Parâmetros:
    * @param canvas - O objeto PdfContentByte onde o desenho será realizado (representa o canvas do PDF).
    * @param fields - Um array de campos (VBox) que contém os textos a serem inseridos nos post-its.
    * 
    * Abaixo está a descrição detalhada do posicionamento e da configuração de cada post-it:
    * 
    * - Linha Superior (70% da altura da página):
    *   - Coluna 1: Post-it "Parceiros" com cor verde, ocupa toda a altura da linha superior.
    *   - Coluna 5: Post-it "Segmento de Clientes" com cor verde, ocupa toda a altura da linha superior.
    *   - Coluna 3: Post-it "Proposta de Valor" com cor azul, ocupa toda a altura da linha superior.
    *   - Coluna 2: Post-it "Atividades" com cor laranja, ocupa metade da altura da linha superior.
    *   - Coluna 4: Post-it "Relacionamento" com cor amarela, ocupa metade da altura da linha superior.
    * 
    * - Linha Inferior (30% da altura da página):
    *   - Coluna 2: Post-it "Recursos" com cor roxa, ocupa metade da altura da linha inferior.
    *   - Coluna 4: Post-it "Canais" com cor vermelha, ocupa metade da altura da linha inferior.
    *   - Coluna 1: Post-it "Custos" com cor cinza, ocupa 2.5 colunas de largura e toda a altura da linha inferior.
    *   - Coluna 5: Post-it "Fontes de Renda" com cor azul claro, ocupa 2.5 colunas de largura e toda a altura da linha inferior.
    * 
    * Cada post-it é desenhado em uma posição específica, começando pela coordenada x e y para cada um, e é ajustado de acordo com a altura e a largura da coluna e linha em que se encontra. O conteúdo dos post-its é extraído dos campos de texto fornecidos no parâmetro `fields`.
    * 
    * Para cada campo, o método `escreveNoPostIt` é chamado para inserir o texto dentro do retângulo do post-it, com ajuste de tamanho de fonte quando necessário para garantir que o texto caiba no post-it.
    * 
    * Estrutura detalhada das colunas e linhas:
    * - Linha superior ocupa 70% da altura da página (alturaPagina * 0.7).
    * - Linha inferior ocupa 30% da altura da página (alturaPagina * 0.3).
    * - A largura de cada coluna é calculada como a largura da página dividida por 5 (larguraPagina / 5).
    * - O espaço entre os post-its nas colunas e linhas é gerenciado pela variável `espaçamento`, ajustando-se conforme o número de post-its e a distribuição do layout.
    */
    private void desenhaCanvas(PdfContentByte canvas, VBox... fields) {
        // Calcula a largura e altura da página em paisagem
        float larguraPagina = PageSize.A4.rotate().getWidth();
        float alturaPagina = PageSize.A4.rotate().getHeight();

        float larguraColuna = larguraPagina / 5; // Divide a largura em 5 colunas iguais
        float alturaLinhaSuperior = alturaPagina * 0.7f; // Linha superior ocupa 70% da altura da página
        float alturaLinhaInferior = alturaPagina * 0.3f; // Linha inferior ocupa 30% da altura da página

        float x, y;

        // Linha Superior - 5 Colunas
        // Parceiros - Coluna 1
        x = 0;
        y = alturaPagina - alturaLinhaSuperior;
        encaixaPostIt(canvas, x, y, larguraColuna, alturaLinhaSuperior, "green");
        escreveNoPostIt(canvas, fields[0], x, y, larguraColuna, alturaLinhaSuperior); // Escrever texto

        // Segmento de Clientes - Coluna 5
        x = 4 * larguraColuna;
        encaixaPostIt(canvas, x, y, larguraColuna, alturaLinhaSuperior, "green");
        escreveNoPostIt(canvas, fields[1], x, y, larguraColuna, alturaLinhaSuperior); // Escrever texto

        // Proposta de Valor - Coluna 3 (Altura Dupla)
        x = 2 * larguraColuna;
        encaixaPostIt(canvas, x, y, larguraColuna, alturaLinhaSuperior, "blue");
        escreveNoPostIt(canvas, fields[2], x, y, larguraColuna, alturaLinhaSuperior); // Escrever texto

        // Atividades - Coluna 2 (metade da altura da linha superior)
        x = larguraColuna;
        encaixaPostIt(canvas, x, y + alturaLinhaSuperior / 2, larguraColuna, alturaLinhaSuperior / 2, "orange");
        escreveNoPostIt(canvas, fields[3], x, y + alturaLinhaSuperior / 2, larguraColuna, alturaLinhaSuperior / 2); // Escrever texto

        // Relacionamento - Coluna 4 (metade da altura da linha superior)
        x = 3 * larguraColuna;
        encaixaPostIt(canvas, x, y + alturaLinhaSuperior / 2, larguraColuna, alturaLinhaSuperior / 2, "yellow");
        escreveNoPostIt(canvas, fields[4], x, y + alturaLinhaSuperior / 2, larguraColuna, alturaLinhaSuperior / 2); // Escrever texto

        // Recursos - Coluna 2 (Abaixo de Atividades)
        x = larguraColuna;
        encaixaPostIt(canvas, x, y, larguraColuna, alturaLinhaSuperior / 2, "purple");
        escreveNoPostIt(canvas, fields[5], x, y, larguraColuna, alturaLinhaSuperior / 2); // Escrever texto

        // Canais - Coluna 4 (Abaixo de Relacionamento)
        x = 3 * larguraColuna;
        encaixaPostIt(canvas, x, y, larguraColuna, alturaLinhaSuperior / 2, "red");
        escreveNoPostIt(canvas, fields[6], x, y, larguraColuna, alturaLinhaSuperior / 2); // Escrever texto

        // Linha Inferior - 2 Retângulos (2,5 colunas cada)
        // Custos - Metade Esquerda
        x = 0;
        y = 0;
        encaixaPostIt(canvas, x, y, 2.5f * larguraColuna, alturaLinhaInferior, "gray");
        escreveNoPostIt(canvas, fields[7], x, y, 2.5f * larguraColuna, alturaLinhaInferior); // Escrever texto

        // Fontes de Renda - Metade Direita
        x = 2.5f * larguraColuna;
        encaixaPostIt(canvas, x, y, 2.5f * larguraColuna, alturaLinhaInferior, "lightblue");
        escreveNoPostIt(canvas, fields[8], x, y, 2.5f * larguraColuna, alturaLinhaInferior); // Escrever texto
    }

    /**
    * Função para escrever o texto dentro do retângulo.
    *
    * @param canvas O canvas onde o texto será desenhado.
    * @param field O campo com o texto que será exibido.
    * @param x A posição X do canto superior esquerdo do retângulo.
    * @param y A posição Y do canto superior esquerdo do retângulo.
    * @param largura A largura do retângulo.
    * @param altura A altura do retângulo.
    */
    private void escreveNoPostIt(PdfContentByte canvas, VBox field, float x, float y, float largura, float altura) {
        TextField textField = (TextField) field.getChildren().get(1);
        String texto = textField.getText();
        Font font = new Font(Font.FontFamily.HELVETICA, 8); // Ajuste a fonte e tamanho aqui

        // Coloca o texto no centro do retângulo
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(texto, font), x + largura / 2, y + altura / 2, 0);
    }

    /**
    * Função para desenhar um retângulo com uma cor de fundo específica no canvas.
    *
    * @param canvas O canvas onde o retângulo será desenhado.
    * @param x A posição X do canto superior esquerdo do retângulo.
    * @param y A posição Y do canto superior esquerdo do retângulo.
    * @param largura A largura do retângulo.
    * @param altura A altura do retângulo.
    * @param cor A cor de fundo do retângulo.
    */
    private void encaixaPostIt(PdfContentByte canvas, float x, float y, float largura, float altura, String cor) {
        // Configurar a cor de fundo do retângulo
        switch (cor) {
            case "green":
                canvas.setColorFill(BaseColor.GREEN);
                break;
            case "gray":
                canvas.setColorFill(BaseColor.GRAY);
                break;
            case "orange":
                canvas.setColorFill(BaseColor.ORANGE);
                break;
            case "yellow":
                canvas.setColorFill(BaseColor.YELLOW);
                break;
            case "purple":
                canvas.setColorFill(new BaseColor(128, 0, 128));  // Cor roxa
                break;
            case "red":
                canvas.setColorFill(BaseColor.RED);
                break;
            case "blue":
                canvas.setColorFill(BaseColor.BLUE);
                break;
            case "lightblue":
                canvas.setColorFill(new BaseColor(173, 216, 230)); // Cor azul claro
                break;
            default:
                canvas.setColorFill(BaseColor.GRAY);  // Caso padrão
                break;
        }

        // Desenhando o retângulo no canvas
        canvas.rectangle(x, y, largura, altura);
        canvas.fill();  // Preenchendo o retângulo com a cor definida
        canvas.stroke(); // Desenhando a borda do retângulo
    }

    private void escreveNoPostIt(PdfContentByte canvas, VBox... fields) {
        float x = 20, y = alturaPagina - 40, largura = 80, altura = 100;
        float espaçamento = 5;

        for (int i = 0; i < fields.length; i++) {
            // Acessa o campo de texto e o título
            TextField textField = (TextField) fields[i].getChildren().get(1);
            String texto = textField.getText();
            String titulo = ((VBox) fields[i]).getChildren().get(0).getId(); // Usando o título do Post-it como ID (se configurado)

            // Calcula as posições para o texto
            float textoX = x + (i % 3) * (largura + espaçamento);
            float textoY = y - (i / 3) * (altura + espaçamento);

            // Define o título com fonte maior e negrito
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Phrase tituloPhrase = new Phrase(titulo, tituloFont);
            // Coloca o título acima do texto, no topo do post-it
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, tituloPhrase, textoX + largura / 2, textoY + altura - 10, 0);

            // Ajustar o texto automaticamente no post-it
            // Ajusta o tamanho da fonte para caber no post-it
            Font textoFont = new Font(Font.FontFamily.HELVETICA, 6); // Tamanho de fonte básico
            Phrase textoPhrase = new Phrase(texto, textoFont);
        
            // Ajuste do tamanho da fonte com base no espaço disponível
            float fontSize = calcularTamanhoFonte(texto, largura, altura);
            textoFont.setSize(fontSize);

            // Desenha o texto ajustado dentro do post-it
            textoPhrase = new Phrase(texto, textoFont);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, textoPhrase, textoX + largura / 2, textoY + altura / 2, 0);
        }
    }

    private float calcularTamanhoFonte(String texto, float largura, float altura) {
        // Estima um tamanho de fonte baseado no espaço disponível
        int maxCharactersPerLine = (int) (largura / 5); // Aproximadamente 5px por caractere
        int totalLines = (int) (altura / 15); // Aproximadamente 15px por linha

        int totalCharacters = texto.length();
        int linesNeeded = (int) Math.ceil((double) totalCharacters / maxCharactersPerLine);

        // Ajusta a fonte de acordo com o número de linhas necessárias
        if (linesNeeded > totalLines) {
            return 6; // Fonte pequena para textos grandes
        } else {
            return 10; // Fonte maior para textos pequenos
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
