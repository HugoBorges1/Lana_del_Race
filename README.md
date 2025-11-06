# 🚀 Lana del Race

> Um jogo de corrida 2D *multiplayer* local, desenvolvido em Java com o tema de Lana Del Rey e Kanye West.

<p align="center">
    <img src="https://media1.tenor.com/m/MBzBtemCo_EAAAAC/lana-del-rey-kanye-west.gif" alt="GIF temático do projeto">
</p>

## 📖 Sobre o Projeto

Este projeto foi desenvolvido para a disciplina de Computação Gráfica. A proposta era criar um jogo de corrida *top-down* (visto de cima) em Java com tema livre.

O resultado é **Lana del Race**, um jogo para dois jogadores locais onde você pode escolher entre 10 carros temáticos (baseados em álbuns da Lana Del Rey) e competir em uma pista com uma foto do Kanye West, desviando de obstáculos e tentando completar as voltas primeiro.

---

## ✨ Funcionalidades Principais

* **Multiplayer Local:** Jogue com um amigo na mesma máquina.
* **Seleção de Carros:** Escolha entre 10 carros temáticos diferentes.
* **Contagem de Voltas:** O jogo define um vencedor com base no número de voltas estipulado no menu.
* **Obstáculos Dinâmicos:** Obstáculos aparecem aleatoriamente na pista. Colidir com eles causa uma redução temporária de velocidade.
* **Detecção de Colisão:**
    * **Carro vs. Pista:** Bater nas paredes da pista para o carro.
    * **Carro vs. Carro:** Colidir com o outro jogador para ambos os carros e toca um som de colisão.
* **Configuração de Jogo:** Escolha o número de voltas antes de iniciar a corrida.
* **Trilha Sonora:**
    * Música de fundo no menu.
    * Música de corrida selecionada aleatoriamente (3 opções).
    * Música de vitória diferente para cada jogador.
    * Efeitos sonoros para colisões.

---

## 🕹️ Como Jogar

### Controles

| Ação | Jogador 1 | Jogador 2 |
| :--- | :---: | :---: |
| **Acelerar** | `W` | `Seta para Cima` |
| **Frear/Ré** | `S` | `Seta para Baixo` |
| **Virar Esquerda**| `A` | `Seta para Esquerda` |
| **Virar Direita** | `D` | `Seta para Direita` |

### Executando o Jogo

1.  **Pré-requisito:** Você precisa ter o **Java (JRE)** instalado em sua máquina.
2.  **Baixar:** Faça o download do arquivo `Lana_del_race.jar` (disponível na [página de Releases](httpsa://github.com/HugoBorges1/Lana_del_Race/releases) deste repositório).
3.  **Executar:** Você pode executar o jogo de duas maneiras:
    * **Clique Duplo:** Na maioria dos sistemas, basta dar um clique duplo no arquivo `.jar`.
    * **Via Terminal:** Se o clique duplo não funcionar, abra um terminal na pasta onde o arquivo está e digite:
        ```bash
        java -jar Lana_del_race.jar
        ```

---

## 🛠️ Para Desenvolvedores

### Tecnologias Utilizadas

* **Java**: Linguagem principal.
* **Java Swing**: Para toda a interface gráfica (janelas, botões, painéis).
* **Java AWT**: Para renderização 2D (`Graphics`, `Graphics2D`), transformação de imagem (`AffineTransform`) e gerenciamento de eventos (`KeyListener`).
* **Java Image I/O**: Para carregar e exibir imagens (`.png`, `.jpg`).
* **Java Sound (javax.sound.sampled)**: Para tocar música de fundo e efeitos sonoros (`.wav`).
* **Git LFS**: Para gerenciar o arquivo `.jar` de build (maior que 100MB) no repositório.

### Como Executar o Código-Fonte

1.  **Pré-requisitos:**
    * **JDK** (Java Development Kit) 11 ou superior.
    * **Git** e **Git LFS** (para clonar o repositório corretamente).
2.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/HugoBorges1/Lana_del_Race.git](https://github.com/HugoBorges1/Lana_del_Race.git)
    cd Lana_del_Race
    ```
3.  **Abra em uma IDE:**
    * Abra o projeto na sua IDE Java favorita (VS Code com o "Extension Pack for Java", IntelliJ IDEA, ou Eclipse).
    * Localize o arquivo `Classes/JogoDeCorrida.java`.
    * Execute o método `main()` contido nele.

### Estrutura do Projeto

O código é todo orientado a objetos e está dividido no pacote `Classes`:

* `JogoDeCorrida.java`: **(Classe Principal)**
    * Contém o método `main()`, que inicializa o menu principal (`JFrame`).
    * Gerencia o loop principal do jogo (`iniciarJogo`) em uma nova `Thread`.
    * Gerencia o estado do jogo (`jogoAtivo`), voltas e spawn de obstáculos.
    * Implementa o `KeyListener` para capturar os comandos dos jogadores.
    * Renderiza a pista (`desenharPista`) e as informações (`desenharInformacoes`).

* `Carro.java`: **(Objeto Principal do Jogo)**
    * Define toda a física do carro: posição (`x`, `y`), `velocidade`, `aceleracao`, `angulo`, `rotacao`.
    * Carrega a própria imagem (sprite) e a renderiza com rotação (`AffineTransform`).
    * Gerencia o estado individual do jogador: `voltas`, `passouPelaLinhaDeChegada`.
    * Define a lógica de colisão (`colidirCom`) e interação com obstáculos (`reduzirVelocidadeTemporariamente`).

* `DeteccaoColisao.java`: **(Utilitário)**
    * Classe estática com métodos para verificar colisões.
    * `verificarColisao(carro1, carro2)`: Usa `Rectangle.intersects()` para colisões carro-carro.
    * `verificarColisaoComPista1(carro)`: Usa `Rectangle.contains()` e `Rectangle.intersects()` para verificar se o carro está dentro dos limites da pista.

* `Obstacle.java`:
    * Define o objeto "obstáculo".
    * É instanciado em uma posição aleatória (`Math.random()`), garantindo que não apareça no meio da pista.
    * Fornece seu `Rectangle` para detecção de colisão.

* `SelecaoCarro.java`: **(Componente de UI)**
    * Um `JPanel` customizado usado no menu.
    * Gerencia a lógica de seleção de carros com botões `<` e `>`.
    * Pré-carrega e redimensiona as imagens dos carros (`ImageIcon`) para melhor performance.

* `PainelFundo.java`: **(Componente de UI)**
    * Um `JPanel` simples que desenha uma imagem de fundo (`lanadelmengo.jpg`) no menu.

* `Som.java`: **(Utilitário)**
    * Classe estática para gerenciar todo o áudio.
    * Usa `javax.sound.sampled.Clip` para tocar e parar músicas (`tocarMusicaV`, `pararMusica`) e efeitos (`tocarEfeitoSom`).

*(Nota: O arquivo `ThreadCarro.java` existe, mas a lógica de threading principal é implementada diretamente em `JogoDeCorrida.java` através de uma expressão lambda `new Thread(() -> { ... })`)*.