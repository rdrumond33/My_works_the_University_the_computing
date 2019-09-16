package lexer;

import java.io.*;

public class Lexer {

    private static final int END_OF_FILE = -1; // constante para fim do arquivo
    private static int lookahead = 0; // armazena o último caractere lido do arquivo	
    public static int n_line = 1; // contador de linhas
    public static int n_column = 1; // contador de colunas
    private RandomAccessFile instance_file; // referencia para o arquivo
    private static TS tabelaSimbolos; // tabela de simbolos
    int coluna_do_cometario = 0;
    int Linha_de_comentario = 0;

    // construtora >> nao eh necessario mexer aqui
    public Lexer(String input_data) {

        // Abre instance_file de input_data
        try {
            instance_file = new RandomAccessFile(input_data, "r");
        } catch (IOException e) {
            System.out.println("Erro de abertura do arquivo " + input_data + "\n" + e);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Erro do programa ou falha da tabela de simbolos\n" + e);
            System.exit(2);
        }
    }

    // Fecha instance_file de input_data >> nao eh necessario mexer aqui
    public void fechaArquivo() {

        try {
            instance_file.close();
        } catch (IOException errorFile) {
            System.out.println("Erro ao fechar arquivo\n" + errorFile);
            System.exit(3);
        }
    }

    // Reporta erro para o usuário >> nao eh necessario mexer aqui
    public void sinalizaErro(String mensagem) {

        System.out.println("[Erro Lexico]: " + mensagem + "\n");
    }

    // Volta uma posição do buffer de leitura >> nao eh necessario mexer aqui
    public void retornaPonteiro() {

        try {
            // Não é necessário retornar o ponteiro em caso de Fim de Arquivo
            if (lookahead != END_OF_FILE) {
                instance_file.seek(instance_file.getFilePointer() - 1);
                n_column--; // decrementar a coluna
            }
        } catch (IOException e) {
            System.out.println("Falha ao retornar a leitura\n" + e);
            System.exit(4);
        }
    }

    public Token proxToken() {

        // essa variavel armaeza o lexema (palavra) construido
        StringBuilder lexema = new StringBuilder();

        // variavel que representa o estado atual
        int estado = 0;

        // armazena o char corrente
        char c;

        // sai desse loop somente qndo retornar um token
        while (true) {
            c = '\u0000'; // null char

            // avanca caractere
            try {
                lookahead = instance_file.read();
                if (lookahead != END_OF_FILE) {
                    c = (char) lookahead;
                    n_column++;//Toda vez que avançar o ponteiro ira acrecentar o numero de coluna
                }
            } catch (IOException e) {
                System.out.println("Erro na leitura do arquivo");
                System.exit(3);
            }

            // movimentacao do automato
            switch (estado) {

                case 0:
                    if (lookahead == END_OF_FILE) // fim de arquivo. hora de parar
                    {
                        return new Token(Tag.EOF, "EOF", n_line, n_column);
                    } else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                        // Permance no estado = 0
                        if (c == '\n' || c == '\r') {//Reconhecer quebra de linha
                            n_column = 1;
                            n_line++;
                        } else if (c == '\t') {//Reconhecer TAB

                            n_column += 3;

                        }

                    } else if (c == '+') {//Estado Q1
                        return new Token(Tag.RELOP_PLUS, "+", n_line, n_column);
                    } else if (c == '-') {// Estado Q2
                        return new Token(Tag.RELOP_MINUS, "-", n_line, n_column);
                    } else if (c == '*') {//Estado Q3
                        return new Token(Tag.RELOP_MULT, "*", n_line, n_column);
                    } else if (c == '/') {
                        estado = 24;
                    } else if (c == '<') {
                        lexema.append(c);//adiciona o caracter C e vai prar o estado=5
                        estado = 5;//Estado Q5
                    } else if (c == '>') {
                        lexema.append(c);
                        estado = 8;//Estado Q8
                    } else if (c == '=') {
                        lexema.append(c);
                        estado = 11;//Estado Q11
                    } else if (c == '!') {//estado Q14 
                        lexema.append(c);
                        estado = 14;
                    } else if (Character.isLetter(c)) {//Estado Q16
                        lexema.append(c);
                        estado = 16;
                    } else if (Character.isDigit(c)) {//Estado Q18
                        lexema.append(c); // comecamos a construir um numero
                        estado = 18; // vamos para o estado 18
                    } else if (c == '"') {//Estado Q23
                        estado = 23;
                    } else if (c == '(') {//Estado Q25
                        return new Token(Tag.SMB_OP, "(", n_line, n_column);
                    } else if (c == ')') {//Estado Q26
                        return new Token(Tag.SMB_CP, ")", n_line, n_column);
                    } else if (c == ';') {//Estado Q27
                        return new Token(Tag.SMB_SEMICOLON, ";", n_line, n_column);
                    } else {
                        sinalizaErro("Simbolo " + c + " invalido na linha " + n_line + " e coluna " + n_column);
                        return null;
                    }
                    break;

                case 5:

                    if (c == '=') {//Estado Q6
                        estado = 0;
                        return new Token(Tag.RELOP_LE, "<=", n_line, n_column);
                    } else {//Estado Q7
                        estado = 0;
                        retornaPonteiro();
                        return new Token(Tag.RELOP_LT, "<", n_line, n_column);
                    }
                case 8:
                    if (c == '=') {//Estado Q9
                        estado = 0;

                        return new Token(Tag.RELOP_LE, ">=", n_line, n_column);

                    } else {//Estado Q10
                        estado = 0;
                        retornaPonteiro();
                        return new Token(Tag.RELOP_GT, ">", n_line, n_column);
                    }

                case 11:
                    if (c == '=') {//Estado Q12

                        estado = 0;
                        return new Token(Tag.RELOP_EQ, "==", n_line, n_column);

                    } else {//Estado Q13

                        estado = 0;
                        retornaPonteiro();
                        return new Token(Tag.RELOP_ASSIGN, "=", n_line, n_column);

                    }

                case 14:
                    if (c == '=') {//Estado Q15

                        estado = 0;
                        return new Token(Tag.RELOP_NQ, "!=", n_line, n_column);

                    } else {//Erro
                        sinalizaErro("invalido na linha " + n_line + " e coluna " + n_column);
                        return null;
                    }

                case 16:
                    if (Character.isLetterOrDigit(c) || c == '_') {//Estado 16
                        lexema.append(c);
                    } else {
                        estado = 0;
                        retornaPonteiro();

                        Token token = tabelaSimbolos.retornaToken(lexema.toString());
                        if (token == null) {
                            estado = 0;

                            return new Token(Tag.ID, lexema.toString(), n_line, n_column);
                        } else {
                            estado = 0;
                            return token;
                        }
                    }
                    break;

                case 18:
                    if (Character.isDigit(c)) {//Estado Q18

                        lexema.append(c);
                    } else if (c == '.') {//Estado Q20
                        lexema.append(c);
                        estado = 20;
                    } else {//Estado Q19
                        estado = 0;
                        if (c == ' ') {
                            retornaPonteiro();
                        }

                        return new Token(Tag.INTEGER, lexema.toString(), n_line, n_column);
                    }
                    break;

                case 20:
                    if (Character.isDigit(c)) {//Estado Q21
                        lexema.append(c);
                        estado = 21;
                    } else {
                        estado = 0;
                        sinalizaErro("ERRO! CUIDADO com a linha: " + n_line + " e coluna: " + n_column);
                        return null;
                    }
                    break;

                case 21:
                    if (Character.isDigit(c)) {//Estado Q21
                        lexema.append(c);
                    } else {//Estado Q22
                        retornaPonteiro();
                        estado = 0;
                        return new Token(Tag.DOUBLE, lexema.toString(), n_line, n_column);

                    }

                    break;
                case 23:
                    if (c == '"') {//Estado Q24
                        estado = 0;

                        return new Token(Tag.STRING, lexema.toString(), n_line, n_column);
                    } else if (c == '\n' || c == '\r') {
                        retornaPonteiro();
                        sinalizaErro("String não fechada antes de quebra de linha antes do fim de arquivo Linha: " + n_line + " Coluna: " + n_column);

                        return null;
                    } else if (lookahead == END_OF_FILE) {
                        sinalizaErro("String deve ser fechada com \" antes do fim de arquivo");
                        return null;
                    } else { // Se vier outro, permanece no estado 23
                        lexema.append(c);
                    }
                    break;

                case 24:
                    if (c == '/') {//verificaçao se e um tokem de divisao ou comentario
                        coluna_do_cometario = n_column - 2;
                        estado = 25;

                    } else if (c == '*') {
                        coluna_do_cometario = n_column - 2;
                        Linha_de_comentario = n_line;
                        estado = 26;

                    } else {

                        retornaPonteiro();
                        return new Token(Tag.RELOP_DIV, "/", n_line, n_column);
                    }

                    break;
                case 25:
                    if (Character.isLetter(c) || c == '_') {

                    } else if (lookahead == END_OF_FILE) {
                        sinalizaErro("Comentarionao  deve ser fechada com antes do fim de arquivo");
                        return null;
                    } else if (c == '\n' || c == '\r') {
                        estado = 0;
                        retornaPonteiro();

                        System.out.printf("Comentario de Linha '//' apartir da  Linha:%d da Coluna: %d ate a Coluna:%d \n", n_line, coluna_do_cometario, n_column);

                    }

                    break;
                case 26:
                    if (Character.isLetter(c) || c == '_') {

                    } else if (c == ' ' || c == '\n' || c == '\r') {
                        if (c == '\n' || c == '\r') {
                            n_column = 1;
                            n_line++;
                        }

                    } else if (c == '*') {
                        estado = 27;

                    }

                    break;
                case 27:
                    if (lookahead == END_OF_FILE) {
                        sinalizaErro("Comentarionao  deve ser fechada com antes do fim de arquivo");
                        return null;
                    } else if (c == '/') {
                        estado = 0;

                        System.out.printf("Comentario de Varias Linhas '/**/' começa na Linha:%d da Coluna: %d e termina na linha: %d e Coluna:%d \n", Linha_de_comentario, coluna_do_cometario, n_line, n_column);

                    } else {
                        retornaPonteiro();
                        sinalizaErro("comentario não fechado: " + n_line + " Coluna: " + n_column);
                        return null;

                    }

                    break;

            } // fim switch
        } // fim while
    } // fim metodo

    public static void main(String[] args) {
        //integrantes da dupla: Rodrigo Drumond de Paula RA:11623863
        //e 

        Lexer lexer = new Lexer("HelloJavinha.jvn"); // parametro eh um programa em Javinha
        Token token;

        // cria o objeto Tabela de Simbolos para inserir todas as palavras
        // reservadas do Javinha.
        tabelaSimbolos = new TS();

        // Enquanto não houver erros ou não for fim de arquivo:
        do {

            // o metodo proxToken() simula o AFD. Eh esse metodo que nos retorna
            // o token reconhecido. Iremos executar esse metodo ate o token
            // retornado for igual a null ou estivermos em fim de arquivo.
            token = lexer.proxToken();

            // Imprime token
            if (token != null) {
                System.out.println("Token: " + token.toString() + "\t Linha: "
                        + n_line + "\t Coluna: " + n_column);
            }

        } while (token != null && token.getClasse() != Tag.EOF);
        lexer.fechaArquivo();
    }
}
