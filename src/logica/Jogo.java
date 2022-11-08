package logica;

import util.CorPeca;

/**
 * Classe contendo a lógica do jogo de damas
 *
 * TODO Salvar uma matriz com as peças para mantera a lógica no apartada da interface
 */
public class Jogo {

    /**
     * Verifica se uma posição deveria ter peça no inicio de um jogo
     * TODO Esse método foi feito mais para testes e deve ser aprofundado mais depois
     *
     * @return true caso possua peça false caso não
     */
    public static Peca obterPecaJogoPadrao(int x, int y){
        if(y <= 3 && (x + y) % 2 == 0){
            return new Peca(CorPeca.BRANCA);
        }
        if(y >= 6 && (x + y) % 2 == 0){
            return new Peca(CorPeca.PRETA);
        }
        return null;
    }

    /**
     * Verifica se um movimento é válido.
     * TODO Esse método foi feito mais para testes e deve ser aprofundado mais depois
     *
     * @return true caso o movimento seja válido, false caso não
     */
    public static boolean verificarMovimentoValido(Movimento movimento){
        /*
            Movimento válido:

            - A casa destino deve estar vazia
            - A casa de partida deve possuir uma peça
            - A casa de partida deve ser diferente da destino
            - Caso não ocorra uma captura e a peça não seja uma dama
              - A diferença de distância entre a partida e a saída em ambos os eixos deve ser igual à 1 ou -1
              - A peça também deverá seguir apenas para frente (o valor anterior deverá ser -1 para peças brancas e +1 para peças pretas
            - Caso uma captura possa ocorrer a captura é a única jogada válida
              - No caso da captura a distancia entre partida e saída em ambos os eixos deve ser igual a 2 ou -2 eu acho (sem dama)
              - No caso da peça parar em uma casa que possibilita outra captura, ela pode e deve realizar uma nova captura
              - A maior cadeia de caputuras é priorizada no caso de multiplas cadeias de captura
            - A dama não possui limite na movimentação contanto que não pule uma peça da mesma cor
         */


        //TODO hardcoded pra obrigar a só poder avançar
        if(movimento.getDeY() <= 3){
            return movimento.getDeY() - movimento.getAteY() == -1 && Math.abs(movimento.getDeX() - movimento.getAteX()) == 1;
        }
        if(movimento.getDeY() >= 6){
            return movimento.getDeY() - movimento.getAteY() == 1 && Math.abs(movimento.getDeX() - movimento.getAteX()) == 1;
        }

        return false;
    }

    /**
     * Tenta realizar um movimento.
     * TODO Esse método foi feito mais para testes e deve ser aprofundado mais depois
     * @param movimento movimento a ser realizado.
     * @return resultado da movimentação.
     */
    public static boolean mover(Movimento movimento){
        return true;
    }

}
