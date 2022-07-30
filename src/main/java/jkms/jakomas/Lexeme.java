package jkms.jakomas;

import jkms.jakomas.Main.LexemeTypes;

//Лексема это - базовая абстрактная единица значения в user_input
class Lexeme {
    private final LexemeTypes lexemeType;
    private final String value;

    //конструктор, если value не одиночный символ
    Lexeme(LexemeTypes lexemeTypes, String value) {
        this.lexemeType = lexemeTypes;
        this.value = value;
    }
    //конструктор, если value одиночный символ-операнд
    Lexeme(LexemeTypes lexemeTypes, Character value) {
        this.lexemeType = lexemeTypes;
        this.value = value.toString();
    }
    //переопределение метода для Lexeme, если нужно её вывести
    @Override
    public String toString() {
        return "Lexeme{" +
                "lexemeTypes=" + lexemeType +
                ", value='" + value + '\'' +
                '}';
    }

    public LexemeTypes getType() { return lexemeType; }
    public String getValue() { return value; }
}
