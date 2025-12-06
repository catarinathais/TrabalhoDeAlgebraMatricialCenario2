

import \\ LinearAlgebra;
import \\ Matrix;
import \\ Vector;

public class Questao1 {
    public static void main(String[] args) {
        Metodos metodos = new Metodos();
        // Matriz da Questão 1
        Matrix A = new Matrix(3, 3, new double[]{
                0, 1, 0,
                1, 0, 1,
                1, 0, 1
        });

        //1: Vetor inicial (Soma das colunas)
        double[] elementosSoma = new double[A.colunas];
        for (int i = 0; i < A.linhas; i++) {
            for (int j = 0; j < A.colunas; j++) {
                elementosSoma[j] += A.get(i, j);
            }
        }
        Vector vetorSoma = new Vector(A.colunas, elementosSoma);

        // Calcula norma inicial para normalizar a0
        double normaInicial = metodos.calcularNorma(vetorSoma);

        // a0 = 1 / Norma * Vetor Soma
        Vector a0 = LinearAlgebra.times(1 / normaInicial, vetorSoma);

        //2: Preparar Matriz de Iteração (At * A)
        Matrix At = LinearAlgebra.transpose(A);
        Matrix AtA = LinearAlgebra.dot(At, A);

        //3: Iterações com Tolerância
        Vector vetorAnterior = a0;
        Vector vetorAtual = null;

        double tolerancia = 0.00001; // Criterio de parada
        double erro = 1.0; // Valor alto para garantir que entre no loop
        int k = 0;

        System.out.println("Vetor Inicial (a0):");
        vetorAnterior.print();
        System.out.println("------------------------------");

        do {
            k++;

            // 1. Multiplica: v = (At * A) * v_anterior
            Vector vetorNaoNormalizado = LinearAlgebra.dotMV(AtA, vetorAnterior);

            // 2. Normaliza o resultado
            double norma = metodos.calcularNorma(vetorNaoNormalizado);

            // Cria vetor normalizado manualmente
            double[] dadosNormalizados = new double[vetorNaoNormalizado.dim];
            for (int i = 0; i < vetorNaoNormalizado.dim; i++) {
                dadosNormalizados[i] = vetorNaoNormalizado.getDouble(i) / norma;
            }
            vetorAtual = new Vector(vetorNaoNormalizado.dim, dadosNormalizados);

            // 3. Calcula o Erro
            erro = 0;
            for (int i = 0; i < vetorAtual.dim; i++) {
                double diff = vetorAtual.getDouble(i) - vetorAnterior.getDouble(i);
                erro += Math.pow(diff, 2);
            }
            erro = Math.sqrt(erro);

            // 4. Exibe e Atualiza para a próxima volta
            System.out.printf("Iteração " + k + " | Erro: " + erro);
            System.out.println();
            vetorAtual.print(); // Comentei para focar no resultado final

            vetorAnterior = vetorAtual;

        } while (erro > tolerancia);

        System.out.println("------------------------------");
        System.out.println("Convergiu na iteração: " + k);
        System.out.println();

        // Exibir vetor final NÃO ordenado com identificação
        System.out.println("=== Vetor Autoridade Final (Ordem dos Sites) ===");
        for (int i = 0; i < vetorAtual.dim; i++) {
            // Assumindo que indice 0 é o Site 1, indice 1 é o Site 2, etc.
            System.out.printf("Site " + (i + 1) + ": " + vetorAtual.getDouble(i));
            System.out.println();
        }
        System.out.println();
        // Ordenar e exibir DECRESCENTE (Ranking)

        // Criamos vetores temporários para não alterar o objeto Vector original
        int n = vetorAtual.dim;
        double[] valores = new double[n];
        int[] idsDosSites = new int[n];

        // Popula os vetores auxiliares
        for (int i = 0; i < n; i++) {
            valores[i] = vetorAtual.getDouble(i);
            idsDosSites[i] = i + 1;
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Se o valor atual for MENOR que o próximo, troca (para ficar decrescente)
                if (valores[j] < valores[j + 1]) {

                    // Troca os valores
                    double tempValor = valores[j];
                    valores[j] = valores[j + 1];
                    valores[j + 1] = tempValor;

                    // Troca os IDs correspondentes (para o site acompanhar sua nota)
                    int tempId = idsDosSites[j];
                    idsDosSites[j] = idsDosSites[j + 1];
                    idsDosSites[j + 1] = tempId;
                }
            }
        }

        System.out.println("=== Ranking de Autoridade (Decrescente) ===");
        for (int i = 0; i < n; i++) {
            System.out.printf((i + 1) + " Lugar: Site " + idsDosSites[i] );
            System.out.println( " | Autoridade: = " + valores[i]);
        }
    }
}
