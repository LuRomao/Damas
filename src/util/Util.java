package util;

import java.awt.*;
import java.util.List;


public class Util {

    public static final Dimension TAMANHO_TELA = new Dimension(800, 800);
    public static final Dimension TAMANHO_BORDA = new Dimension(700, 700);
    public static final Dimension TAMANHO_CASA = new Dimension(10, 10);
    public static final Dimension TAMANHO_PECA = new Dimension(50, 50);

    public static final int PADDING_PECA = -10;
    public static final int ESPACAMENTO_PECA = 8;
    public static final int ESPACAMENTO_SOMBRA = 2;

    public static final Color COR_CASA_BRANCA = Color.decode("#eee4e1");
    public static final Color COR_CASA_PRETA = Color.decode("#b2967d");
    public static final Color COR_PECA_BRANCA = Color.decode("#e3d0ca");
    public static final Color COR_PECA_PRETA = Color.decode("#523c29");
    public static final Color COR_HIGHLIGHT = Color.decode("#65eb9d");
    public static final Color COR_HIGHLIGHT_ENCADEAMENTO = Color.decode("#d0f5df");
    public static final Color COR_HIGHLIGHT_PECA_SELECIONADA = Color.decode("#8f7660");

    public static Color brightness(Color c, double scale) {
        int r = Math.min(255, (int) (c.getRed() * scale));
        int g = Math.min(255, (int) (c.getGreen() * scale));
        int b = Math.min(255, (int) (c.getBlue() * scale));
        return new Color(r,g,b);
    }

    public static int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }

    public static Color obterCorPorEnum(CorPeca corPeca){
        return corPeca.equals(CorPeca.BRANCA) ? COR_PECA_BRANCA : COR_PECA_PRETA;
    }

    public static <T> T obterUltimoElemento(List<T> lista){
        return lista.size() != 0 ? lista.get(lista.size() - 1) : null;
    }

    public static <T> T obterPrimeiroElemento(List<T> lista){
        return lista.size() != 0 ? lista.get(0) : null;
    }

    public static <T> boolean ultimoElemento(List<T> lista, T objeto){
        return lista.indexOf(objeto) == lista.size() - 1;
    }
}
