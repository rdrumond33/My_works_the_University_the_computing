package compilador;

import java.util.HashMap;

public class Ts{
    
	private HashMap<Token, InfIdentificador> tabelaSimbolos; // Tabela de simbolos

   public Ts() {
   	tabelaSimbolos = new HashMap<Token, InfIdentificador>();

      // Inserindo as palavras reservadas
      Token word;
      word = new Token(Tag.KW, "algoritmo", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
        
      word = new Token(Tag.KW, "declare", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
          
      word = new Token(Tag.KW, "inicio", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "fim", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
        
      word = new Token(Tag.KW, "subrotina", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
        
      word = new Token(Tag.KW, "retorne", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "logico", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "numerico", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "literal", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
        
      word = new Token(Tag.KW, "nulo", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
      
      word = new Token(Tag.KW, "se", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "senao", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "enquanto", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "para", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
      
      word = new Token(Tag.KW, "ate", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
      
      word = new Token(Tag.KW, "faca", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());
      
      word = new Token(Tag.KW, "repita", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "escreva", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "leia", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "verdadeiro", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "falso", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

       word = new Token(Tag.KW, "ou", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "e", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

      word = new Token(Tag.KW, "nao", 0, 0);
      this.tabelaSimbolos.put(word, new InfIdentificador());

        
   }
    
	public void put(Token w, InfIdentificador i) {
   	tabelaSimbolos.put(w, i);
   }

   // Retorna um identificador de um determinado token
   public InfIdentificador getIdentificador(Token w) {
   	InfIdentificador infoIdentificador = (InfIdentificador) tabelaSimbolos.get(w);
      return infoIdentificador;
   }

   // Pesquisa na tabela de simbolos se ha algum tokem com determinado lexema
   // vamos usar esse metodo somente para diferenciar ID e KW
   public Token retornaToken(String lexema) {
   	for(Token token : tabelaSimbolos.keySet()) {
   	   if(token.getLexema().equals(lexema)) {
         	return token;
         }
      }
      return null;
	}
    
 
}