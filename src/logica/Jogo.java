package logica;

import util.CorPeca;
import util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe contendo a lógica do jogo de damas
 *
 */
public class Jogo {

    private Casa[][] casas = new Casa[8][8];

    private List<Peca> pecasBrancasEmJogo = new ArrayList<>();
    private List<Peca> pecasPretasEmJogo = new ArrayList<>();

    private List<CadeiaMovimentos> movimentosBrancas = new ArrayList<>();
    private List<CadeiaMovimentos> movimentosPretas = new ArrayList<>();

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
        List<Peca> pecasTurno = obterPecas(turno);
        setMovimentosTurnoAtual(new ArrayList<>());

        pecasTurno.forEach(peca -> {
            peca.setMovimentos(new ArrayList<>());

            if(!peca.isDama()){
                computarJogadasCasa(peca.getCasa(), null, false, true, null, peca);
            } else {
                computarJogadasCasaDama(peca.getCasa(), null, false, null, peca);
            }

        });
        obterMovimentosTurnoAtual().stream().forEach(m -> m.getPecaMovimentada().getMovimentos().add(m));
        System.out.println("Movimentos Possiveis: " + obterMovimentosTurnoAtual().size());
    }

    private void computarJogadasCasa(Casa casaAtual, Casa casaAnterior, boolean esperaPeca, boolean primeiraPassagem, CadeiaMovimentos cadeiaMovimentos, Peca peca){
        List<Casa> movimentosBasicos = obterCasasMovimentoBasico(casaAnterior, casaAtual, primeiraPassagem, false);

        //Primeira iteração
        if(primeiraPassagem){
            for(Casa c : movimentosBasicos){

                cadeiaMovimentos = new CadeiaMovimentos();
                cadeiaMovimentos.getMovimentos().add(new Movimento(casaAtual, c));
                cadeiaMovimentos.setPecaMovimentada(peca);

                // Caso não tenha uma peça na casa verificada
                if(c.getPeca() == null){
                    tentarAdicionarCadeiaDeMovimentos(cadeiaMovimentos);

                } else if (!peca.getCorPeca().equals(c.getPeca().getCorPeca())){
                    computarJogadasCasa(c, casaAtual, false, false,cadeiaMovimentos, peca);
                }
            }

        } else {
            for(Casa c : movimentosBasicos){

                CadeiaMovimentos novaCadeiaMovimentos = new CadeiaMovimentos(cadeiaMovimentos);
                novaCadeiaMovimentos.getMovimentos().add(new Movimento(casaAtual, c));

                //Caso espere uma peça e possua uma peça
                if(c.getPeca() != null && esperaPeca && !peca.getCorPeca().equals(c.getPeca().getCorPeca()) && !cadeiaMovimentos.getPecasCapturadas().stream().anyMatch(p -> p.equals(c.getPeca()))){
                    novaCadeiaMovimentos.getPecasCapturadas().add(c.getPeca());
                    computarJogadasCasa(c, casaAtual, false, false, novaCadeiaMovimentos, peca);
                }

                //Caso não espere peça e não possua peça
                if(c.getPeca() == null && !esperaPeca){
                    computarJogadasCasa(c, null, true, false, novaCadeiaMovimentos, peca);
                }
            }

            tentarAdicionarCadeiaDeMovimentos(cadeiaMovimentos);
        }

    }

    private void computarJogadasCasaDama(Casa casaAtual, Casa casaAnterior, boolean continuarDirecao, CadeiaMovimentos cadeiaMovimentos, Peca peca){
        if(cadeiaMovimentos == null){
            cadeiaMovimentos = new CadeiaMovimentos(peca, new ArrayList<>());
        }

        //Caso tenha uma captura, só tenta adicionar a cadeia de movimentos após outro caso de captura
        //TODO TERMINAR LOGICA DA DAMA
        if(cadeiaMovimentos.getCapturas() > 0){
            //Lógica de movimentação após a primeira captura

        } else {
            List<Casa> movimentosBasicos = obterCasasMovimentoBasico(continuarDirecao ? casaAnterior : null, casaAtual, false, false);

            for(Casa c : movimentosBasicos){

                CadeiaMovimentos newCadeia = new CadeiaMovimentos(cadeiaMovimentos);
                newCadeia.getMovimentos().add(new Movimento(casaAtual, c));
                continuarDirecao = true;

                if(casaAtual.getPeca() != null && !casaAtual.getPeca().equals(peca)){
                    newCadeia.getPecasCapturadas().add(casaAtual.getPeca());
                    continuarDirecao = false;
                }

                computarJogadasCasaDama(c, casaAtual, continuarDirecao, newCadeia, peca);
                tentarAdicionarCadeiaDeMovimentos(newCadeia);
            }
        }
    }

    private void tentarAdicionarCadeiaDeMovimentos(CadeiaMovimentos cadeiaMovimentos){
        List<Peca> pecasCapturadas = new ArrayList<>();
        Movimento ultimoMovimento = null;

        // Obtem as peças capturadas em um encadeamento e salva o ultimo movimento para ser removido caso contenha uma peça
        for(Movimento movimento : cadeiaMovimentos.getMovimentos()){
            if(casas[movimento.getAteY()][movimento.getAteX()].getPeca() != null){
                if(!Util.ultimoElemento(cadeiaMovimentos.getMovimentos(), movimento)){
                    pecasCapturadas.add(casas[movimento.getAteY()][movimento.getAteX()].getPeca());
                } else {
                    ultimoMovimento = movimento;
                }
            }
        }

        // Caso o ultimo movimento contenha uma peça o remove
        if(ultimoMovimento != null){
            cadeiaMovimentos.getMovimentos().remove(ultimoMovimento);
        }

        cadeiaMovimentos.setPecasCapturadas(pecasCapturadas);

        // Obtém uma lista ordenada de cadeias de movimento com capturas maiores ou iguais a cadeia atual
        List<CadeiaMovimentos> cadeiaMaiorOuIgual = obterMovimentosTurnoAtual().stream()
                .filter(c -> c.getCapturas() >= cadeiaMovimentos.getCapturas())
                .sorted(Comparator.comparingInt(CadeiaMovimentos::getCapturas).reversed()).toList();

        List<CadeiaMovimentos> cadeiasValidas = new ArrayList<>(cadeiaMaiorOuIgual);

        // Caso não possua cadeia de movimento ou possua uma com o mesmo tamanho apenas adiciona a lista
        // e caso possua uma cadeia menor que a atual substitui ela pela a atual
        if(cadeiaMaiorOuIgual.size() == 0 || cadeiaMaiorOuIgual.get(0).getCapturas() == cadeiaMovimentos.getCapturas()){
            cadeiasValidas.add(cadeiaMovimentos);

        } else if(cadeiaMaiorOuIgual.get(0).getCapturas() < cadeiaMovimentos.getCapturas()){
            cadeiasValidas = new ArrayList<>();
            cadeiasValidas.add(cadeiaMovimentos);
        }

        setMovimentosTurnoAtual(cadeiasValidas);
    }

    public List<Peca> obterPecas(CorPeca cor){
        return cor.equals(CorPeca.BRANCA) ? pecasBrancasEmJogo : pecasPretasEmJogo;
    }

    public List<CadeiaMovimentos> obterMovimentosTurnoAtual(){
        return turno.equals(CorPeca.BRANCA) ? movimentosBrancas : movimentosPretas;
    }

    private void setMovimentosTurnoAtual(List<CadeiaMovimentos> movimentos){
        switch (turno){
            case BRANCA -> movimentosBrancas = movimentos;
            case PRETA -> movimentosPretas = movimentos;
        }
    }

    private List<Casa> obterCasasMovimentoBasico(Casa casaAnterior, Casa casaAtual, boolean apenasFrente, boolean removerUltimaDirecao){
        List<Casa> resultado = new ArrayList<>();

        if(casaAnterior != null){
            int x = (casaAtual.getX() - casaAnterior.getX() ) + casaAtual.getX();
            int y = (casaAtual.getY() - casaAnterior.getY()) + casaAtual.getY();

            if(posicaoDentroDoTabuleiro(x, y)){
                resultado.add(casas[y][x]);
            }

        } else {

            int frente = casaAtual.getY() + turno.getDirecao();

            int direita = casaAtual.getX() + 1;
            int esquerda = casaAtual.getX() - 1;

            if(posicaoDentroDoTabuleiro(esquerda, frente)){
                resultado.add(casas[frente][esquerda]);
            }

            if(posicaoDentroDoTabuleiro(direita, frente)){
                resultado.add(casas[frente][direita]);
            }

            if(!apenasFrente){
                int atras = casaAtual.getY() + (turno.getDirecao() * -1);

                if(posicaoDentroDoTabuleiro(direita, atras)){
                    resultado.add(casas[atras][direita]);
                }

                if(posicaoDentroDoTabuleiro(esquerda, atras)){
                    resultado.add(casas[atras][esquerda]);
                }
            }

        }

        return resultado;
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
        if(casa.getY() <= 2 && (casa.getY() + casa.getX()) % 2 != 0){
            return new Peca(CorPeca.PRETA, casa);
        }
        if(casa.getY() >= 5 && (casa.getY() + casa.getX()) % 2 != 0){
            return new Peca(CorPeca.BRANCA, casa);
        }
        return null;
    }

    /**
     * Obtem os movimentos válidos de uma peça.
     * @param x eixo x da peça
     * @param y eixo y da peça
     * @return lista de movimentos válidos
     */
    public List<CadeiaMovimentos> obterMovimentosPeca(int x, int y){
        Peca peca = casas[y][x].getPeca();
        if(peca != null && peca.getCorPeca().equals(turno)){
            return peca.getMovimentos();
        }
        return new ArrayList<>();
    }

    /**
     * Verifica se uma peça é dama
     * @param x eixo x da peça
     * @param y eixo y da peça
     * @return true caso seja dama false caso contrario
     */
    public boolean pecaEhDama(int x, int y){
        Peca peca = casas[y][x].getPeca();
        return peca != null && peca.isDama();
    }

    /**
     * Tenta realizar um movimento.
     * @param cadeiaMovimentos cadeoa de movimentos a serem realizados.
     * @return resultado da movimentação.
     */
    public boolean mover(CadeiaMovimentos cadeiaMovimentos){
        Movimento primeiroMovimento = Util.obterPrimeiroElemento(cadeiaMovimentos.getMovimentos());
        Peca peca =  casas[primeiroMovimento.getDeY()][primeiroMovimento.getDeX()].getPeca();
        Casa casaFinal = null;

        for(Movimento movimento : cadeiaMovimentos.getMovimentos()){
            Casa casaDe = casas[movimento.getDeY()][movimento.getDeX()];
            Peca pecaCapturada = casaDe.getPeca();

            if(pecaCapturada != null && !pecaCapturada.getCorPeca().equals(turno)){
                obterPecas(pecaCapturada.getCorPeca()).remove(pecaCapturada);
            }

            casaDe.setPeca(null);


            if(Util.ultimoElemento(cadeiaMovimentos.getMovimentos(), movimento)){
                casaFinal = casas[movimento.getAteY()][movimento.getAteX()];
                casaFinal.setPeca(peca);
                peca.setCasa(casaFinal);
            }
        }

        if(peca.getCorPeca().equals(CorPeca.BRANCA) && casaFinal.getY() == 0
                || peca.getCorPeca().equals(CorPeca.PRETA) && casaFinal.getY() == 7){
            peca.setDama(true);
        }

        printBoard();
        return true;
    }

    /**
     * Verifica se uma posição existe dentro do tabuleiro
     * @param x eixo x
     * @param y eixo y
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

    public CorPeca getTurno() {
        return turno;
    }
}
