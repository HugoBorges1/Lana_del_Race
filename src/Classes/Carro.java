package Classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Carro {
    private double x, y; // Posições do carro
    private double velocidade = 0; // Velocidade atual do carro
    private double aceleracao = 0.5; // Aceleração constante
    private double desaceleracao = 0.1; // Desaceleração
    private double maxVelocidade = 20; // Velocidade máxima
    private double angulo = 0; // Ângulo atual do carro (em radianos)
    private double rotacao = 0.04; // Quanto o carro gira ao pressionar a direção
    private Image imagemCarro; // Imagem que representa o carro
    private int largura, altura; // Largura e altura do carro (baseado na imagem)
    private int voltas = 0; // Contador de voltas do carro
    private boolean passouPelaLinhaDeChegada = false; // Flag para indicar se o carro cruzou a linha de chegada
    private boolean emColisao = false;
    private long tempoColisao = 0;
    private long ultimoSomColisao = 0; // Tempo da última vez que o som foi tocado
    private static final long INTERVALO_SOM = 2000; // 5 segundos de intervalo
    private Image imagemColisao;
    
    // Construtor que inicializa o carro com posição inicial e caminho da imagem
    public Carro(int xInicial, int yInicial, String caminhoImagem) {
        this.x = xInicial;
        this.y = yInicial;

        // Carregar a imagem do carro a partir do caminho fornecido
        try {
            InputStream imgStream = getClass().getResourceAsStream(caminhoImagem);
            if (imgStream != null) {
                this.imagemCarro = ImageIO.read(imgStream);
            } else {
                System.out.println("Imagem não encontrada: " + caminhoImagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            imagemColisao = ImageIO.read(getClass().getResourceAsStream("/Imagens/exp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Definir largura e altura com base na imagem carregada ou definir valores padrão
        if (this.imagemCarro != null) {
            this.largura = imagemCarro.getWidth(null);
            this.altura = imagemCarro.getHeight(null);
        } else {
            this.largura = 100; // Valor padrão se não houver imagem
            this.altura = 100; // Valor padrão se não houver imagem
        }
    }

    // Método para desenhar o carro na tela, aplicando rotação e posição
    public void desenhar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform backup = g2d.getTransform();

        // Desenha o carro
        g2d.translate(x, y);
        g2d.rotate(angulo);

        if (imagemCarro != null) {
            g2d.drawImage(imagemCarro, -largura / 2, -altura / 2, null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(-largura / 2, -altura / 2, largura, altura);
        }

        g2d.setTransform(backup);

        // Verifica se está em colisão e se o tempo não passou de 0.5 segundos
        if (emColisao && (System.currentTimeMillis() - tempoColisao < 500)) {
            // Desenha a imagem da explosão/faísca
            g2d.drawImage(imagemColisao, (int) x - 50, (int) y - 50, null);  // Ajuste a posição da explosão
        } else {
            // Reseta o estado de colisão após 0.5 segundos
            emColisao = false;
        }
    }


    // Método que controla o movimento do carro na pista
    public void mover(int pista, Carro outroCarro) {
        // Atualiza a posição do carro com base no ângulo e velocidade
        x += Math.cos(angulo) * velocidade;
        y += Math.sin(angulo) * velocidade;

        // Simula desaceleração (diminui a velocidade quando não há aceleração)
        if (velocidade > 0) {
            velocidade -= desaceleracao;
        } else if (velocidade < 0) {
            velocidade += desaceleracao;
        }

        // Evita pequenas variações próximas a zero para parar o carro completamente
        if (Math.abs(velocidade) < desaceleracao) {
            velocidade = 0;
        }

        // Verifica colisão com a pista e executa a lógica específica da pista
        boolean colidiuComPista = false;
        switch (pista) {
            case 1:
                colidiuComPista = DeteccaoColisao.verificarColisaoComPista1(this);
                break;
            // Adicione mais casos para diferentes pistas, se necessário
        }

        if (colidiuComPista) {
            
            registrarColisao();
            retroceder(); // Parar o carro se houver colisão com a pista
        }

        // Verificar colisão com outro carro
        boolean colidiuComCarro = DeteccaoColisao.verificarColisao(this, outroCarro);
        if (colidiuComCarro) {
            
            colidirCom(outroCarro); // Aplica lógica de colisão com outro carro
     
        }

        verificarVolta(this); // Verifica se o carro completou uma volta
    }

    // Método que verifica se o carro passou pela linha de chegada para contar uma volta
    private void verificarVolta(Carro carro) {
        // Supondo que a linha de chegada está em x = 650
        if (carro.getX() < 650 && !carro.passouPelaLinhaDeChegada()) {
            carro.setPassouPelaLinhaDeChegada(true);
        } else if (carro.getX() > 650 && carro.passouPelaLinhaDeChegada()) {
            carro.incrementarVoltas(); // Incrementa o contador de voltas
            carro.setPassouPelaLinhaDeChegada(false);
        }
    }

    // Métodos para obter o número de voltas do carro
    public int getVoltas() {
        return voltas;
    }

    // Método para acelerar o carro
    public void acelerar() {
        if (velocidade < maxVelocidade) {
            velocidade += aceleracao;
        }
    }

    // Método para frear o carro
    public void freiar() {
        if (velocidade > -maxVelocidade / 2) {
            velocidade -= aceleracao;
        }
    }

    // Método para virar o carro à esquerda
    public void virarEsquerda() {
        if (velocidade != 0) { // Só permite virar se o carro estiver em movimento
            angulo -= rotacao;
        }
    }

    // Método para virar o carro à direita
    public void virarDireita() {
        if (velocidade != 0) { // Só permite virar se o carro estiver em movimento
            angulo += rotacao;
        }
    }

    // Método para obter um retângulo de colisão do carro para verificar colisões
    public Rectangle getRetangulo() {
        // Reduzir o tamanho do retângulo para diminuir a área de colisão
        int larguraReduzida = (int) (largura * 0.5); // Reduz a largura
        int alturaReduzida = (int) (altura * 0.5); // Reduz a altura

        // Ajusta a posição para que o retângulo menor ainda fique no centro do carro
        int xAjustado = (int) x - larguraReduzida / 2;
        int yAjustado = (int) y - alturaReduzida / 2;

        return new Rectangle(xAjustado, yAjustado, larguraReduzida, alturaReduzida);
    }

    // Método para parar o carro, usado em caso de colisão
    public void retroceder() {
        velocidade = 0;
    }

    // Métodos de controle para verificar e ajustar a passagem pela linha de chegada
    public double getX() {
        return x;
    }

    public boolean passouPelaLinhaDeChegada() {
        return passouPelaLinhaDeChegada;
    }

    public void setPassouPelaLinhaDeChegada(boolean passou) {
        this.passouPelaLinhaDeChegada = passou;
    }

    // Método para incrementar o contador de voltas do carro
    public void incrementarVoltas() {
        voltas++;
    }

    // Método para definir o comportamento do carro ao colidir com outro carro
    public void colidirCom(Carro outroCarro) {
        this.retroceder();
        outroCarro.retroceder();

        // Marcar a colisão
        registrarColisao();
    }

    private void registrarColisao() {
        this.emColisao = true;
        this.tempoColisao = System.currentTimeMillis(); // Marca o tempo da colisão

        // Toca o som apenas se 2 segundos tiverem se passado desde o último som
        if (System.currentTimeMillis() - ultimoSomColisao > INTERVALO_SOM) {
            Som.tocarEfeitoSom("/Sons/exp.wav");
            ultimoSomColisao = System.currentTimeMillis(); // Atualiza o tempo do último som
        }

    }

        public void reduzirVelocidadeTemporariamente() {
        double velocidadeOriginal = velocidade;
        velocidade *= 0.5; // Reduz para 50%

        // Restaurar a velocidade após 1 segundo
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            velocidade = velocidadeOriginal;
        }).start();
    }
}
