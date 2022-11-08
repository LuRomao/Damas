package logica;

import util.CorPeca;

/**
 * Classe contendo lógica de uma peça especifica.
 */
public class Peca {

    private CorPeca corPeca;
    private boolean dama;

    public Peca(CorPeca corPeca) {
        this.corPeca = corPeca;
    }

    public CorPeca getCorPeca() {
        return corPeca;
    }

    public void setCorPeca(CorPeca corPeca) {
        this.corPeca = corPeca;
    }

    public boolean isDama() {
        return dama;
    }

    public void setDama(boolean dama) {
        this.dama = dama;
    }
}
