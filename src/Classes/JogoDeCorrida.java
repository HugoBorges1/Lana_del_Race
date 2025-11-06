package Classes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JogoDeCorrida extends JPanel implements KeyListener {
    private Carro carro1; // Instância do carro do jogador 1
    private Carro carro2; // Instância do carro do jogador 2
    private boolean jogoAtivo = true; // Controle para manter o jogo ativo
    private boolean[] teclas = new boolean[256]; // Array para registrar quais teclas estão pressionadas
    private int voltasRestantes; // Variável para controlar o número de voltas restantes
    private int pista; // Identificador da pista selecionada
    private Image pistaInternaImage; // Imagem da parte interna da pista

    // Variável para armazenar a música de fundo
    private String musicaDeFundoPath; 

    // Lista para gerenciar os obstáculos
    private ArrayList<Obstacle> obstaculos = new ArrayList<>();
    private static final int MAX_OBSTACULOS = 5; // Limite máximo de 5 obstáculos simultâneos
    private long ultimoSpawn = System.currentTimeMillis();

    // Construtor que aceita o número da pista e o número de voltas
    public JogoDeCorrida(int numPista, int numVoltas, String caminhoCarro1, String caminhoCarro2) {
        
        // Sorteia a musica de fundo da corrida
        int sorteado = sortearNumero(1, 2, 3);
        
        // Toca música de fundo sorteada
        switch (sorteado) {
            case 1:
                musicaDeFundoPath = "/Sons/ct.wav";
                break;
            case 2:
                musicaDeFundoPath = "/Sons/run.wav";
                break;
            case 3:
                musicaDeFundoPath = "/Sons/stro.wav";
                break;
            default:
                musicaDeFundoPath = null; // Caso de segurança
                break;
        }
        
        if (musicaDeFundoPath != null) {
            Som.tocarMusicaV(musicaDeFundoPath); // Toca a música escolhida
        }
        
        // Inicializa os carros com as imagens selecionadas
        carro1 = new Carro(500, 250, caminhoCarro1); // Cria o carro do jogador 1
        carro2 = new Carro(500, 150, caminhoCarro2); // Cria o carro do jogador 2

        voltasRestantes = numVoltas; // Define o número de voltas
        pista = numPista; // Define a pista selecionada
        this.addKeyListener(this); // Adiciona o listener de teclado
        this.setFocusable(true); // Permite que o componente receba o foco para detectar entradas
        this.requestFocusInWindow(); // Garante o foco na janela para entradas de teclado

        // ### CORREÇÃO APLICADA ###
        // Carrega os recursos (imagens) APENAS UMA VEZ no construtor
        try {
            if (pista == 1) {
                // Carrega a imagem da pista e armazena na variável de instância
                pistaInternaImage = ImageIO.read(getClass().getResourceAsStream("/Imagens/ye.jpg"));
            }
            // Se você tiver uma pista 2, adicione um else if (pista == 2) aqui
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro de I/O ao carregar imagem da pista!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Esta é a exceção provável se o caminho estiver errado
            System.err.println("Imagem da pista não encontrada! Verifique o caminho do recurso.");
            // A imagem ficará nula, mas o jogo não vai travar
        }
    }
    
    public static int sortearNumero(int num1, int num2, int num3) {
        Random random = new Random();
        int[] numeros = {num1, num2, num3};
        int index = random.nextInt(numeros.length);  // Gera um número aleatório entre 0 e 2
        return numeros[index];
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        desenharPista(g); // Desenha a pista na tela
        carro1.desenhar(g); // Desenha o carro 1 na tela
        carro2.desenhar(g); // Desenha o carro 2 na tela

        // Desenha os obstáculos
        for (Obstacle obstaculo : obstaculos) {
            obstaculo.desenhar(g);
        }

        desenharInformacoes(g); // Desenha as informações de voltas e status na tela
    }

    // Método para desenhar a pista
    private void desenharPista(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (pista == 1) {

            // Desenha a área externa e interna da pista
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(100, 100, 1720, 880); // Área externa da pista

            // Desenhar a imagem dentro da área interna da pista, se disponível
            if (pistaInternaImage != null) {
                g2d.drawImage(pistaInternaImage, 300, 300, 1320, 480, null); // Desenha a imagem interna
            } else {
                // Caso a imagem tenha falhado ao carregar (null), desenha a área interna em cinza claro
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(300, 300, 1320, 480); 
            }

            // Desenho das bordas da pista
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(15)); // Define a espessura da linha
            g2d.drawRect(100, 100, 1720, 880); // Borda externa
            g2d.drawRect(300, 300, 1320, 480); // Borda interna

            // Linha de chegada
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(20));
            g2d.drawLine(650, 100, 650, 300); // Linha de chegada
        }
    }

    // Método para desenhar informações na tela, como voltas restantes e status dos carros
    private void desenharInformacoes(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("Jogador 1 está na volta: " + carro1.getVoltas(), 50, 50);
        g.drawString("Jogador 2 está na volta: " + carro2.getVoltas(), 50, 80);
    }

    // Método que inicia o jogo e controla a lógica de atualização contínua
    private void iniciarJogo() {
        new Thread(() -> {
            while (jogoAtivo) {
                processarEntrada(); // Processa entradas do teclado
                carro1.mover(pista, carro2); // Move o carro 1
                carro2.mover(pista, carro1); // Move o carro 2

                gerenciarObstaculos(); // Gerencia o spawn e despawn de obstáculos
                verificarColisaoComObstaculos(carro1); // Verifica colisão com obstáculos para o carro 1
                verificarColisaoComObstaculos(carro2); // Verifica colisão com obstáculos para o carro 2

                // Verifica colisões entre os carros
                if (DeteccaoColisao.verificarColisao(carro1, carro2)) {
                    carro1.colidirCom(carro2); // Aplica lógica de colisão
                    carro2.colidirCom(carro1);
                }

                verificarVencedor(); // Verifica se há um vencedor
                repaint(); // Redesenha a tela

                try {
                    Thread.sleep(20); // Controla a velocidade do loop de jogo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Método para verificar se algum jogador completou as voltas necessárias
    private void verificarVencedor() {
        if (carro1.getVoltas() >= voltasRestantes) {
            jogoAtivo = false; // Termina o jogo
            
            // ### CORREÇÃO APLICADA ###
            if (musicaDeFundoPath != null) {
                Som.pararMusica(musicaDeFundoPath); // Para a música correta
            }
            
            Som.tocarMusicaV("/Sons/db.wav"); // Toca música de vitória
            JOptionPane.showMessageDialog(this, "Jogador 1 ganhou!"); // Exibe mensagem de vitória
        } else if (carro2.getVoltas() >= voltasRestantes) {
            jogoAtivo = false; // Termina o jogo
            
            if (musicaDeFundoPath != null) {
                Som.pararMusica(musicaDeFundoPath); // Para a música correta
            }
            
            Som.tocarMusicaV("/Sons/cag.wav"); // Toca música de vitória
            JOptionPane.showMessageDialog(this, "Jogador 2 ganhou!"); // Exibe mensagem de vitória
        }
    }


    // Método para processar entradas de teclado
    private void processarEntrada() {
        if (teclas[KeyEvent.VK_W]) carro1.acelerar(); // Jogador 1 acelera
        if (teclas[KeyEvent.VK_S]) carro1.freiar(); // Jogador 1 freia
        if (teclas[KeyEvent.VK_A]) carro1.virarEsquerda(); // Jogador 1 vira à esquerda
        if (teclas[KeyEvent.VK_D]) carro1.virarDireita(); // Jogador 1 vira à direita
        if (teclas[KeyEvent.VK_UP]) carro2.acelerar(); // Jogador 2 acelera
        if (teclas[KeyEvent.VK_DOWN]) carro2.freiar(); // Jogador 2 freia
        if (teclas[KeyEvent.VK_LEFT]) carro2.virarEsquerda(); // Jogador 2 vira à esquerda
        if (teclas[KeyEvent.VK_RIGHT]) carro2.virarDireita(); // Jogador 2 vira à direita
    }

    // Método para gerenciar obstáculos (spawn e despawn)
    private void gerenciarObstaculos() {
        long agora = System.currentTimeMillis();

        // Spawn de novos obstáculos a cada 2 segundos
        if (agora - ultimoSpawn > 2000) {
            // Remover o obstáculo mais antigo se já houver 5
            if (obstaculos.size() >= MAX_OBSTACULOS) {
                obstaculos.remove(0); // Remove o obstáculo mais antigo
            }

            // Adicionar novo obstáculo
            obstaculos.add(new Obstacle("/Imagens/obs.png")); // Caminho da imagem do obstáculo
            ultimoSpawn = agora; // Atualiza o tempo do último spawn
        }

        // Remover obstáculos que ultrapassaram o tempo limite (gerenciado pelo número de obstáculos)
        Iterator<Obstacle> iterador = obstaculos.iterator();
        while (iterador.hasNext()) {
            Obstacle obstaculo = iterador.next();
            // Aqui não removemos obstáculos pelo tempo, mas pelo limite de 5
        }
    }

    // Verificar colisão entre carro e obstáculos
    private void verificarColisaoComObstaculos(Carro carro) {
        for (Obstacle obstaculo : obstaculos) {
            if (carro.getRetangulo().intersects(obstaculo.getRetangulo())) {
                carro.reduzirVelocidadeTemporariamente();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        teclas[e.getKeyCode()] = true; // Marca a tecla como pressionada
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclas[e.getKeyCode()] = false; // Marca a tecla como liberada
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // Método principal para iniciar o jogo a partir do menu
    public static void main(String[] args) {
        JFrame menu = new JFrame("Menu Inicial");
        menu.setSize(1920, 1080);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setLayout(null);

        Som.tocarMusicaV("/Sons/fla.wav"); // Inicia música de fundo do menu

        PainelFundo painelFundo = new PainelFundo("/Imagens/lanadelmengo.jpg");
        painelFundo.setBounds(0, 0, 1920, 1080);
        painelFundo.setLayout(null);
        menu.add(painelFundo);

        JButton btnJogar = new JButton("Jogar");
        btnJogar.setBounds(760, 900, 400, 80);
        btnJogar.setFont(new Font("Arial", Font.PLAIN, 32));
        painelFundo.add(btnJogar);

        JButton btnFechar = new JButton("Sair");
        btnFechar.setBounds(760, 780, 400, 80);
        btnFechar.setFont(new Font("Arial", Font.PLAIN, 32));
        painelFundo.add(btnFechar);

        // Ações do botão Jogar
        btnJogar.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) {
                // Define um array de pistas disponíveis
                String[] pistas = {"Pista 1"};

                // ComboBox para selecionar a pista
                JComboBox<String> pistaComboBox = new JComboBox<>(pistas);

                // Campo de texto para inserir o número de voltas, com o valor inicial definido como "1"
                JTextField voltasField = new JTextField("1", 5);
                voltasField.setHorizontalAlignment(JTextField.CENTER); // Alinha o texto ao centro

                // Botões para incrementar (+) ou decrementar (-) o número de voltas
                JButton btnMais = new JButton("+");
                JButton btnMenos = new JButton("-");

                // Painel que organiza os botões de controle de voltas e o campo de texto
                JPanel painelVoltas = new JPanel();
                painelVoltas.add(btnMenos); // Adiciona o botão de decrementar
                painelVoltas.add(voltasField); // Adiciona o campo de texto de voltas
                painelVoltas.add(btnMais); // Adiciona o botão de incrementar

                // Ação do botão "+" (incrementar o número de voltas)
                btnMais.addActionListener(evt -> {
                    int voltas = Integer.parseInt(voltasField.getText()); // Obtém o valor atual de voltas
                    voltas++; // Incrementa em 1
                    voltasField.setText(String.valueOf(voltas)); // Atualiza o campo de texto com o novo valor
                });

                // Ação do botão "-" (decrementar o número de voltas)
                btnMenos.addActionListener(evt -> {
                    int voltas = Integer.parseInt(voltasField.getText()); // Obtém o valor atual de voltas
                    if (voltas > 1) { // Apenas decrementa se o número de voltas for maior que 1
                        voltas--; // Decrementa em 1
                        voltasField.setText(String.valueOf(voltas)); // Atualiza o campo de texto com o novo valor
                    }
                });

                // Painel personalizado para a seleção dos carros (SelecaoCarro)
                SelecaoCarro painelSelecao = new SelecaoCarro();

                // Cria uma mensagem de diálogo que exibe as opções de configuração da corrida
                Object[] message = {
                    "Selecione a Pista:", pistaComboBox, // Exibe a seleção de pista
                    "Número de Voltas:", painelVoltas, // Exibe o painel de controle de voltas
                    "Selecione os Carros:", painelSelecao // Exibe a seleção de carros
                };

                // Exibe um diálogo de confirmação com as configurações da corrida
                int option = JOptionPane.showConfirmDialog(
                    null, message, "Configurações da Corrida", JOptionPane.OK_CANCEL_OPTION
                );

                // Se o usuário confirmar as configurações (clicar em OK)
                if (option == JOptionPane.OK_OPTION) {
                    // Obtém a pista selecionada (pistas começam em 1, então soma-se 1 ao índice)
                    int numPista = pistaComboBox.getSelectedIndex() + 1;

                    // Obtém o número de voltas do campo de texto
                    int numVoltas = Integer.parseInt(voltasField.getText());

                    // Obtém os carros selecionados no painel de seleção
                    String carro1Selecionado = painelSelecao.getCarroSelecionado1();
                    String carro2Selecionado = painelSelecao.getCarroSelecionado2();

                    // Para a música de fundo do menu
                    Som.pararMusica("/Sons/fla.wav");

                    // Fecha o menu
                    menu.dispose();

                    // Cria uma nova janela para o jogo
                    JFrame janela = new JFrame("LANA DEL RACE");

                    // Inicializa o jogo com as configurações selecionadas (pista, voltas e carros)
                    JogoDeCorrida jogo = new JogoDeCorrida(numPista, numVoltas, carro1Selecionado, carro2Selecionado);

                    // Adiciona o jogo na janela
                    janela.add(jogo);
                    janela.setSize(1920, 1080); // Define o tamanho da janela
                    janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a ação ao fechar
                    janela.setVisible(true); // Torna a janela visível
                    jogo.requestFocusInWindow(); // Garante que o jogo tenha o foco para receber entradas
                    jogo.iniciarJogo(); // Inicia o jogo
                }
            }
        });

        // Ação do botão Sair
        btnFechar.addActionListener(e -> {
            // Para a música de fundo
            Som.pararMusica("/Sons/fla.wav");

            // Fecha o programa
            System.exit(0);
        });

        // Define a localização do menu para aparecer no centro da tela
        menu.setLocationRelativeTo(null);

        // Torna o menu visível
        menu.setVisible(true);
    }
}