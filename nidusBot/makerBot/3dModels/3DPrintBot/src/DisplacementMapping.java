package src;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class DisplacementMapping {
    
    // PixelReader utilizado para ler os dados dos pixels da imagem
    private PixelReader pixelReader;
    
    // Largura e altura da imagem para iterar sobre os pixels
    private int width;
    private int height;
    
    // A malha TriangleMesh que representa a superfície 3D gerada a partir da imagem
    private TriangleMesh surfaceMesh;

    // Construtor que inicializa o PixelReader, as dimensões da imagem e a malha de superfície
    public DisplacementMapping(PixelReader pixelReader, int width, int height) {
        this.pixelReader = pixelReader;  // PixelReader para ler os dados da imagem
        this.width = width;              // Largura da imagem para iterar sobre os pixels
        this.height = height;            // Altura da imagem para iterar sobre os pixels
        this.surfaceMesh = new TriangleMesh(); // Inicializa uma TriangleMesh vazia para a superfície 3D
    }

    /**
     * Este método aplica o mapeamento de deslocamento para gerar uma superfície 3D com base em uma imagem.
     * O brilho de cada pixel é usado para determinar a elevação de um ponto na malha.
     * 
     * @param image A imagem utilizada para gerar o mapa de deslocamento.
     */
    public void applyDisplacementMapping(Image image) {
        // Verifica se a imagem é nula
        if (image == null) {
            System.out.println("Erro: A imagem é nula!");
            return;
        }

        // Obtém o PixelReader da imagem para acessar os dados dos pixels
        pixelReader = image.getPixelReader();
        
        // Verifica se o PixelReader foi inicializado corretamente
        if (pixelReader == null) {
            System.out.println("Erro: PixelReader é nulo! Não foi possível ler os dados da imagem.");
            return;
        }

        // Loga as dimensões da imagem
        System.out.println("Dimensões da imagem: Largura = " + width + ", Altura = " + height);

        // Limpa qualquer ponto existente na malha antes de adicionar novos
        surfaceMesh.getPoints().clear();

        // Itera sobre cada pixel da imagem para criar a superfície 3D
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                // Obtém a cor do pixel na posição (x, y)
                Color color = pixelReader.getColor(x, y);
                
                // Verifica se a cor é nula (não deve ocorrer, mas é uma boa prática verificar)
                if (color == null) {
                    System.out.println("Erro: A cor é nula no pixel (" + x + ", " + y + ")");
                    continue;  // Pula este pixel se houver um problema
                }

                // Usa o brilho do pixel para calcular a elevação (altura)
                // O brilho varia entre 0 (escuro) e 1 (brilho máximo), e multiplicamos por 10 para escalá-lo
                float elevation = (float) (color.getBrightness() * 10);  // Converte para float

                // Loga a elevação calculada para o pixel
                System.out.println("Pixel (" + x + "," + y + ") - Elevação: " + elevation);

                // Adiciona as coordenadas x, y e a elevação (z) como um ponto na malha 3D
                surfaceMesh.getPoints().addAll((float) x, (float) y, elevation);  // Cast para float
            }
        }

        // Loga o número total de pontos adicionados à superfície
        System.out.println("Total de pontos adicionados à superfície: " + surfaceMesh.getPoints().size() / 3);

        // Gera as faces (triângulos) da superfície 3D com base nos pontos
        // Este loop conecta pontos adjacentes para formar faces quadrilaterais
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {

                // Calcula os índices dos quatro pontos vizinhos (superior-esquerda, superior-direita, inferior-esquerda, inferior-direita)
                int topLeft = (y * width) + x;
                int topRight = (y * width) + (x + 1);
                int bottomLeft = ((y + 1) * width) + x;
                int bottomRight = ((y + 1) * width) + (x + 1);

                // Loga os índices dos vértices para os triângulos
                System.out.println("Adicionando face com os índices: " + topLeft + ", " + topRight + ", " + bottomRight + ", " + bottomLeft);

                // Adiciona dois triângulos que formam um retângulo
                surfaceMesh.getFaces().addAll(topLeft, 0, topRight, 0, bottomRight, 0);
                surfaceMesh.getFaces().addAll(topLeft, 0, bottomRight, 0, bottomLeft, 0);
            }
        }

        // Loga o número total de faces adicionadas à superfície
        System.out.println("Total de faces adicionadas à superfície: " + surfaceMesh.getFaces().size() / 6); // Cada face tem 6 valores (3 pontos por triângulo)
    }

    /**
     * Retorna a malha 3D gerada.
     * 
     * @return A TriangleMesh que representa a superfície deslocada.
     */
    public TriangleMesh getSurfaceMesh() {
        return surfaceMesh;
    }

    /**
     * Limpa a malha de superfície removendo todos os pontos e faces.
     * Isso é útil ao resetar a superfície antes de aplicar um novo mapeamento de deslocamento.
     */
    public void clearSurface() {
        surfaceMesh.getPoints().clear();  // Limpa os pontos da malha
        surfaceMesh.getFaces().clear();   // Limpa as faces da malha
        System.out.println("Superfície limpa.");
    }
}
