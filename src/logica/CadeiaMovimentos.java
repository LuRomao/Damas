package logica;

import java.util.List;

public class CadeiaMovimentos {

    private List<Movimento> movimentos;
    private List<Peca> pecasCapturadas;

    public CadeiaMovimentos(List<Movimento> movimentos) {
        this.movimentos = movimentos;
    }

    public int getCapturas(){
        return movimentos.size();
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
