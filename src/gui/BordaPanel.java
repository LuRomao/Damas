package gui;

import util.CorPeca;
import util.Util;

import javax.swing.*;

public class BordaPanel extends JPanel {

    private CorPeca turno;

    public BordaPanel(CorPeca corPeca){
        setBackground(Util.obterCorPorEnum(corPeca));
        setPreferredSize(Util.TAMANHO_BORDA);
    }

    public CorPeca getTurno() {
        return turno;
    }

    public void setTurno(CorPeca turno) {
        this.turno = turno;
        setBackground(Util.obterCorPorEnum(turno));
    }
}
