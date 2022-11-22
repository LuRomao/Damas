package gui;

import logica.CadeiaMovimentos;
import logica.Casa;
import logica.Jogo;
import logica.Peca;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a visualização de uma casa do tabuleiro de damas
 */
class CasaPanel extends JPanel {

    private int posicaoX, posicaoY;
    private PecaPanel pecaPanel = null;
    private boolean jogadaValida = false;
    private List<CadeiaMovimentos> cadeiaMovimentos = new ArrayList<>();

    private Color corPadrao;
    private TabuleiroPanel tabuleiroPanel;


    /**
     * Constroi uma casa, e popula ela com peças caso necessário.
     *
     * @param posicaoX coordenada x do tabuleiro.
     * @param posicaoY coordenada y do tabuleiro.
     */
    public CasaPanel(TabuleiroPanel tabuleiroPanel, int posicaoX, int posicaoY) {
        this.tabuleiroPanel = tabuleiroPanel;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;

        corPadrao = (posicaoX + posicaoY) % 2 != 0 ? Util.COR_CASA_PRETA : Util.COR_CASA_BRANCA;
        setBackground(corPadrao);
        setPreferredSize(Util.TAMANHO_CASA);
        setLayout(new GridBagLayout());

        Peca peca = Jogo.obterPecaJogoPadrao(new Casa(posicaoX, posicaoY));
        if (peca != null) {
            pecaPanel = new PecaPanel(peca);
            add(pecaPanel);
        }

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickCasa();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Util.brightness(corPadrao, 0.9));
                tabuleiroPanel.escreverPosicao(posicaoX, posicaoY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(corPadrao);
            }
        });
    }

    /**
     * Chamado ao clicar em uma casa.
     */
    private void onClickCasa(){
        if(pecaPanel != null){
            tabuleiroPanel.marcarJogadasPossiveis(this);
        } else if (jogadaValida) {
            tabuleiroPanel.moverPeca(this);
        }
    }

    /**
     * Remove a peça da casa.
     */
    public PecaPanel removerPeca(){
        PecaPanel peca = (PecaPanel) getComponent(0);
        removeAll();
        repaint();
        pecaPanel = null;
        return peca;
    }

    /**
     * Adiciona uma peça à casa.
     * @param peca Peça a ser adicionada
     */
    public void adicionarPeca(PecaPanel peca){
        pecaPanel = peca;
        add(pecaPanel);
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }

    public PecaPanel getPecaPanel() {
        return pecaPanel;
    }

    public void setPecaPanel(PecaPanel pecaPanel) {
        this.pecaPanel = pecaPanel;
    }

    public Color getCorPadrao() {
        return corPadrao;
    }

    public void setCorPadrao(Color corPadrao) {
        this.corPadrao = corPadrao;
    }

    public TabuleiroPanel getTabuleiroPanel() {
        return tabuleiroPanel;
    }

    public void setTabuleiroPanel(TabuleiroPanel tabuleiroPanel) {
        this.tabuleiroPanel = tabuleiroPanel;
    }

    public boolean isJogadaValida() {
        return jogadaValida;
    }

    public void setJogadaValida(boolean jogadaValida) {
        this.jogadaValida = jogadaValida;
    }

    public List<CadeiaMovimentos> getCadeiaMovimentos() {
        return cadeiaMovimentos;
    }

    public void setCadeiaMovimentos(List<CadeiaMovimentos> cadeiaMovimentos) {
        this.cadeiaMovimentos = cadeiaMovimentos;
    }
}
