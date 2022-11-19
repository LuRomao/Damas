package logica;

/**
 * Representa uma casa na lógica do jogo
 */
public class Casa {

    private Peca peca;
    private int linha, coluna;

    public Casa(int coluna, int linha) {
        this.coluna = coluna;
        this.linha = linha;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
}
