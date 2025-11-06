package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class SelecaoCarro extends JPanel {
    private int indiceCarro1 = 0; // Índice do carro selecionado pelo jogador 1
    private int indiceCarro2 = 0; // Índice do carro selecionado pelo jogador 2

    // Array contendo os caminhos para as imagens dos carros
    private String[] carros = {
        "/Imagens/BB.png", "/Imagens/aka.png", "/Imagens/borntodie.png",
        "/Imagens/chem.png", "/Imagens/didu.png", "/Imagens/honey.png",
        "/Imagens/nfr.png", "/Imagens/lustf.png", "/Imagens/paradise.png",
        "/Imagens/ultra.png"
    };

    // Arrays para guardar os ÍCONES pré-carregados, em vez de carregar do disco toda hora
    private ImageIcon[] iconesCarros;
    private ImageIcon iconePlaceholder; // Imagem reserva para caso de erro
    private static final int PREVIEW_SIZE = 128; // Tamanho padrão para os previews

    private JLabel previewCarro1; // Rótulo para mostrar a pré-visualização do carro do jogador 1
    private JLabel previewCarro2; // Rótulo para mostrar a pré-visualização do carro do jogador 2

    // Construtor que configura a interface de seleção de carros
    public SelecaoCarro() {

        criarIconePlaceholder();
        carregarIconesCarros();

        setLayout(new GridLayout(2, 3)); // Layout de grade com duas linhas e três colunas

        // Componentes de seleção para o Jogador 1
        JButton btnEsquerda1 = new JButton("<"); // Botão para navegar para a esquerda
        previewCarro1 = new JLabel(iconesCarros[indiceCarro1]); 
        previewCarro1.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza a imagem
        JButton btnDireita1 = new JButton(">"); // Botão para navegar para a direita

        // Componentes de seleção para o Jogador 2
        JButton btnEsquerda2 = new JButton("<"); // Botão para navegar para a esquerda
        previewCarro2 = new JLabel(iconesCarros[indiceCarro2]); 
        previewCarro2.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza a imagem
        JButton btnDireita2 = new JButton(">"); // Botão para navegar para a direita

        // Adiciona os componentes ao painel
        add(btnEsquerda1);
        add(previewCarro1);
        add(btnDireita1);

        add(btnEsquerda2);
        add(previewCarro2);
        add(btnDireita2);

        // Ação para os botões de navegação do Jogador 1
        btnEsquerda1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceCarro1 = (indiceCarro1 - 1 + carros.length) % carros.length; // Decrementa o índice de forma circular
                atualizarPreviewCarro1(); // Atualiza a imagem de pré-visualização
            }
        });

        btnDireita1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceCarro1 = (indiceCarro1 + 1) % carros.length; // Incrementa o índice de forma circular
                atualizarPreviewCarro1(); // Atualiza a imagem de pré-visualização
            }
        });

        // Ação para os botões de navegação do Jogador 2
        btnEsquerda2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceCarro2 = (indiceCarro2 - 1 + carros.length) % carros.length; // Decrementa o índice de forma circular
                atualizarPreviewCarro2(); // Atualiza a imagem de pré-visualização
            }
        });

        btnDireita2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indiceCarro2 = (indiceCarro2 + 1) % carros.length; // Incrementa o índice de forma circular
                atualizarPreviewCarro2(); // Atualiza a imagem de pré-visualização
            }
        });
    }

    // Cria uma imagem "reserva" para usar se o arquivo não for encontrado.
    private void criarIconePlaceholder() {
        BufferedImage placeholderImg = new BufferedImage(PREVIEW_SIZE, PREVIEW_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholderImg.createGraphics();
        
        g2d.setColor(Color.DARK_GRAY); // Fundo cinza escuro
        g2d.fillRect(0, 0, PREVIEW_SIZE, PREVIEW_SIZE);
        
        g2d.setColor(Color.RED); // Um '?' em vermelho
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.drawString("?", PREVIEW_SIZE / 2 - 12, PREVIEW_SIZE / 2 + 16); // Centraliza o '?'
        
        g2d.setColor(Color.WHITE); // Borda branca
        g2d.drawRect(2, 2, PREVIEW_SIZE - 4, PREVIEW_SIZE - 4);
        
        g2d.dispose();
        iconePlaceholder = new ImageIcon(placeholderImg);
    }

    // Carrega TODAS as imagens de carro de uma vez e as armazena no array 'iconesCarros'
    private void carregarIconesCarros() {
        iconesCarros = new ImageIcon[carros.length];
        
        for (int i = 0; i < carros.length; i++) {
            try {
                URL imgURL = getClass().getResource(carros[i]); // Tenta encontrar o arquivo
                
                if (imgURL != null) {
                    // Se encontrar, carrega e redimensiona
                    ImageIcon original = new ImageIcon(imgURL);
                    Image scaled = original.getImage().getScaledInstance(PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH);
                    iconesCarros[i] = new ImageIcon(scaled);
                } else {
                    // Se não encontrar (imgURL == null), usa o placeholder
                    System.err.println("Erro: Imagem do carro NÃO ENCONTRADA: " + carros[i]);
                    iconesCarros[i] = iconePlaceholder; // Impede o crash
                }
            } catch (Exception e) {
                // Em caso de qualquer outro erro de carregamento
                System.err.println("Erro ao carregar " + carros[i] + ": " + e.getMessage());
                iconesCarros[i] = iconePlaceholder; // Impede o crash
            }
        }
    }

    // Método para atualizar a imagem de pré-visualização do carro do Jogador 1
    private void atualizarPreviewCarro1() {
        previewCarro1.setIcon(iconesCarros[indiceCarro1]);
    }

    // Método para atualizar a imagem de pré-visualização do carro do Jogador 2
    private void atualizarPreviewCarro2() {
        previewCarro2.setIcon(iconesCarros[indiceCarro2]);
    }

    // Método para obter o caminho da imagem do carro selecionado pelo Jogador 1
    public String getCarroSelecionado1() {
        return carros[indiceCarro1];
    }

    // Método para obter o caminho da imagem do carro selecionado pelo Jogador 2
    public String getCarroSelecionado2() {
        return carros[indiceCarro2];
    }
}