package logica;

import util.CorPeca;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe contendo a lógica do jogo de damas
 *
 */
public class Jogo {

    private Casa[][] casas = new Casa[8][8];

    private List<Peca> pecasBrancasEmJogo = new ArrayList<>();
    private List<Peca> pecasPretasEmJogo = new ArrayList<>();

    private List<Movimento> movimentosBrancas = new ArrayList<>();
    private List<Movimento> movimentosPretas = new ArrayList<>();

    private CorPeca turno = CorPeca.BRANCA;

    /**
     * Método que inicia o jogo
     */
    public void iniciarJogo(){
        //Loop em todas as posições do tabuleiro inserindo as peças
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Casa casa = new Casa(x, y);
                Peca peca = obterPecaJogoPadrao(casa);
                casa.setPeca(peca);

                casas[y][x] = casa;

                if(peca == null){
                    continue;
                }

                switch (peca.getCorPeca()){
                    case BRANCA -> pecasBrancasEmJogo.add(peca);
                    case PRETA -> pecasPretasEmJogo.add(peca);
                }
            }
        }
        printBoard();
        calcularJogadasTurno();
    }

    /**
     * Método que calcula os movimentos possíveis de uma peça
     */
    public void calcularJogadasTurno(){
        List<Peca> pecasTurno = turno.equals(CorPeca.BRANCA) ? pecasBrancasEmJogo : pecasPretasEmJogo;

        pecasTurno.forEach(peca -> {
            peca.getMovimentos().clear();

            if(!peca.isDama()){
                //Caso o movimento seja para um valor y válido
                int direcao = peca.getCasa().getLinha() + turno.getDirecao();
                if(direcao >= 0 && direcao <= 7){

                    //Caso possa se mover para a direita
                    int direita = peca.getCasa().getColuna() + 1;
                    if(direita >= 0 && direita <= 7){
                        Casa casaDiagonalDireita = casas[direcao][direita];
                        if (casaDiagonalDireita.getPeca() == null){
                            peca.getMovimentos().add(new Movimento(peca.getCasa(), casaDiagonalDireita));
                        }
                    }

                    //Caso possa se mover para a esquerda
                    int esquerda = peca.getCasa().getColuna() - 1;
                    if(esquerda >= 0 && esquerda <= 7){
                        Casa casaDiagonalEsquerda = casas[direcao][esquerda];
                        if (casaDiagonalEsquerda.getPeca() == null){
                            peca.getMovimentos().add(new Movimento(peca.getCasa(), casaDiagonalEsquerda));
                        }
                    }
                }
            } else {
                adicionarMovimentosDiagonais(peca, 1, 1);
                adicionarMovimentosDiagonais(peca, 1, -1);
                adicionarMovimentosDiagonais(peca, -1, 1);
                adicionarMovimentosDiagonais(peca, -1, -1);
            }

        });
    }

    /**
     * Adiciona os movimentos diagonais da dama
     * @param peca dama.
     * @param xDir direção horizontal
     * @param yDir direção vertical.
     */
    private void adicionarMovimentosDiagonais(Peca peca, int xDir, int yDir){
        for(int x = peca.getCasa().getColuna() + xDir, y = peca.getCasa().getLinha() + yDir; posicaoDentroDoTabuleiro(x, y); x = x + xDir, y = y + yDir){
            if(casas[y][x].getPeca() == null){
                peca.getMovimentos().add(new Movimento(peca.getCasa(), casas[y][x]));
            } else {
                break;
            }
        }
    }

    /**
     * Método chamado para passar o turno
     */
    public void passarTurno(){
        switch (turno){
            case BRANCA -> turno = CorPeca.PRETA;
            case PRETA -> turno = CorPeca.BRANCA;
        }

        calcularJogadasTurno();
    }

    /**
     * Verifica se uma posição deveria ter peça no inicio de um jogo
     *
     * @return true caso possua peça false caso não
     */
    public static Peca obterPecaJogoPadrao(Casa casa){
        if(casa.getLinha() <= 2 && (casa.getLinha() + casa.getColuna()) % 2 == 0){
            return new Peca(CorPeca.PRETA, casa);
        }
        if(casa.getLinha() >= 5 && (casa.getLinha() + casa.getColuna()) % 2 == 0){
            return new Peca(CorPeca.BRANCA, casa);
        }
        return null;
    }

    /**
     * Obtem os movimentos válidos de uma peça.
     * @param x coluna da peça
     * @param y linha da peça
     * @return lista de movimentos válidos
     */
    public List<Movimento> obterMovimentosPeca(int x, int y){
        Peca peca = casas[y][x].getPeca();
        if(peca != null && peca.getCorPeca().equals(turno)){
            return peca.getMovimentos();
        }
        return new ArrayList<>();
    }

    /**
     * Verifica se uma peça é dama
     * @param x coluna da peça
     * @param y linha da peça
     * @return true caso seja dama false caso contrario
     */
    public boolean pecaEhDama(int x, int y){
        Peca peca = casas[y][x].getPeca();
        return peca != null && peca.isDama();
    }

    /**
     * Tenta realizar um movimento.
     * @param movimento movimento a ser realizado.
     * @return resultado da movimentação.
     */
    public boolean mover(Movimento movimento){
        Casa casaDe = casas[movimento.getDeY()][movimento.getDeX()];
        Casa casaAte = casas[movimento.getAteY()][movimento.getAteX()];
        Peca peca = casaDe.getPeca();

        casaDe.setPeca(null);
        casaAte.setPeca(peca);
        peca.setCasa(casaAte);

        if(peca.getCorPeca().equals(CorPeca.BRANCA) && casaAte.getLinha() == 0
                || peca.getCorPeca().equals(CorPeca.PRETA) && casaAte.getLinha() == 7){
            peca.setDama(true);
        }

        printBoard();
        return true;
    }

    /**
     * Verifica se uma posição existe dentro do tabuleiro
     * @param x coluna
     * @param y linha
     * @return true caso exista false caso contrario
     */
    public boolean posicaoDentroDoTabuleiro(int x, int y){
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    /**
     * Printa o estado do tabuleiro para fins de debug
     */
    public void printBoard(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                System.out.print("|");
                if (casas[y][x].getPeca() != null){
                    switch (casas[y][x].getPeca().getCorPeca()){
                        case BRANCA -> System.out.print("B");
                        case PRETA -> System.out.print("P");
                    }
                } else {
                    System.out.print(" ");
                }
            }
            System.out.print("|\n");
        }
        System.out.print("\n");
    }

}
