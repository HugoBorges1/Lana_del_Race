package Classes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Obstacle {
    private double x, y; // Posição do obstáculo
    private Image imagemObstaculo;
    private int largura, altura;

    // Construtor que aceita a imagem do obstáculo
    public Obstacle(String caminhoImagem) {
        try {
            this.imagemObstaculo = ImageIO.read(getClass().getResourceAsStream(caminhoImagem));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Define largura e altura com base na imagem
        if (this.imagemObstaculo != null) {
            this.largura = imagemObstaculo.getWidth(null);
            this.altura = imagemObstaculo.getHeight(null);
        } else {
            this.largura = 50; // Tamanho padrão caso a imagem falhe
            this.altura = 50;
        }

        // Posição inicial aleatória na área da pista
        do {
            this.x = Math.random() * (1720 - 100 - largura) + 100; // Limitar entre as bordas externas
            this.y = Math.random() * (880 - 100 - altura) + 100;  // Limitar entre as bordas externas
        } while ((x > 300 && x < 300 + 1320 && y > 300 && y < 300 + 480)); // Evitar a área interna
    }

    // Desenhar o obstáculo
    public void desenhar(Graphics g) {
        if (imagemObstaculo != null) {
            g.drawImage(imagemObstaculo, (int) x, (int) y, null);
        }
    }

    // Retorna o retângulo do obstáculo para detecção de colisão
    public Rectangle getRetangulo() {
        return new Rectangle((int) x, (int) y, largura, altura);
    }
}
