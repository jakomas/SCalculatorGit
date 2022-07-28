package jkms.jakomas;

import jkms.jakomas.Main.LexemeTypes;

//Лексема это - базовая абстрактная единица значения в user_input
class Lexeme {
    LexemeTypes lexemeTypes;
    String value;

    //конструктор, если value не одиночный символ
    Lexeme(LexemeTypes lexemeTypes, String value) {
        this.lexemeTypes = lexemeTypes;
        this.value = value;
    }
    //конструктор, если value одиночный символ-операнд
    Lexeme(LexemeTypes lexemeTypes, Character value) {
        this.lexemeTypes = lexemeTypes;
        this.value = value.toString();
    }
    //переопределение метода для Lexeme, если нужно её вывести
    @Override
    public String toString() {
        return "Lexeme{" +
                "lexemeTypes=" + lexemeTypes +
                ", value='" + value + '\'' +
                '}';
    }
}
