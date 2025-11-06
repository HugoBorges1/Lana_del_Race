package Classes;

import java.awt.Rectangle;

public class DeteccaoColisao {

    // Método para verificar colisão entre dois carros, retorna true se houver interseção
    public static boolean verificarColisao(Carro carro1, Carro carro2) {
        return carro1.getRetangulo().intersects(carro2.getRetangulo());
    }

    // Método para verificar se o carro saiu dos limites da janela
    // Retorna true se o carro estiver fora dos limites definidos
    public static boolean verificarLimites(Carro carro, int larguraJanela, int alturaJanela) {
        Rectangle ret = carro.getRetangulo();
        return ret.x < 0 || ret.x + ret.width > larguraJanela || ret.y < 0 || ret.y + ret.height > alturaJanela;
    }

    // Método para verificar colisão com os limites da pista 1
    // Retorna true se o carro colidir com as barreiras externas ou internas
    public static boolean verificarColisaoComPista1(Carro carro) {
        // Define a área externa e interna da pista
        Rectangle areaExterna = new Rectangle(100, 100, 1720, 880); // Área total externa da pista
        Rectangle areaInterna = new Rectangle(300, 300, 1320, 480); // Área interna onde os carros não podem ir
        Rectangle ret = carro.getRetangulo();

        // Verifica se o carro está fora da área permitida
        if (!areaExterna.contains(ret) || areaInterna.intersects(ret)) {
            return true; // Colidiu com barreiras
        }
        return false; // Não colidiu
    }
}
