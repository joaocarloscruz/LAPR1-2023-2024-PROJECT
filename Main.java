import java.awt.*;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class Main {
    static final int NUM_PARAMETROS = 7;

    static final int NUM_VALORES_INICIAIS = 3;
    static final int NUM_LINHAS = 10;

    /**
     * No main , apenas declaramos arrays bidimensionais com Strings para os valores dos parametros e valores iniciais e definimos as variáveis
     * método , que nos permite identificar qual o algoritmo a ser usado, passoIntegração ,que representa o passo de integração a ser usado e numeroDias , sendo o numero de dias pedidos pelo utilizador.
     * Temos tambem uma condição com um if statement para identificar qual o modo a ser utilizado (interativo ou não) , o que dará depois início ao resto da aplicação.
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String[][] valoresParametros = new String[NUM_PARAMETROS][NUM_LINHAS];
        String[][] valoresIniciais = new String[NUM_VALORES_INICIAIS][NUM_LINHAS];
        String parametrosCSV = "";
        String valIniciaisCSV = "";
        int metodo = 0;
        double passoIntegracao = 0.0;
        int numeroDias = 0;

        if (isModoInterativo(args)) {
            executarModoInterativo(valoresParametros, valoresIniciais, parametrosCSV, valIniciaisCSV, metodo, passoIntegracao, numeroDias);
        } else {
            executarModoNaoInterativo(valoresParametros, valoresIniciais, args, parametrosCSV, valIniciaisCSV, metodo, passoIntegracao, numeroDias);
        }

    }

    /**
     * Este método serve para identificarmos se o modo a ser utilizado é o interativo ou não.
     * @param args
     * @return
     */
    public static boolean isModoInterativo(String[] args) {
        return args.length == 0;
    }

    /**
     * Aqui , executamos o modo interativo caso seja o pretendido , chamando maior parte dos outros módulos criados. Validamos também cada requesito introduzido e
     * retiramos os valores de cada parâmetro e valor inicial de forma organizada e fazendo a sua conversão quando necessário.
     * @param valoresParametros
     * @param valoresIniciais
     * @param parametrosCSV
     * @param valIniciaisCSV
     * @param metodo
     * @param passoIntegracao
     * @param numeroDias
     * @throws IOException
     * @throws InterruptedException
     */
    public static void executarModoInterativo(String[][] valoresParametros, String[][] valoresIniciais, String parametrosCSV, String valIniciaisCSV, int metodo, double passoIntegracao, int numeroDias) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);

        System.out.print("Introduza o nome do arquivo com os parâmetros: ");
        parametrosCSV = in.nextLine();
        File ficheiroParametros = new File(parametrosCSV);
        while (!ficheiroParametros.exists()) {
            System.out.println("O arquivo de parâmetros não existe: ");
            ;
            parametrosCSV = in.nextLine();
            ficheiroParametros = new File(parametrosCSV);
        }
        valoresParametros = lerParametros(parametrosCSV);

        System.out.print("Introduza o nome do arquivo com as condições iniciais: ");
        valIniciaisCSV = in.nextLine();
        File ficheiroValoresIniciais = new File(valIniciaisCSV);
        while (!ficheiroValoresIniciais.exists()) {
            System.out.println("O arquivo de valores iniciais não existe: ");
            ;
            valIniciaisCSV = in.nextLine();
            ficheiroValoresIniciais = new File(valIniciaisCSV);
        }
        valoresIniciais = lerValIniciais(valIniciaisCSV);

        int quantidadeLinhas = contarNumeroDeLinhas(parametrosCSV);

        System.out.print("Escolha o método (1 - Euler, 2 - Runge Kutta de 4ª ordem): ");
        metodo = in.nextInt();
        while (metodo != 1 && metodo != 2) {
            System.out.println("O número não corresponde a nenhum dos métodos: ");
            metodo = in.nextInt();
        }

        System.out.print("Introduza o passo de integração (0 < h <= 1): ");
        passoIntegracao = in.nextDouble();
        while (passoIntegracao <= 0 || passoIntegracao > 1) {
            System.out.println("O número não se encontra no intervalo especificado: ");
            passoIntegracao = in.nextDouble();
        }

        System.out.print("Introduza o número de dias a considerar para análise (maior que zero): ");
        numeroDias = in.nextInt();
        while (numeroDias <= 0) {
            System.out.println("O número não é maior que zero: ");
            numeroDias = in.nextInt();
        }

        double S0 = 0;
        double I0 = 0;
        double R0 = 0;
        double lambda = 0;
        double b = 0;
        double mu = 0;
        double kapa = 0;
        double beta = 0;
        double delta1 = 0;
        double delta2 = 0;

        for (int i = 1; i < quantidadeLinhas; i++) {


            for (int j = 0; j < valoresParametros[0].length; j++) {
                if (valoresParametros[i][j] != null) {
                    switch (valoresParametros[0][j]) {
                        case "lambda":
                            int indexLambda = procurarColunaPeloNome("lambda", valoresParametros[0]);
                            lambda = Double.parseDouble(valoresParametros[i][indexLambda].replace(",", "."));
                            break;
                        case "mu":
                            int indexMu = procurarColunaPeloNome("mu", valoresParametros[0]);
                            mu = Double.parseDouble(valoresParametros[i][indexMu].replace(",", "."));
                            break;
                        case "kapa":
                            int indexKapa = procurarColunaPeloNome("kapa", valoresParametros[0]);
                            kapa = Double.parseDouble(valoresParametros[i][indexKapa].replace(",", "."));
                            break;
                        case "beta":
                            int indexBeta = procurarColunaPeloNome("beta", valoresParametros[0]);
                            beta = Double.parseDouble(valoresParametros[i][indexBeta].replace(",", "."));
                            break;
                        case "b":
                            int indexB = procurarColunaPeloNome("b", valoresParametros[0]);
                            b = Double.parseDouble(valoresParametros[i][indexB].replace(",", "."));
                            break;
                        case "delta1":
                            int indexDelta1 = procurarColunaPeloNome("delta1", valoresParametros[0]);
                            delta1 = Double.parseDouble(valoresParametros[i][indexDelta1].replace(",", "."));
                            break;
                        case "delta2":
                            int indexDelta2 = procurarColunaPeloNome("delta2", valoresParametros[0]);
                            delta2 = Double.parseDouble(valoresParametros[i][indexDelta2].replace(",", "."));
                            break;
                    }
                }
            }

            for (int j = 0; j < valoresIniciais[0].length; j++) {
                if (valoresIniciais[i][j] != null) {
                    switch (valoresIniciais[0][j]) {
                        case "S0":
                            int indexS0 = procurarColunaPeloNome("S0", valoresIniciais[0]);
                            S0 = Double.parseDouble(valoresIniciais[i][indexS0].replace(",", "."));
                            break;
                        case "I0":
                            int indexI0 = procurarColunaPeloNome("I0", valoresIniciais[0]);
                            I0 = Double.parseDouble(valoresIniciais[i][indexI0].replace(",", "."));
                            break;
                        case "R0":
                            int indexR0 = procurarColunaPeloNome("R0", valoresIniciais[0]);
                            R0 = Double.parseDouble(valoresIniciais[i][indexR0].replace(",", "."));
                            break;
                    }
                }
            }



            switch (metodo) {
                case 1:
                    Euler(numeroDias, passoIntegracao, S0, I0, R0, lambda, b, mu, kapa, beta, delta1, delta2, i);
                    break;
                case 2:
                    RK4(numeroDias, passoIntegracao, S0, I0, R0, lambda, b, mu, kapa, beta, delta1, delta2, i);
                    break;

            }

        }

    }

    /**
     * Neste módulo , executamos o modo não interativo de forma semelhante ao interativo.
     * @param valoresParametros
     * @param valoresIniciais
     * @param args
     * @param parametrosCSV
     * @param valIniciaisCSV
     * @param metodo
     * @param passoIntegracao
     * @param numeroDias
     * @throws IOException
     * @throws InterruptedException
     */
    public static void executarModoNaoInterativo(String[][] valoresParametros, String[][] valoresIniciais, String[] args, String parametrosCSV, String valIniciaisCSV, int metodo, double passoIntegracao, int numeroDias) throws IOException, InterruptedException {

        boolean inputValido = true;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-b":
                    if (i + 1 < args.length) {
                        parametrosCSV = args[i + 1];
                        File ficheiroParametros = new File(parametrosCSV);
                        if (!ficheiroParametros.exists()) {
                            System.out.println("O arquivo de parâmetros não existe ");
                            inputValido = false;
                        }
                        i++;
                    }
                    break;
                case "-c":
                    if (i + 1 < args.length) {
                        valIniciaisCSV = args[i + 1];
                        File ficheirosValoresIniciais = new File(valIniciaisCSV);
                        if (!ficheirosValoresIniciais.exists()) {
                            System.out.println("O arquivo de condições iniciais não existe ");
                            inputValido = false;
                        }
                        i++;
                    }
                    break;
                case "-m":
                    if (i + 1 < args.length) {
                        metodo = Integer.parseInt(args[i + 1]);
                        if (metodo != 1 && metodo != 2) {
                            System.out.println("O número não corresponde a nenhum dos métodos ");
                            System.out.println("1 - Euler");
                            System.out.println("2 - Runge-Kutta");
                            inputValido = false;
                        }
                        i++;
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        passoIntegracao = Double.parseDouble(args[i + 1]);
                        if (passoIntegracao <= 0 || passoIntegracao > 1) {
                            System.out.println("O número não se encontra no intervalo especificado ( 0 < h >= 1 )  ");
                            inputValido = false;
                        }
                        i++;
                    }
                    break;
                case "-d":
                    if (i + 1 < args.length) {
                        numeroDias = Integer.parseInt(args[i + 1]);
                        if (numeroDias <= 0) {
                            System.out.println("O número de dias inserido não é maior que zero");
                            inputValido = false;
                        }
                        i++;
                    }
                    break;
            }
        }
        if (inputValido) {
            valoresParametros = lerParametros(parametrosCSV);
            valoresIniciais = lerValIniciais(valIniciaisCSV);

            int quantidadeLinhas = contarNumeroDeLinhas(parametrosCSV);
            double S0 = 0;
            double I0 = 0;
            double R0 = 0;
            double lambda = 0;
            double b = 0;
            double mu = 0;
            double kapa = 0;
            double beta = 0;
            double delta1 = 0;
            double delta2 = 0;

            for (int i = 1; i < quantidadeLinhas; i++) {


                for (int j = 0; j < valoresParametros[0].length; j++) {
                    if (valoresParametros[i][j] != null) {
                        switch (valoresParametros[0][j]) {
                            case "lambda":
                                int indexLambda = procurarColunaPeloNome("lambda", valoresParametros[0]);
                                lambda = Double.parseDouble(valoresParametros[i][indexLambda].replace(",", "."));
                                break;
                            case "mu":
                                int indexMu = procurarColunaPeloNome("mu", valoresParametros[0]);
                                mu = Double.parseDouble(valoresParametros[i][indexMu].replace(",", "."));
                                break;
                            case "kapa":
                                int indexKapa = procurarColunaPeloNome("kapa", valoresParametros[0]);
                                kapa = Double.parseDouble(valoresParametros[i][indexKapa].replace(",", "."));
                                break;
                            case "beta":
                                int indexBeta = procurarColunaPeloNome("beta", valoresParametros[0]);
                                beta = Double.parseDouble(valoresParametros[i][indexBeta].replace(",", "."));
                                break;
                            case "b":
                                int indexB = procurarColunaPeloNome("b", valoresParametros[0]);
                                b = Double.parseDouble(valoresParametros[i][indexB].replace(",", "."));
                                break;
                            case "delta1":
                                int indexDelta1 = procurarColunaPeloNome("delta1", valoresParametros[0]);
                                delta1 = Double.parseDouble(valoresParametros[i][indexDelta1].replace(",", "."));
                                break;
                            case "delta2":
                                int indexDelta2 = procurarColunaPeloNome("delta2", valoresParametros[0]);
                                delta2 = Double.parseDouble(valoresParametros[i][indexDelta2].replace(",", "."));
                                break;
                        }
                    }
                }


                for (int j = 0; j < valoresIniciais[0].length; j++) {
                    if (valoresIniciais[i][j] != null) {
                        switch (valoresIniciais[0][j]) {
                            case "S0":
                                int indexS0 = procurarColunaPeloNome("S0", valoresIniciais[0]);
                                S0 = Double.parseDouble(valoresIniciais[i][indexS0].replace(",", "."));
                                break;
                            case "I0":
                                int indexI0 = procurarColunaPeloNome("I0", valoresIniciais[0]);
                                I0 = Double.parseDouble(valoresIniciais[i][indexI0].replace(",", "."));
                                break;
                            case "R0":
                                int indexR0 = procurarColunaPeloNome("R0", valoresIniciais[0]);
                                R0 = Double.parseDouble(valoresIniciais[i][indexR0].replace(",", "."));
                                break;
                        }
                    }
                }


                switch (metodo) {
                    case 1:
                        Euler(numeroDias, passoIntegracao, S0, I0, R0, lambda, b, mu, kapa, beta, delta1, delta2, i);
                        break;
                    case 2:
                        RK4(numeroDias, passoIntegracao, S0, I0, R0, lambda, b, mu, kapa, beta, delta1, delta2, i);
                        break;

                }

            }

        }
    }

    /**
     * Aqui lemos os valores dos parâmetros através do ficheiro de texto csv fornecido.
     * @param ficheiro
     * @return
     * @throws FileNotFoundException
     */
    public static String[][] lerParametros(String ficheiro) throws FileNotFoundException {
        Scanner in = new Scanner(new File(ficheiro));

        String[][] valoresParametros = new String[NUM_LINHAS][NUM_PARAMETROS];
        for (int i = 0; i < NUM_LINHAS && in.hasNextLine(); i++) {
            String linha = in.nextLine();
            String[] valores = linha.split(";");
            for (int j = 0; j < NUM_PARAMETROS + 1; j++) {
                if (j != 0) {
                    valoresParametros[i][j - 1] = valores[j];
                }
            }
        }

        in.close();

        return valoresParametros;
    }

    /**
     * Semelhante ao método acima , aqui lemos o valores iniciais de cada função.
     * @param ficheiro
     * @return
     * @throws FileNotFoundException
     */

    public static String[][] lerValIniciais(String ficheiro) throws FileNotFoundException {
        Scanner in = new Scanner(new File(ficheiro));

        String[][] valIniciais = new String[NUM_LINHAS][3];
        for (int i = 0; i < NUM_LINHAS && in.hasNextLine(); i++) {
            String linha = in.nextLine();
            String[] valores = linha.split(";");
            for (int j = 0; j < 3; j++) {
                valIniciais[i][j] = valores[j];
            }
        }
        in.close();
        return valIniciais;
    }

    /**
     * Este método foi criado para procurar , tendo em conta um certo parâmetro ou valor incial , o seu valor correspondente.
     * @param column
     * @param inputArray
     * @return
     */
    public static int procurarColunaPeloNome(String column, String[] inputArray) {
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i].equals(column)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Este método é utilizado para descobrirmos quantos casos é que temos para os valores e parâmetros inicias.
     * @param ficheiro
     * @return
     * @throws FileNotFoundException
     */
    public static int contarNumeroDeLinhas(String ficheiro) throws FileNotFoundException {
        Scanner in = new Scanner(new File(ficheiro));
        int quantidadeLinhas = 0;
        for (int i = 0;  in.hasNextLine(); i++) {
            in.nextLine();
            quantidadeLinhas ++;
        }
        in.close();
        return quantidadeLinhas;
    }

    /**
     * Nesta secção temos o algoritmo de Euler aplicado para as funções S,I e R.
     * @param numeroDias
     * @param passoIntegração
     * @param S0
     * @param I0
     * @param R0
     * @param lambda
     * @param b
     * @param mu
     * @param k
     * @param beta
     * @param delta1
     * @param delta2
     * @param count
     * @throws IOException
     * @throws InterruptedException
     */
    private static void Euler(int numeroDias, double passoIntegração, double S0, double I0, double R0, double lambda, double b, double mu, double k, double beta, double delta1, double delta2, int count) throws IOException, InterruptedException {

        int n = (int) (numeroDias / passoIntegração);
        double[] S = new double[n + 1];
        double[] I = new double[n + 1];
        double[] R = new double[n + 1];

        S[0] = S0;
        I[0] = I0;
        R[0] = R0;

        for (int i = 1; i <= n; i++) {
            double dSdt = lambda - b * S[i - 1] * I[i - 1] - mu * S[i - 1];
            double dIdt = b * S[i - 1] * I[i - 1] - k * I[i - 1] + beta * I[i - 1] * R[i - 1] - (mu + delta1) * I[i - 1];
            double dRdt = k * I[i - 1] - beta * I[i - 1] * R[i - 1] - (mu + delta2) * R[i - 1];

            S[i] = S[i - 1] + passoIntegração * dSdt;
            I[i] = I[i - 1] + passoIntegração * dIdt;
            R[i] = R[i - 1] + passoIntegração * dRdt;

        }
        printarResultados(S, I, R, n, passoIntegração, count, numeroDias);
    }

    /**
     * Similarmente ao método acima , aqui temos o algoritmo de Runge-Kutta de Quarta ordem.
     * @param numeroDias
     * @param passoIntegração
     * @param S0
     * @param I0
     * @param R0
     * @param lambda
     * @param b
     * @param mu
     * @param k
     * @param beta
     * @param delta1
     * @param delta2
     * @param count
     * @throws IOException
     * @throws InterruptedException
     */
    private static void RK4(int numeroDias, double passoIntegração, double S0, double I0, double R0, double lambda, double b, double mu, double k, double beta, double delta1, double delta2, int count) throws IOException, InterruptedException {

        int n = (int) (numeroDias / passoIntegração);
        double[] S = new double[n + 1];
        double[] I = new double[n + 1];
        double[] R = new double[n + 1];

        S[0] = S0;
        I[0] = I0;
        R[0] = R0;

        int i = 0;

        do {
            double dSdt = lambda - b * S[i] * I[i] - mu * S[i];
            double dIdt = b * S[i] * I[i] - k * I[i] + beta * I[i] * R[i] - (mu + delta1) * I[i];
            double dRdt = k * I[i] - beta * I[i] * R[i] - (mu + delta2) * R[i];

            double k1_S = passoIntegração * dSdt;
            double k1_I = passoIntegração * dIdt;
            double k1_R = passoIntegração * dRdt;

            double k2_S = passoIntegração * (lambda - b * (S[i] + 0.5 * k1_S) * (I[i] + 0.5 * k1_I) - mu * (S[i] + 0.5 * k1_S));
            double k2_I = passoIntegração * (b * (S[i] + 0.5 * k1_S) * (I[i] + 0.5 * k1_I) - k * (I[i] + 0.5 * k1_I) + beta * (I[i] + 0.5 * k1_I) * (R[i] + 0.5 * k1_R) - (mu + delta1) * (I[i] + 0.5 * k1_I));
            double k2_R = passoIntegração * (k * (I[i] + 0.5 * k1_I) - beta * (I[i] + 0.5 * k1_I) * (R[i] + 0.5 * k1_R) - (mu + delta2) * (R[i] + 0.5 * k1_R));

            double k3_S = passoIntegração * (lambda - b * (S[i] + 0.5 * k2_S) * (I[i] + 0.5 * k2_I) - mu * (S[i] + 0.5 * k2_S));
            double k3_I = passoIntegração * (b * (S[i] + 0.5 * k2_S) * (I[i] + 0.5 * k2_I) - k * (I[i] + 0.5 * k2_I) + beta * (I[i] + 0.5 * k2_I) * (R[i] + 0.5 * k2_R) - (mu + delta1) * (I[i] + 0.5 * k2_I));
            double k3_R = passoIntegração * (k * (I[i] + 0.5 * k2_I) - beta * (I[i] + 0.5 * k2_I) * (R[i] + 0.5 * k2_R) - (mu + delta2) * (R[i] + 0.5 * k2_R));

            double k4_S = passoIntegração * (lambda - b * (S[i] + k3_S) * (I[i] + k3_I) - mu * (S[i] + k3_S));
            double k4_I = passoIntegração * (b * (S[i] + k3_S) * (I[i] + k3_I) - k * (I[i] + k3_I) + beta * (I[i] + k3_I) * (R[i] + k3_R) - (mu + delta1) * (I[i] + k3_I));
            double k4_R = passoIntegração * (k * (I[i] + k3_I) - beta * (I[i] + k3_I) * (R[i] + k3_R) - (mu + delta2) * (R[i] + k3_R));

            double k_S = (k1_S + 2 * k2_S + 2 * k3_S + k4_S) / 6;
            double k_I = (k1_I + 2 * k2_I + 2 * k3_I + k4_I) / 6;
            double k_R = (k1_R + 2 * k2_R + 2 * k3_R + k4_R) / 6;

            S[i + 1] = S[i] + k_S;
            I[i + 1] = I[i] + k_I;
            R[i + 1] = R[i] + k_R;

            i++;
        } while (i < n);
        printarResultados(S, I, R, n, passoIntegração, count, numeroDias);
    }

    /**
     * Este método tem como objetivo a apresentar os resultados obtidos para o ficheiro de texto de output ,gerar os gráficos necessários através do gnuplot e abri-los como png.
     * @param S
     * @param I
     * @param R
     * @param n
     * @param passoIntegracao
     * @param count
     * @param numeroDias
     * @throws IOException
     * @throws InterruptedException
     */
    private static void printarResultados(double[] S, double[] I, double[] R, int n, double passoIntegracao, int count, int numeroDias) throws IOException, InterruptedException {
        PrintWriter out = new PrintWriter("output" + count + ".csv");
        int dia = 0;
        out.println("dia;S;I;R;T");
        for (int i = 0; i <= n; i++) {
            out.printf(Locale.US, "%d;%.9f;%.9f;%.9f;%.0f\n", dia, S[i], I[i], R[i], S[i] + I[i] + R[i]);
            dia++;
            i += (int) (1 / passoIntegracao) - 1;
        }
        out.close();
        gerarGraficoGnuplot(numeroDias, count);
        abrirImagemDoGrafico("graficoVariacaoCurvas"+count+".png");
    }

    /**
     * Este método é um teste unitário para os algoritmos de Euler e RK4 para verificar se os mesmos estão a apresentar os resultados pretendidos.
     * @param n
     * @param passoIntegração
     * @param S
     * @param I
     * @param R
     */
    public static void testeAlgoritmo(int n, double passoIntegração, double S[], double I[], double R[]) {
        int count = 0;
        for (int j = 0; j <= n; j++) {
            System.out.printf("S[%d] = %f\n", count, S[j]);
            System.out.printf("I[%d] = %f\n", count, I[j]);
            System.out.printf("R[%d] = %f\n", count, R[j]);
            System.out.printf("T[%d] = %f\n", count, S[j] + I[j] + R[j]);
            System.out.printf("\n");

            j += (int) (1 / passoIntegração) - 1;
            count++;
        }
    }

    /**
     * Este método testa se estamos a ler o número de parâmetros corretos.
     * @param valoresParametros
     * @return
     */
    private static boolean testeParametros(String[][] valoresParametros) {
        int numParamentrosAtual = 0;
        for (int i = 0; i < NUM_PARAMETROS; i++) {
            if (valoresParametros[0][i] != null) {
                numParamentrosAtual++;
            }
        }
        if (numParamentrosAtual == NUM_PARAMETROS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Similarmente ao método acima , aqui testamos se estamos a ler os valores iniciais corretos.
     * @param valIniciais
     * @return
     */
    private static boolean testeValIniciais(String[][] valIniciais) {
        int numValIniciaisAtual = 0;
        for (int i = 0; i < NUM_VALORES_INICIAIS; i++) {
            if (valIniciais[i][0] != null) {
                numValIniciaisAtual++;
            }
        }
        if (numValIniciaisAtual == NUM_VALORES_INICIAIS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Neste penúltimo método geramos o gráfico das funções para o caso em estudo através da aplicação Gnuplot.
     * @param numeroDias
     * @param count
     * @throws IOException
     * @throws InterruptedException
     */
    public static void gerarGraficoGnuplot(int numeroDias, int count) throws IOException, InterruptedException {
        String gnuplot = "C:\\Program Files\\gnuplot\\bin\\gnuplot.exe";
        String codigoGnuplot = "set datafile separator \";\"\n" +
                "set term png size 1200,720\n" +
                "set grid\n" +
                "set decimalsign '.'\n" +
                "set output 'graficoVariacaoCurvas"+count+".png'\n" +
                "plot 'output"+count +".csv' using 1:2 with lines lt 1 lc rgb 'red' title 'Curva S', '' using 1:3 with lines lt 2 lc rgb 'dark-blue' title 'Curva I', '' using 1:4 with lines lt 3 lc rgb 'dark-green' title 'Curva R'\n" +
                "set title 'Variação das curvas'\n" +
                "set xlabel 'Nº de dias'\n" +
                "set ylabel 'População'\n" +
                "set xrange [0:" + numeroDias + "]\n" +
                "set yrange [0:1]\n" +
                "set ytics 0, 0.1, 1\n" +
                "set xtics 0, 1\n" +
                "replot\n";

        Path gnuplotScriptPath = Files.createTempFile("gnuplot_script", ".txt");
        Files.writeString(gnuplotScriptPath, codigoGnuplot, StandardOpenOption.CREATE);

        ProcessBuilder processBuilder = new ProcessBuilder(gnuplot, gnuplotScriptPath.toString());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();


        process.waitFor();
    }

    /**
     * Por fim , nesta secção abrimos a imagem dos gráficos gerados pelo Gnuplot.
     * @param graficoVariacoes
     * @throws IOException
     */
    public static void abrirImagemDoGrafico(String graficoVariacoes) throws IOException {
        File imageFile = new File(graficoVariacoes);

        if (!imageFile.exists()) {
            System.out.println("Arquivo de imagem não encontrado: " + graficoVariacoes);
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        desktop.open(imageFile);
    }


}