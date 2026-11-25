class Main {
    public static void main(String[] args) {
        RedBlack rb = new RedBlack();

        int[] valores = {30, 20, 10, 40, 50, 25, 35};

        for (int x : valores) {
            rb.inserir(x);
        }

        rb.exibir();
        System.out.println();

        rb.remover(20);

        rb.exibir();
        System.out.println();
    }
}

// =============== CLASS: RedBlack ===============

public class RedBlack {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class No {
        int elemento;
        boolean cor;
        No esq, dir;

        No(int x) {
            elemento = x;
            cor = RED;
        }
    }

    private No raiz;

    // =============== AUXILIARES ===============

    private boolean isRed(No no) {
        return no != null && no.cor == RED;
    }

    private No rotacionarEsq(No no) {
        No dir = no.dir;
        no.dir = dir.esq;
        dir.esq = no;

        dir.cor = no.cor;
        no.cor = RED;

        return dir;
    }

    private No rotacionarDir(No no) {
        No esq = no.esq;
        no.esq = esq.dir;
        esq.dir = no;

        esq.cor = no.cor;
        no.cor = RED;

        return esq;
    }

    private void inverterCores(No no) {
        no.cor = !no.cor;
        no.esq.cor = !no.esq.cor;
        no.dir.cor = !no.dir.cor;
    }

    // =============== INSERÇÃO ===============

    public void inserir(int x) {
        raiz = inserir(raiz, x);
        raiz.cor = BLACK;
    }

    private No inserir(No no, int x) {
        if (no == null) return new No(x);

        if (x < no.elemento)
            no.esq = inserir(no.esq, x);
        else if (x > no.elemento)
            no.dir = inserir(no.dir, x);

        if (isRed(no.dir) && !isRed(no.esq))
            no = rotacionarEsq(no);

        if (isRed(no.esq) && isRed(no.esq.esq))
            no = rotacionarDir(no);

        if (isRed(no.esq) && isRed(no.dir))
            inverterCores(no);

        return no;
    }

    // =============== BALANCEAMENTO ===============

    private No balancear(No no) {
        if (isRed(no.dir))
            no = rotacionarEsq(no);

        if (isRed(no.esq) && isRed(no.esq.esq))
            no = rotacionarDir(no);

        if (isRed(no.esq) && isRed(no.dir))
            inverterCores(no);

        return no;
    }

    // =============== REMOÇÃO ===============

    public void remover(int x) {
        if (!pesquisar(x)) return;

        if (!isRed(raiz.esq) && !isRed(raiz.dir))
            raiz.cor = RED;

        raiz = remover(raiz, x);

        if (raiz != null)
            raiz.cor = BLACK;
    }

    private No remover(No no, int x) {
        if (x < no.elemento) {
            if (!isRed(no.esq) && !isRed(no.esq.esq))
                no = moverVermelhoEsq(no);

            no.esq = remover(no.esq, x);
        }
        else {
            if (isRed(no.esq))
                no = rotacionarDir(no);

            if (x == no.elemento && no.dir == null)
                return null;

            if (!isRed(no.dir) && !isRed(no.dir.esq))
                no = moverVermelhoDir(no);

            if (x == no.elemento) {
                No menor = menorNo(no.dir);
                no.elemento = menor.elemento;
                no.dir = removerMenor(no.dir);
            }
            else {
                no.dir = remover(no.dir, x);
            }
        }

        return balancear(no);
    }

    private No moverVermelhoEsq(No no) {
        inverterCores(no);

        if (isRed(no.dir.esq)) {
            no.dir = rotacionarDir(no.dir);
            no = rotacionarEsq(no);
            inverterCores(no);
        }

        return no;
    }

    private No moverVermelhoDir(No no) {
        inverterCores(no);

        if (isRed(no.esq.esq)) {
            no = rotacionarDir(no);
            inverterCores(no);
        }

        return no;
    }

    private No menorNo(No no) {
        while (no.esq != null)
            no = no.esq;

        return no;
    }

    private No removerMenor(No no) {
        if (no.esq == null)
            return null;

        if (!isRed(no.esq) && !isRed(no.esq.esq))
            no = moverVermelhoEsq(no);

        no.esq = removerMenor(no.esq);

        return balancear(no);
    }

    // =============== PESQUISA ===============

    public boolean pesquisar(int x) {
        No no = raiz;

        while (no != null) {
            if (x == no.elemento) return true;

            no = (x < no.elemento) ? no.esq : no.dir;
        }

        return false;
    }

    // =============== EXIBIR ===============

    public void exibir() {
        exibir(raiz);
    }

    private void exibir(No no) {
        if (no == null) return;

        exibir(no.esq);

        System.out.printf("%d(%c) ",
            no.elemento, no.cor ? 'V' : 'P');

        exibir(no.dir);
    }
}
