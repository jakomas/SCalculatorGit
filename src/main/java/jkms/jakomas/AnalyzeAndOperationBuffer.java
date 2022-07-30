package jkms.jakomas;

import java.util.ArrayList;

import jkms.jakomas.Main.LexemeTypes;

class AnalyzeAndOperationBuffer {

    private final ArrayList<Lexeme> lexemeArrayList;
    public ArrayList<Lexeme> getLexemeArrayList() {
        return lexemeArrayList;
    }

    AnalyzeAndOperationBuffer(String user_input) {
        this.lexemeArrayList = analyze_user_input(user_input);
    }



    //реализация метода, необходимого для конструктора класса
    public ArrayList<Lexeme> analyze_user_input(String user_input) {
        ArrayList<Lexeme> lexemeArrayList = new ArrayList<>();
        int pos=0;

        while (pos < user_input.length()) {
            char ch = user_input.charAt(pos);
            switch (ch) {
                //мы ожидаем встретить следующие символы:
                case '(' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.L_BRACKET, ch)); pos++;}
                case ')' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.R_BRACKET, ch)); pos++;}
                case '*' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.OP_MUL,    ch)); pos++;}
                case '/' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.OP_DIV,    ch)); pos++;}
                case '^' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.OP_POW,    ch)); pos++;}
                case '+' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.OP_PLUS,   ch)); pos++;}
                case '-' -> {lexemeArrayList.add(new Lexeme(LexemeTypes.OP_MINUS,  ch)); pos++;}


                //мы не ожидаем встретить 'закрыть корень', только 'открыть корень',
                //а уже после проверяем, есть ли 'закрыть корень'.
                case '<' -> {//открыть корень '<|'
                    pos++;
                    if(pos >= user_input.length()) {
                        break;
                    }
                    ch = user_input.charAt(pos);
                    if(ch == '|') //подтверждает открытие корня
                        lexemeArrayList.add(new Lexeme(LexemeTypes.L_SQR, "<|"));
                    else //если не подтверждено открытие корня '<' + '|'
                        throw new RuntimeException(ErrorsClass.getErrorUserInput(0));

                    pos++;//новое значение для ch
                }


                //признак 'закрытия корня' != его закрытию, проверка на синтаксис
                case '|' -> { //непонятно часть какого оператора является '<|' или '|>'
                    //выяснить, есть ли дальше логическая 'пара' для него - '>'
                    pos++;
                    if(pos >= user_input.length()) {
                        break;
                    }

                    ch = user_input.charAt(pos);

                    if(ch == '>') //если пара равна '|>' - это операция закрытия корня
                        lexemeArrayList.add(new Lexeme(LexemeTypes.R_SQR, "|>"));
                    else {
                        //если логической пары не встретили, возможно
                        //пользователь допустил синтаксическую ошибку
                        for(Lexeme lexType : lexemeArrayList) {
                            if(lexType.getType().equals(LexemeTypes.L_SQR))
                                //да, левая скобка корня есть, неверно закрыли
                                throw new RuntimeException(ErrorsClass.getErrorUserInput(2));
                            else if (lexType.getType().equals(LexemeTypes.R_SQR)) {
                                //нет, до этого уже была правая скобка корня, неверно открыли новую
                                throw new RuntimeException(ErrorsClass.getErrorUserInput(1));
                            }
                        }
                    }
                    pos++;//новое значение для ch
                }


                //если это не операнды, а цифры - числа
                default -> {
                    //предыдущее pos++ с новым циклом поменяло ch
                    if(ch <= '9' && ch >= '0') {
                        StringBuilder sbNumeric = new StringBuilder();
                        do {
                            sbNumeric.append(ch);
                            pos++;

                            if(pos >= user_input.length())
                                break;
                            ch = user_input.charAt(pos);

                        } while (ch <= '9' && ch >= '0');

                        //накопили столько цифр, сколько смогли, получилось число
                        lexemeArrayList.add(new Lexeme(LexemeTypes.NUMBER, sbNumeric.toString()));
                    } else {
                        //Мы не ожидаем встретить '>'
                        //если встретили один из парных символов закрывающей скобки корня,
                        //то это - синтаксическая ошибка пользователя.
                        //По-хорошему, нужно выполнить проверку, выяснить, какой парный символ
                        //был до и после, что бы утверждать,
                        //какую именно ошибку допустил пользователь
                        if(ch == '>')
                            throw new RuntimeException(ErrorsClass.getErrorUserInput(3));
                        else
                            //если встретили то, что не предусмотрено
                            throw new RuntimeException("\nНепредусмотренный символ: "
                                +ch+" index: "+pos);
                    }
                }//the end default ->


            }//the end switch(ch);

        }//the end while(pos < user_input.length())


        //DONE - завершить работу, всегда последняя лексема
        lexemeArrayList.add(new Lexeme(LexemeTypes.DONE, ""));
        return lexemeArrayList;
    }//the end method analyze_user_input()

    static int plus_minus(LexemeBuffer lexemeBuffer) {
        int value = mul_div(lexemeBuffer);
        System.out.println("В plus_minus value: " + value);

        while (true) {
            Lexeme lexeme = lexemeBuffer.getNextLexeme();
            switch (lexeme.getType()) {
                case OP_PLUS -> value += mul_div(lexemeBuffer);
                case OP_MINUS -> value -= mul_div(lexemeBuffer);
                //не нашли операции сложение/вычитание
                default -> {
                    lexemeBuffer.backPos();//лексема была взята -> pos++, вернуть обратно
                    return value; //верни, value = mul_div()
                }
            }
        }
    }

    public static int mul_div(LexemeBuffer lexemeBuffer) {
        int value = pow(lexemeBuffer);
        System.out.println("В mul_div value: " + value);

        while (true) {
            Lexeme lexeme = lexemeBuffer.getNextLexeme();//pos++
            switch (lexeme.getType()) {
                case OP_MUL -> value *= pow(lexemeBuffer);
                case OP_DIV -> value /= pow(lexemeBuffer);
                default -> {
                    lexemeBuffer.backPos();//pos--
                    return value;
                }
            }
        }
    }


    public static int pow(LexemeBuffer lexemeBuffer) {
        int value = main_factor(lexemeBuffer);
        System.out.println("В pow value: " + value);

        while (true) {
            Lexeme lexeme = lexemeBuffer.getNextLexeme();//pos++
            if (lexeme.getType() == LexemeTypes.OP_POW) {
                value = (int) Math.pow(value, main_factor(lexemeBuffer));
            } else {
                lexemeBuffer.backPos();//pos--
                return value;
            }
        }
    }


    public static int main_factor(LexemeBuffer lexemeBuffer) {
        Lexeme lexeme = lexemeBuffer.getNextLexeme();

        switch(lexeme.getType()) {
            case NUMBER -> { return Integer.parseInt(lexeme.getValue()); }
            case L_SQR -> {
                int value = plus_minus(lexemeBuffer);
                lexeme = lexemeBuffer.getNextLexeme();

                if(!lexeme.getType().equals(LexemeTypes.R_SQR)) {
                    throw new RuntimeException(ErrorsClass.getErrorUserInput(4));
                }
                //посчитали подкоренное выражение, извлекли из него корень, вернули
                System.out.println("В main_factor value: " + Math.sqrt( value ));
                return (int) Math.sqrt( value );
            }


            case L_BRACKET -> {
                int value = plus_minus(lexemeBuffer);
                lexeme = lexemeBuffer.getNextLexeme();

                if(!lexeme.getType().equals(LexemeTypes.R_BRACKET)){
                    throw new RuntimeException("Нет закрывающей скобки. Некорректное выражение.");
                }
                //проверка пройдена, скобка закрыта, выражение внутри посчитано, вернуть результат
                return value;
            }


            default ->
                    throw new RuntimeException("Неизвестный тип лексемы: " +lexeme.getType()
                            + " на позиции: "+lexemeBuffer.getPos());

        }
    }


}
