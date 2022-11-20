package gui;

import logica.Peca;
import util.CorPeca;
import util.Util;

import javax.swing.*;
import java.awt.*;

/**
 * Representa a visualização de uma peça do tabuleiro
 */
class PecaPanel extends JPanel {

    private CorPeca corPeca;
    private boolean dama = false;

    /**
     * Constroi uma peça.
     */
    public PecaPanel(Peca peca) {
        setPreferredSize(Util.TAMANHO_PECA);
        corPeca = peca.getCorPeca();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calculos que literalmente só tem o propósito de alinhar o desenho das peças
        int offset = Util.PADDING_PECA / 2;
        if (dama){
            desenharPeca(g, -(Util.ESPACAMENTO_PECA / 2) - offset, -(Util.ESPACAMENTO_PECA / 2) - offset);
            desenharPeca(g, (Util.ESPACAMENTO_PECA / 2) - offset, (Util.ESPACAMENTO_PECA / 2) - offset);
        } else {
            desenharPeca(g, -offset, -offset);
        }
    }

    private void desenharPeca(Graphics g, int x, int y){
        Color corPrimaria = corPeca.equals(CorPeca.BRANCA) ? Util.COR_PECA_BRANCA : Util.COR_PECA_PRETA;
        Color corSombra = corPeca.equals(CorPeca.BRANCA) ? Util.COR_PECA_PRETA : Util.COR_PECA_BRANCA;

        g.setColor(corSombra);
        g.fillOval(x - (Util.ESPACAMENTO_SOMBRA / 2), y - (Util.ESPACAMENTO_SOMBRA / 2), g.getClipBounds().width + Util.PADDING_PECA, g.getClipBounds().height + Util.PADDING_PECA);
        g.setColor(corPrimaria);
        g.fillOval(x + (Util.ESPACAMENTO_SOMBRA / 2), y + (Util.ESPACAMENTO_SOMBRA / 2), g.getClipBounds().width + Util.PADDING_PECA, g.getClipBounds().height + Util.PADDING_PECA);

    }

    public boolean isDama() {
        return dama;
    }

    public void setDama(boolean dama) {
        this.dama = dama;
        repaint();
    }
}
