package gui;

import logica.Jogo;
import logica.Movimento;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a interface gráfica de um tabuleiro de damas.
 */
public class TabuleiroPanel {

    private JFrame janela;
    private List<CasaPanel> casas = new ArrayList<>();

    private CasaPanel casaSelecionada = null;

    /**
     * Constroi a visualização de um tabuleiro criando uma janela e populando com as diversas casas do tabuleiro
     */
    public TabuleiroPanel(){
        janela = new JFrame("damas");
        janela.setLayout(new GridLayout(8, 8));

        CasaPanel teste = null;

        // Cria cada casa do tabuleiro
        for (int y = 1; y <= 8; y++){
            for (int x = 1; x <= 8; x++){
                CasaPanel casaPanel = new CasaPanel(this, x, y);
                casas.add(casaPanel);
                janela.add(casaPanel);
            }
        }

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(Util.TAMANHO_TELA);
        janela.setVisible(true);
    }

    /**
     * Marca as casas que possuem jogadas válidas.
     * @param casaClicada a casa que parte o movimento.
     */
    public void marcarJogadasPossiveis(CasaPanel casaClicada){
        casaSelecionada = casaClicada;
        casas.forEach(c -> {
            c.setBorder(null);
            c.setJogadaValida(false);
            if(Jogo.verificarMovimentoValido(new Movimento(casaClicada.getPosicaoX(), casaClicada.getPosicaoY(), c.getPosicaoX(), c.getPosicaoY()))){
                c.setJogadaValida(true);
                c.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT));
            }
        });
        casaClicada.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT_PECA_SELECIONADA));
    }

    /**
     * Movimenta uma peça de uma casa para outra.
     * @param casaClicada casa para o qual a peça sera movimentada.
     */
    public void moverPeca(CasaPanel casaClicada){
        if(Jogo.mover(new Movimento(casaSelecionada.getPosicaoX(), casaSelecionada.getPosicaoY(), casaClicada.getPosicaoX(), casaClicada.getPosicaoY()))){
            PecaPanel peca = casaSelecionada.removerPeca();
            casaClicada.adicionarPeca(peca);
            casas.forEach(c -> {
                c.setBorder(null);
                c.setJogadaValida(false);
            });
        }
    }

}
