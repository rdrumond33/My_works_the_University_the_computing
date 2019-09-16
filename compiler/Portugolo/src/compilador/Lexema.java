package compilador;

import java.io.*;

public class Lexema {

	private static final int END_OF_FILE = -1; // constante para fim do arquivo
	private static int lookahead = 0; // armazena o ultimo caractere lido do arquivo
	public static int n_line = 1; // contador de linhas
	public static int n_column = 1; // contador de colunas
	private RandomAccessFile instance_file; // referencia para o arquivo
	private static Ts tabelaSimbolos; // tabela de simbolos
	static int err = 0;

	/**
	 * Função Lexema abre o arquivo
	 */
	public Lexema(String input_data) {
		/**
		 * Abre instance_file de input_data
		 */
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

	/**
	 * fechaArquivo() fechar arquivo
	 */
	public void fechaArquivo() {

		try {
			instance_file.close();
		} catch (IOException errorFile) {
			System.out.println("Erro ao fechar arquivo\n" + errorFile);
			System.exit(3);
		}
	}

	/**
	 * sinalizaErro(char c, int n_line, int n_colum) Recebe o caracter que esta
	 * errado na variavel c com a linha e coluna e retorna uma menssagem
	 * 
	 */
	public void sinalizaErro(char c, int n_line, int n_colum) {
		++err;
		System.out.println("[Erro lexico]: Simbolo " + c + " invalido na linha " + n_line + " e coluna " + n_column);
		verificarQuebraLibha(c);

	}

	/**
	 * retornaPonteiro() retorna o ponteiro para o caracter anterior no arquivo e
	 * decrementa 1 na coluna
	 */
	public void retornaPonteiro() {

		try {
			if (lookahead != END_OF_FILE) {
				instance_file.seek(instance_file.getFilePointer() - 1);
				n_column--;
			}
		} catch (IOException e) {
			System.out.println("Falha ao retornar a leitura\n" + e);
			System.exit(4);
		}
	}

	/**
	 * verificarQuebraLibha(char c) se c e \n e retorna a coluna=1 e acresenta mais
	 * 1 a linha
	 */
	public void verificarQuebraLibha(char c) {

		if (c == '\n' || c == '\r') {
			n_column = 1;
			n_line++;

		}
	}

	/**
	 * proxToken() procura no arquivo e verificar erros de lexema
	 */
	public Token proxToken() {

		// variavel armaeza a palavra construida
		StringBuilder lexema = new StringBuilder();

		// variavel que representa o estado atual
		int estado = 0;

		// armazena o char corrente
		char c;

		while (true) {

			c = '\u0000'; // null char

			// avanca caractere
			try {

				lookahead = instance_file.read();
				if (lookahead != END_OF_FILE) {

					c = (char) lookahead;

					n_column++;
				}
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo");
				System.exit(3);
			}

			// movimentacao do automato
			switch (estado) {

			case 0:
				if (lookahead == END_OF_FILE) { // fim de arquivo

					return new Token(Tag.EOF, "EOF", n_line, n_column);
				} else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
					// Permance no estado = 0
					verificarQuebraLibha(c);
					// Reconhece o Tabulacao com 3 de espaco
					if (c == '\t')
						n_column += 3;
				} else if (c == '+') {// Estado Q1
					return new Token(Tag.RELOP_PLUS, "+", n_line, n_column);
				} else if (c == '-') {// Estado Q2
					return new Token(Tag.RELOP_MINUS, "-", n_line, n_column);
				} else if (c == '*') {// Estado Q3
					return new Token(Tag.RELOP_MULT, "*", n_line, n_column);
				} else if (c == '/') {
					estado = 4;
				} else if (Character.isLetter(c)) {// Estado Q11
					lexema.append(c);
					estado = 11;
				} else if (Character.isDigit(c)) {// Estado Q13
					lexema.append(c); // comecamos a construir um numero
					estado = 13; // vamos para o estado 13
				} else if (c == '"') {// Estado Q18
					lexema.append(c);
					estado = 18;
				} else if (c == ';') {// Estado Q21
					return new Token(Tag.SMB_PontoVirgula, ";", n_line, n_column);
				} else if (c == ',') {
					return new Token(Tag.SMB_SEMICOLON, ",", n_line, n_column);
				} else if (c == ')') {// Estado Q23
					return new Token(Tag.SMB_CP, ")", n_line, n_column);
				} else if (c == '(') {// Estado Q24
					return new Token(Tag.SMB_OP, "(", n_line, n_column);
				} else if (c == '=') {
					return new Token(Tag.RELOP_EQ, "=", n_line, n_column);
				} else if (c == '>') {
					estado = 26;// Estado Q26
				} else if (c == '<') {
					estado = 29;// Estado Q29
				} else {
					if (err == 0) {

						sinalizaErro(c, n_line, n_column);
					}

				}
				break;
			/************************************************************************************************************************************/

			case 4:
				if (c == '*') {
					estado = 6;
				} else if (c == '/') {
					estado = 9;
				} else {
					return new Token(Tag.RELOP_DIV, "/", n_line, n_column);
				}

				break;

			case 5:

				if (c == '=') {// Estado Q6
					estado = 0;
					return new Token(Tag.RELOP_LE, "<=", n_line, n_column);
				} else {// Estado Q7
					estado = 0;
					retornaPonteiro();
					return new Token(Tag.RELOP_LT, "<", n_line, n_column);
				}

			case 6:
				if (c == '*') {
					estado = 7;

				} else if (c == '\n') {
					n_line++;
					n_column = 1;
				} else if (c != '*') {

				} else {

					sinalizaErro(c, n_line, n_column);
				}

				break;

			case 7:
				if (c == '/') {
					n_column++;
					estado = 0;
				} else if (c == '*') {
					if (err == 0) {

						sinalizaErro(c, n_line, n_column);
					} else {

					}
				} else {
					if (c == '\n') {
						n_column = 1;
						n_line++;
					}

					if (err == 0) {
						sinalizaErro(c, n_line, n_column);

					} else if (lookahead == END_OF_FILE) {
						return new Token(Tag.EOF, "EOF", n_line, n_column);

					}

				}

				break;
			case 8:
				if (c == '=') {// Estado Q9
					estado = 0;
					return new Token(Tag.RELOP_LE, ">=", n_line, n_column);

				} else {// Estado Q10
					estado = 0;
					retornaPonteiro();
					return new Token(Tag.RELOP_GT, ">", n_line, n_column);
				}
			case 9:
				if (c == '\n') {
					n_line++;
					n_column = -1;
					estado = 0;
				}

				break;
			case 11:
				if (Character.isLetterOrDigit(c)) {
					lexema.append(c);
				} else {
					estado = 0;
					retornaPonteiro();

					Token token = tabelaSimbolos.retornaToken(lexema.toString().toLowerCase());

					if (token == null) {
						return new Token(Tag.ID, lexema.toString(), n_line, n_column);
					} else if (token.getLexema().equalsIgnoreCase(lexema.toString())) {
						return token;
					} else {
						return new Token(Tag.ID, lexema.toString(), n_line, n_column);
					}

				}

				break;
			case 13:

				if (Character.isDigit(c)) {
					lexema.append(c);
				} else if (c == '.') {
					estado = 15;
					lexema.append(c);

				} else {
					estado = 0;
					retornaPonteiro();

					return new Token(Tag.Numerico, lexema.toString(), n_line, n_column);
				}

				break;
			case 15:

				if (!Character.isDigit(c)) {// Q15 e Q16
					if (err == 0) {
						sinalizaErro(c, n_line, n_column);

					}
				} else {// Q17
					lexema.append(c);
					estado = 0;
					return new Token(Tag.Numerico, lexema.toString(), n_line, n_column);
				}

				break;

			case 18:

				if (c == '"') {// Q20
					if (err == 0) {

						sinalizaErro(c, n_line, n_column);

					} else {
						lexema.append(c);
						estado = 0;
						return new Token(Tag.Literal, lexema.toString().replace("\n\t", "").replace("\n", ""), n_line,
								n_column);
					}
				} else if (c != '"' && ((int) c >= 0 || (int) c <= 250)) {
					verificarQuebraLibha(c);
					lexema.append(c);
					err++;
				}

				break;

			case 26:
				if (c == '=') {
					return new Token(Tag.RELOP_GE, ">=", n_line, n_column);
				} else {
					estado = 0;
					retornaPonteiro();
					return new Token(Tag.RELOP_GT, ">", n_line, n_column);
				}
			case 29:
				if (c == '-') {// Q33
					estado = 33;

				} else if (c == '=') {// Q31
					return new Token(Tag.RELOP_LT, "<=", n_line, n_column);

				} else if (c == '>') {// Q30
					return new Token(Tag.RELOP_LT_GT, "<>", n_line, n_column);

				} else {
					estado = 0;
					retornaPonteiro();
					return new Token(Tag.RELOP_LT, "<", n_line, n_column);
				}

				break;

			case 33:

				if (c == '-') { // Q34

					return new Token(Tag.RELOP_ARTIB, "<--", n_line, n_column);
				} else {
					if (err == 0) {

						sinalizaErro(c, n_line, n_column);

					} else {
						if (lookahead == END_OF_FILE) {
							retornaPonteiro();
							estado = 0;
						}
					}

				}

				break;

			} 
		}
	}

	public static void main(String[] args) {
		Lexema lexer = new Lexema("portugolo\\primeiro_portugolo.ptgl"); // diretorio

		Token token;
		// cria o objeto Tabela de Simbolos para inserir todas as palavras
		// reservadas do Portugolo.
		tabelaSimbolos = new Ts();

		do {

			token = lexer.proxToken();

			// Imprime token
			if (token != null) {
				System.out.println("Token:" + token.toString() + "\t Linha: " + n_line + "\t Coluna: " + n_column);
			}

		} while (token != null && token.getClasse() != Tag.EOF);
		lexer.fechaArquivo();
	}
}
