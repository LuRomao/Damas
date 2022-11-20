package gui;

import logica.Jogo;
import logica.Movimento;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Representa a interface gráfica de um tabuleiro de damas.
 */
public class TabuleiroPanel {

    private JFrame janela;
    private CasaPanel[][] casasArray = new CasaPanel[8][8];
    private Jogo jogo = new Jogo();

    private CasaPanel casaSelecionada = null;

    /**
     * Constroi a visualização de um tabuleiro criando uma janela e populando com as diversas casas do tabuleiro
     */
    public TabuleiroPanel(){
        jogo.iniciarJogo();

        janela = new JFrame("damas");
        janela.setLayout(new GridLayout(8, 8));

        CasaPanel teste = null;

        // Cria cada casa do tabuleiro
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                CasaPanel casaPanel = new CasaPanel(this, x, y);
                casasArray[y][x] = casaPanel;
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
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                casasArray[y][x].setBorder(null);
                casasArray[y][x].setJogadaValida(false);
            }
        }

        List<Movimento> movimentos = jogo.obterMovimentosPeca(casaClicada.getPosicaoX(), casaClicada.getPosicaoY());
        movimentos.forEach(m -> {
            casasArray[m.getAteY()][m.getAteX()].setJogadaValida(true);
            casasArray[m.getAteY()][m.getAteX()].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT));
        });

        casaClicada.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT_PECA_SELECIONADA));
    }

    /**
     * Movimenta uma peça de uma casa para outra.
     * @param casaClicada casa para o qual a peça sera movimentada.
     */
    public void moverPeca(CasaPanel casaClicada){
        if(jogo.mover(new Movimento(casaSelecionada.getPosicaoX(), casaSelecionada.getPosicaoY(), casaClicada.getPosicaoX(), casaClicada.getPosicaoY()))){
            PecaPanel peca = casaSelecionada.removerPeca();
            casaClicada.adicionarPeca(peca);
            for (int y = 0; y < 8; y++){
                for (int x = 0; x < 8; x++){
                    casasArray[y][x].setBorder(null);
                    casasArray[y][x].setJogadaValida(false);
                }
            }
            peca.setDama(jogo.pecaEhDama(casaClicada.getPosicaoX(), casaClicada.getPosicaoY()));
            jogo.passarTurno();
        }
    }

}
