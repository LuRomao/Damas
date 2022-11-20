package gui;

import logica.CadeiaMovimentos;
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
    private CasaPanel[][] casasArray = new CasaPanel[8][8];
    private List<CasaPanel> casasMarcadas = new ArrayList<>();
    private Jogo jogo = new Jogo();
    private JPanel borda;

    private CasaPanel casaSelecionada = null;

    /**
     * Constroi a visualização de um tabuleiro criando uma janela e populando com as diversas casas do tabuleiro
     */
    public TabuleiroPanel(){
        jogo.iniciarJogo();

        borda = new JPanel();
        borda.setPreferredSize(Util.TAMANHO_BORDA);

        janela = new JFrame("damas");
        janela.setLayout(new GridBagLayout());
        janela.add(borda);

        alterarCorFundo();

        borda.setLayout(new GridLayout(8, 8));

        CasaPanel teste = null;

        // Cria cada casa do tabuleiro
        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                CasaPanel casaPanel = new CasaPanel(this, x, y);
                casasArray[y][x] = casaPanel;
                borda.add(casaPanel);
            }
        }

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(Util.TAMANHO_TELA);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    /**
     * Marca as casas que possuem jogadas válidas.
     * @param casaClicada a casa que parte o movimento.
     */
    public void marcarJogadasPossiveis(CasaPanel casaClicada){
        casaSelecionada = casaClicada;
        desmarcarCasas(false);

        List<CadeiaMovimentos> movimentos = jogo.obterMovimentosPeca(casaClicada.getPosicaoX(), casaClicada.getPosicaoY());
        movimentos.forEach(c -> c.getMovimentos().forEach(m -> {
            casasMarcadas.add(casasArray[m.getAteY()][m.getAteX()]);
            casasArray[m.getAteY()][m.getAteX()].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT_ENCADEAMENTO));

            if(Util.ultimoElemento(c.getMovimentos(), m)){
                casasArray[m.getAteY()][m.getAteX()].setJogadaValida(true);
                casasArray[m.getAteY()][m.getAteX()].getCadeiaMovimentos().add(c);
                casasArray[m.getAteY()][m.getAteX()].setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT));
            }
        }));

        casaClicada.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Util.COR_HIGHLIGHT_PECA_SELECIONADA));
        casasMarcadas.add(casaClicada);
    }

    /**
     * Movimenta uma peça de uma casa para outra.
     * @param casaClicada casa para o qual a peça sera movimentada.
     */
    public void moverPeca(CasaPanel casaClicada){
        CadeiaMovimentos cadeiaMovimentos = casaClicada.getCadeiaMovimentos().stream().filter(c -> c.getMovimentos().stream().findFirst().get().getDeX() == casaSelecionada.getPosicaoX() && c.getMovimentos().stream().findFirst().get().getDeY() == casaSelecionada.getPosicaoY()).toList().get(0);

        if(jogo.mover(cadeiaMovimentos)){
            PecaPanel peca = casaSelecionada.removerPeca();
            desmarcarCasas(true);
            casaClicada.adicionarPeca(peca);
            peca.setDama(jogo.pecaEhDama(casaClicada.getPosicaoX(), casaClicada.getPosicaoY()));
            jogo.passarTurno();
            alterarCorFundo();
        }
    }

    private void desmarcarCasas(boolean removerPecas){
        casasMarcadas.forEach(c -> {
            c.setBorder(null);
            c.setJogadaValida(false);
            c.setCadeiaMovimentos(new ArrayList<>());

            if(removerPecas && c.getPecaPanel() != null){
                c.removerPeca();
            }
        });
        casasMarcadas = new ArrayList<>();
    }

    private void alterarCorFundo(){
        Color corFundo = Util.obterCorPorEnum(jogo.getTurno());
        janela.getContentPane().setBackground(corFundo);
        borda.setBackground(corFundo);
    }

}
