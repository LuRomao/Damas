package logica;

/**
 * Representa uma casa na lógica do jogo
 */
public class Casa {

    private Peca peca;
    private int x, y;

    public Casa(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
