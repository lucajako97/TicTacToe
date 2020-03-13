package example.controller;

import example.model.Table;

public class Game {

    public boolean rightMove(Table gameboard, short pos) {
        boolean result = true;
        switch (pos) {
            case 1:
                if ( gameboard.getBoard()[0][0] != ' ' ) result = false;
                break;
            case 2:
                if ( gameboard.getBoard()[0][2] != ' ' ) result = false;
                break;
            case 3:
                if ( gameboard.getBoard()[0][4] != ' ' ) result = false;
                break;
            case 4:
                if ( gameboard.getBoard()[2][0] != ' ' ) result = false;
                break;
            case 5:
                if ( gameboard.getBoard()[2][2] != ' ' ) result = false;
                break;
            case 6:
                if ( gameboard.getBoard()[2][4] != ' ' ) result = false;
                break;
            case 7:
                if ( gameboard.getBoard()[4][0] != ' ' ) result = false;
                break;
            case 8:
                if ( gameboard.getBoard()[4][2] != ' ' ) result = false;
                break;
            case 9:
                if ( gameboard.getBoard()[4][4] != ' ' ) result = false;
                break;
            default:
                break;
        }
        return result;
    }

    public void updateTheTable(Table gameBoard, short numberOfPlayer, short pos) {
        gameBoard.setSymbol(numberOfPlayer, pos);
    }

    public short isTie(Table gameBoard) {
        //0 is tie
        //1 the winner is the first player
        //2 the winner is the second player
        //3 there is no tie and no a winner
        short tie = 3;
        if (
                gameBoard.getBoard()[0][0] != ' ' &&
                        gameBoard.getBoard()[0][2] != ' ' &&
                        gameBoard.getBoard()[0][4] != ' ' &&
                        gameBoard.getBoard()[2][0] != ' ' &&
                        gameBoard.getBoard()[2][2] != ' ' &&
                        gameBoard.getBoard()[2][4] != ' ' &&
                        gameBoard.getBoard()[4][0] != ' ' &&
                        gameBoard.getBoard()[4][2] != ' ' &&
                        gameBoard.getBoard()[4][4] != ' '   ) {

            tie = 0;
        }

        return tie;
    }

    public short thereIsAWinner(Table gameBoard) {
        //0 is tie
        //1 the winner is the first player
        //2 the winner is the second player
        //3 there is no tie and no a winner
        short winner = 3;
        if (
                gameBoard.getBoard()[0][0] == 'X' && gameBoard.getBoard()[0][2] == 'X' && gameBoard.getBoard()[0][4] == 'X' ||
                        gameBoard.getBoard()[2][0] == 'X' && gameBoard.getBoard()[2][2] == 'X' && gameBoard.getBoard()[2][4] == 'X' ||
                        gameBoard.getBoard()[4][0] == 'X' && gameBoard.getBoard()[4][2] == 'X' && gameBoard.getBoard()[4][4] == 'X' ||
                        gameBoard.getBoard()[0][0] == 'X' && gameBoard.getBoard()[2][0] == 'X' && gameBoard.getBoard()[4][0] == 'X' ||
                        gameBoard.getBoard()[0][2] == 'X' && gameBoard.getBoard()[2][2] == 'X' && gameBoard.getBoard()[4][2] == 'X' ||
                        gameBoard.getBoard()[0][4] == 'X' && gameBoard.getBoard()[2][4] == 'X' && gameBoard.getBoard()[4][4] == 'X' ||
                        gameBoard.getBoard()[0][0] == 'X' && gameBoard.getBoard()[2][2] == 'X' && gameBoard.getBoard()[4][4] == 'X' ||
                        gameBoard.getBoard()[0][4] == 'X' && gameBoard.getBoard()[2][2] == 'X' && gameBoard.getBoard()[4][0] == 'X'
        )
        {
            winner = 1;
        }
        else if (
                gameBoard.getBoard()[0][0] == 'O' && gameBoard.getBoard()[0][2] == 'O' && gameBoard.getBoard()[0][4] == 'O' ||
                        gameBoard.getBoard()[2][0] == 'O' && gameBoard.getBoard()[2][2] == 'O' && gameBoard.getBoard()[2][4] == 'O' ||
                        gameBoard.getBoard()[4][0] == 'O' && gameBoard.getBoard()[4][2] == 'O' && gameBoard.getBoard()[4][4] == 'O' ||
                        gameBoard.getBoard()[0][0] == 'O' && gameBoard.getBoard()[2][0] == 'O' && gameBoard.getBoard()[4][0] == 'O' ||
                        gameBoard.getBoard()[0][2] == 'O' && gameBoard.getBoard()[2][2] == 'O' && gameBoard.getBoard()[4][2] == 'O' ||
                        gameBoard.getBoard()[0][4] == 'O' && gameBoard.getBoard()[2][4] == 'O' && gameBoard.getBoard()[4][4] == 'O' ||
                        gameBoard.getBoard()[0][0] == 'O' && gameBoard.getBoard()[2][2] == 'O' && gameBoard.getBoard()[4][4] == 'O' ||
                        gameBoard.getBoard()[0][4] == 'O' && gameBoard.getBoard()[2][2] == 'O' && gameBoard.getBoard()[4][0] == 'O'
        )
        {
            winner = 2;
        }


        return winner;
    }

}
