package logica;

import util.CorPeca;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe contendo lógica de uma peça especifica.
 */
public class Peca {

    private Casa casa;
    private CorPeca corPeca;
    private boolean dama;
    private List<Movimento> movimentos = new ArrayList<>();

    public Peca(CorPeca corPeca, Casa casa) {
        this.corPeca = corPeca;
        this.casa = casa;
    }

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
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

    public List<Movimento> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(List<Movimento> movimentos) {
        this.movimentos = movimentos;
    }
}
