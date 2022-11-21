package logica;

import java.util.ArrayList;
import java.util.List;

public class CadeiaMovimentos {

    private Peca pecaMovimentada;
    private List<Movimento> movimentos;
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public CadeiaMovimentos(Peca pecaMovimentada, List<Movimento> movimentos) {
        this.pecaMovimentada = pecaMovimentada;
        this.movimentos = movimentos;
    }

    public Peca getPecaMovimentada() {
        return pecaMovimentada;
    }

    public void setPecaMovimentada(Peca pecaMovimentada) {
        this.pecaMovimentada = pecaMovimentada;
    }

    public int getCapturas(){
        return pecasCapturadas.size();
    }

    public List<Movimento> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(List<Movimento> movimentos) {
        this.movimentos = movimentos;
    }

    public List<Peca> getPecasCapturadas() {
        return pecasCapturadas;
    }

    public void setPecasCapturadas(List<Peca> pecasCapturadas) {
        this.pecasCapturadas = pecasCapturadas;
    }
}
