package jkms.jakomas;

import java.util.ArrayList;

//отдельный "цех" в программе, работает с ArrayList<Lexeme>
public class LexemeBuffer {

    private final ArrayList<Lexeme> lexemeArrayList;
    private int pos;

    LexemeBuffer(ArrayList<Lexeme> lexemeArrayList) {
        this.lexemeArrayList = lexemeArrayList;
    }

    public Lexeme getNextLexeme() {
        return lexemeArrayList.get(pos++);
    }
    public Lexeme getBehindLexeme() {
        return lexemeArrayList.get(pos--);
    }
    public Lexeme getCurrentLexeme() {
        return lexemeArrayList.get(pos);
    }

    //Важно! Всякий раз, когда используются эти методы, меняется значение pos. Следите за этим!
    public int  getPos()  { return pos; }
    public void backPos() { pos--; }
    public void NextPos() { pos++; }

}
