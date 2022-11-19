package logica;

import util.CorPeca;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe contendo a lógica do jogo de damas
 *
 * TODO Salvar uma matriz com as peças para mantera a lógica no apartada da interface
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
        // NO caso de não ser Dama
        List<Peca> pecasTurno = turno.equals(CorPeca.BRANCA) ? pecasBrancasEmJogo : pecasPretasEmJogo;

        pecasTurno.forEach(peca -> {
            peca.getMovimentos().clear();

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
        });
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
            return new Peca(CorPeca.BRANCA, casa);
        }
        if(casa.getLinha() >= 5 && (casa.getLinha() + casa.getColuna()) % 2 == 0){
            return new Peca(CorPeca.PRETA, casa);
        }
        return null;
    }

    public List<Movimento> obterMovimentosPeca(int x, int y){
        Peca peca = casas[y][x].getPeca();
        if(peca != null && peca.getCorPeca().equals(turno)){
            return peca.getMovimentos();
        }
        return new ArrayList<>();
    }

    /**
     * Tenta realizar um movimento.
     * TODO Esse método foi feito mais para testes e deve ser aprofundado mais depois
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
        return true;
    }

    public void printBoard(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                System.out.print("|");
                if (casas[y][x].getPeca() != null){
                    System.out.print("o");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.print("|\n");
        }
    }

}
