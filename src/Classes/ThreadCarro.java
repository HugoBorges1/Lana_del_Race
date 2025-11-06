package Classes;

public class ThreadCarro extends Thread {
    private Carro carro;
    private Carro outroCarro; // Adicionar referência ao outro carro
    private int pista; // Número da pista

    public ThreadCarro(Carro carro, Carro outroCarro, int pista) {
        this.carro = carro;
        this.outroCarro = outroCarro; // Inicializar o outro carro
        this.pista = pista; // Inicializar o número da pista
    }

    @Override
    public void run() {
        while (true) {
            carro.mover(pista, outroCarro); // Passa a pista e o outro carro como parâmetros
            try {
                Thread.sleep(20); // Controla a velocidade da thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
