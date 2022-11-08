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

    /**
     * Constroi uma peça.
     */
    public PecaPanel(Peca peca) {
        setPreferredSize(Util.TAMANHO_PECA);
        corPeca = peca.getCorPeca();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(corPeca.equals(CorPeca.BRANCA) ? Util.COR_PECA_BRANCA : Util.COR_PECA_PRETA);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }
}
