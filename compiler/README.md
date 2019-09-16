# Compilador de uma lingagem chamada ***Portugolo***
- Contem:
  - [x] Analisador Léxico.
  - [x] Analisador Sintático.
  - [x] Analisador Semântico.
  
## Gramática da linguagem PortuGolo.

![Portugolo](https://user-images.githubusercontent.com/31081356/64989310-9efa8d80-d8a3-11e9-975c-116bebdcf342.png)

### Padrões para números, literais e identificadores do PortuGolo:
```
digito = [0-9]
letra = [A-Z | a-z]
Numerico = digito+(.digito+)?
ID = letra (letra | digito)*
Literal = pelo menos um dos 256 caracteres do conjunto ASCII entre aspas duplas
```

#### Outras características de PortuGolo:

- Regras:
  - As palavras-chave de PortuGolo são reservadas.
  - A linguagem possui comentários de mais de uma linha. Um comentário começa com `/*` e deve terminar com `*/`.
  - A linguagem possui comentários de uma linha. Um comentário começa com `//`.
  - A linguagem não é case-sensitive.
  - Cada tabulação, deverá contar como 3 espaços em branco.
