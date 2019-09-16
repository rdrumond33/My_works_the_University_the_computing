package compilador;


/**
 * Tag
 */
public enum Tag {

    // fim de arquivo
    EOF,

    // Operadores
    RELOP_LT, // <
    RELOP_LE, // <=
    RELOP_GT, // >
    RELOP_GE, // >=
    RELOP_LT_GT, // <>
    RELOP_EQ, // =
    RELOP_PLUS, // +
    RELOP_MINUS, // -
    RELOP_MULT, // *
    RELOP_DIV, // /
    RELOP_NQ, // !=
    RELOP_ARTIB, // <--
    // Simbolos
    SMB_OP, // (
    SMB_CP, // )
    SMB_SEMICOLON, // ,
    SMB_PontoVirgula, // ;

    // identificador
    ID,

    // numeros
    Numerico,

    // strings
	Literal,

    // palavra reservada
    KW;
}