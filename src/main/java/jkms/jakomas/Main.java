package jkms.jakomas;

import java.util.Scanner;

public class Main {
    //enum - перечисление, типов Lexeme
    enum LexemeTypes {
        L_BRACKET, R_BRACKET, //левая и правая скобки
        NUMBER,               //число
        OP_POW,               //степень
        L_SQR, R_SQR,         //границы подкоренного выражения
        OP_MUL, OP_DIV,       //делить - умножить
        OP_PLUS, OP_MINUS,    //плюс и минус
        DONE,                 //завершить работу с лексемами
    }

    static int checkOnStart(LexemeBuffer lexemeBuffer) {
        Lexeme lexeme = lexemeBuffer.getNextLexeme();//pos++

        if(lexeme.getType().equals(LexemeTypes.DONE))
            throw new RuntimeException("Некорректное выражение");
        else {
            //если, проверка пройдена - откатить назад pos,
            //работа с первой lexeme index 0, а не lexeme index 1
            lexemeBuffer.backPos();
            return AnalyzeAndOperationBuffer.plus_minus(lexemeBuffer);
        }
    }
    public static void main(String[] args) {
        String user_input = new Scanner(System.in)
                .nextLine()
                .trim()
                .replace(" ","");
        System.out.println("Вы ввели: " + user_input);
        //экземпляр класса, содержит динамический массив из Lexeme
        AnalyzeAndOperationBuffer anOpBuffer = new AnalyzeAndOperationBuffer(user_input);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(anOpBuffer.getLexemeArrayList());
        System.out.println("Результат: " + checkOnStart(lexemeBuffer));
    }
}