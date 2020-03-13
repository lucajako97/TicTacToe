package example.model;

import java.io.Serializable;

public class Table implements Serializable, Cloneable{

    private char[][] board;

    public Table() {
        this.board = new char[][] {
                {' ', '|', ' ', '|', ' '},
                {'-', '+', '-', '+', '-'},
                {' ', '|', ' ', '|', ' '},
                {'-', '+', '-', '+', '-'},
                {' ', '|', ' ', '|', ' '}
        };
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char first, char second, char third, char forth, char fifth, char sixth, char seventh, char eighth, char ninth) {
        this.board[0][0] = first;
        this.board[0][2] = second;
        this.board[0][4] = third;
        this.board[2][0] = forth;
        this.board[2][2] = fifth;
        this.board[2][4] = sixth;
        this.board[4][0] = seventh;
        this.board[4][2] = eighth;
        this.board[4][4] = ninth;
    }

    public void setSymbol(short numberOfPlayer, short position) {
        char symbol = ' ';

        if (numberOfPlayer == 1)  symbol = 'X';
        else symbol = 'O';

        switch (position) {
            case 1:
                this.board[0][0] = symbol;
                break;
            case 2:
                this.board[0][2] = symbol;
                break;
            case 3:
                this.board[0][4] = symbol;
                break;
            case 4:
                this.board[2][0] = symbol;
                break;
            case 5:
                this.board[2][2] = symbol;
                break;
            case 6:
                this.board[2][4] = symbol;
                break;
            case 7:
                this.board[4][0] = symbol;
                break;
            case 8:
                this.board[4][2] = symbol;
                break;
            case 9:
                this.board[4][4] = symbol;
                break;
            default:
                break;
        }
    }


}
