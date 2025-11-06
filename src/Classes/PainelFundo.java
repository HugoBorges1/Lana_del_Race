package Classes;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PainelFundo extends JPanel {
    private Image imagemFundo; // Imagem que será usada como fundo do painel

    // Construtor que carrega a imagem de fundo a partir de um caminho fornecido
    public PainelFundo(String caminhoImagem) {
        try {
            imagemFundo = ImageIO.read(getClass().getResourceAsStream(caminhoImagem));
        } catch (IOException e) {
            e.printStackTrace(); // Exibe o erro se não for possível carregar a imagem
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Chama o método de superclasse para garantir que o painel seja desenhado corretamente
        if (imagemFundo != null) {
            // Desenha a imagem de fundo, ajustando-a para preencher todo o painel
            g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
