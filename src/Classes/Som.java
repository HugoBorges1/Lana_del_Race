package Classes;

import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Som {
    private static Clip clipMusicaFundo; // Clip para a música de fundo
    private static Clip clipEfeitoSom;   // Clip para efeitos sonoros

    // Método para tocar a música de fundo
    public static void tocarMusicaV(String caminhoMusica) {
        try {
            if (clipMusicaFundo != null && clipMusicaFundo.isRunning()) {
                return; // Não toca novamente se já estiver tocando
            }

            InputStream inputStream = Som.class.getResourceAsStream(caminhoMusica);
            if (inputStream == null) {
                System.err.println("Arquivo de música não encontrado: " + caminhoMusica);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            clipMusicaFundo = AudioSystem.getClip();
            clipMusicaFundo.open(audioStream);
            clipMusicaFundo.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para tocar um efeito sonoro
    public static void tocarEfeitoSom(String caminhoSom) {
        try {
            InputStream inputStream = Som.class.getResourceAsStream(caminhoSom);
            if (inputStream == null) {
                System.err.println("Arquivo de efeito sonoro não encontrado: " + caminhoSom);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            clipEfeitoSom = AudioSystem.getClip();
            clipEfeitoSom.open(audioStream);
            clipEfeitoSom.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para parar a música de fundo
    public static void pararMusica(String caminhoMusica) {
        if (clipMusicaFundo != null && clipMusicaFundo.isRunning()) {
            clipMusicaFundo.stop();
            clipMusicaFundo.close();
            clipMusicaFundo = null;
        }
    }
}