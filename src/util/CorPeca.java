package util;

public enum CorPeca {
    BRANCA(-1), PRETA(1);

    private int direcao;

    CorPeca(int direcao) {
        this.direcao = direcao;
    }

    public int getDirecao() {
        return direcao;
    }
}
