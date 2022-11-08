package logica;

/**
 * Classe representando um movimento no jogo.
 */
public class Movimento {

    private int deX, deY, ateX, ateY;

    public Movimento(int deX, int deY, int ateX, int ateY) {
        this.deX = deX;
        this.deY = deY;
        this.ateX = ateX;
        this.ateY = ateY;
    }

    public int getDeX() {
        return deX;
    }

    public void setDeX(int deX) {
        this.deX = deX;
    }

    public int getDeY() {
        return deY;
    }

    public void setDeY(int deY) {
        this.deY = deY;
    }

    public int getAteX() {
        return ateX;
    }

    public void setAteX(int ateX) {
        this.ateX = ateX;
    }

    public int getAteY() {
        return ateY;
    }

    public void setAteY(int ateY) {
        this.ateY = ateY;
    }
}
