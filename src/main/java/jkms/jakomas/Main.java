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

    public static void main(String[] args) {
        String user_input = new Scanner(System.in)
                //взять следующую строку
                .nextLine()
                //исключить пробелы сначала и после строки
                .trim()
                //исключить пробелы внутри строки подменой
                .replace(" ","");
        System.out.println("Вы ввели: " + user_input);
    }
}