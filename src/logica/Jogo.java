package logica;

import util.CorPeca;
import util.Util;

import java.util.ArrayList;
import java.util.Collections;
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
                computarJogadasCasa(peca.getCasa().getColuna(), peca.getCasa().getLinha(), false, 0, null, peca);
            } else {
                adicionarMovimentosDiagonais(peca, 1, 1);
                adicionarMovimentosDiagonais(peca, 1, -1);
                adicionarMovimentosDiagonais(peca, -1, 1);
                adicionarMovimentosDiagonais(peca, -1, -1);
            }

        });
        obterMovimentosTurnoAtual().stream().forEach(m -> m.getPecaMovimentada().getMovimentos().add(m));
        System.out.println("Movimentos Possiveis: " + obterMovimentosTurnoAtual().size());
    }

    private void computarJogadasCasa(int x, int y, boolean esperaPeca, int depth, CadeiaMovimentos cadeiaMovimentos, Peca peca){
        List<Casa> movimentosBasicos = obterCasasMovimentoBasico(casas[y][x]);
        Casa casaAtual = casas[y][x];

        //Primeira iteração
        if(depth == 0){
            for(Casa c : movimentosBasicos){

                cadeiaMovimentos = new CadeiaMovimentos();
                cadeiaMovimentos.getMovimentos().add(new Movimento(casaAtual, c));
                cadeiaMovimentos.setPecaMovimentada(peca);

                // Caso não tenha uma peça na casa verificada
                if(c.getPeca() == null){
                    tentarAdicionarCadeiaDeMovimentos(cadeiaMovimentos);

                } else if (!peca.getCorPeca().equals(c.getPeca().getCorPeca())){
                    computarJogadasCasa(c.getColuna(), c.getLinha(), false, depth + 1,cadeiaMovimentos, peca);
                }
            }

        } else {
            for(Casa c : movimentosBasicos){

                CadeiaMovimentos novaCadeiaMovimentos = new CadeiaMovimentos(cadeiaMovimentos);
                novaCadeiaMovimentos.getMovimentos().add(new Movimento(casaAtual, c));

                //Caso espere uma peça e possua uma peça
                if(c.getPeca() != null && esperaPeca && !peca.getCorPeca().equals(c.getPeca().getCorPeca())){
                    //Apenas continuando a direção da casa anterior
                    novaCadeiaMovimentos.getPecasCapturadas().add(c.getPeca());
                    computarJogadasCasa(c.getColuna(), c.getLinha(), !esperaPeca, depth + 1, novaCadeiaMovimentos, peca);
                }

                //Caso não espere peça e não possua peça
                if(c.getPeca() == null && !esperaPeca){
                    //Qualquer direção contanto que não tenha uma peça que já esteja na cadeia de movimentos
                    computarJogadasCasa(c.getColuna(), c.getLinha(), !esperaPeca, depth + 1, novaCadeiaMovimentos, peca);
                }
            }

            tentarAdicionarCadeiaDeMovimentos(cadeiaMovimentos);
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

        // Obtém uma lista ordenada de cadeias de movimento maiores ou iguais a cadeia atual
        List<CadeiaMovimentos> cadeiaMaiorOuIgual = obterMovimentosTurnoAtual().stream()
                .filter(c -> c.getMovimentos().size() >= cadeiaMovimentos.getMovimentos().size())
                .sorted(Comparator.comparingInt(CadeiaMovimentos::getCapturas).reversed()).toList();

        List<CadeiaMovimentos> cadeiasValidas = new ArrayList<>(cadeiaMaiorOuIgual);

        // Caso não possua cadeia de movimento ou possua uma com o mesmo tamanho apenas adiciona a lista
        // e caso possua uma cadeia menor que a atual substitui ela pela a atual
        if(cadeiaMaiorOuIgual.size() == 0 || cadeiaMaiorOuIgual.get(0).getCapturas() == cadeiaMovimentos.getCapturas()){
            System.out.println("Movimento Adicionado");
            cadeiasValidas.add(cadeiaMovimentos);

        } else if(cadeiaMaiorOuIgual.get(0).getCapturas() < cadeiaMovimentos.getCapturas()){
            System.out.println("Movimento substituido");
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

    private List<Casa> obterCasasMovimentoBasico(Casa casa){
        List<Casa> resultado = new ArrayList<>();

        int frente = casa.getLinha() + turno.getDirecao();
        int atras = casa.getLinha() + (turno.getDirecao() * -1);
        int direita = casa.getColuna() + 1;
        int esquerda = casa.getColuna() - 1;

        if(posicaoDentroDoTabuleiro(direita, frente)){
            resultado.add(casas[frente][direita]);
        }

        if(posicaoDentroDoTabuleiro(esquerda, frente)){
            resultado.add(casas[frente][esquerda]);
        }

        return resultado;
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
                // TODO CONSERTAR ISSO
                peca.getMovimentos().add(new CadeiaMovimentos(peca, Collections.singletonList(new Movimento(peca.getCasa(), casas[y][x]))));
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
    public List<CadeiaMovimentos> obterMovimentosPeca(int x, int y){
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

        if(peca.getCorPeca().equals(CorPeca.BRANCA) && casaFinal.getLinha() == 0
                || peca.getCorPeca().equals(CorPeca.PRETA) && casaFinal.getLinha() == 7){
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

    public CorPeca getTurno() {
        return turno;
    }
}
