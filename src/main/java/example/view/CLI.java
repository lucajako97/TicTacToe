package example.view;

import example.model.Table;

public class CLI implements UI {

    private Table cliTable;

    public CLI() {
        this.cliTable = new Table();
    }

    public void printTable() {
        System.out.println();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(this.cliTable.getBoard()[i][j]);
            }
            System.out.println();
        }
    }

    public void updateTable(char first, char second, char third, char forth, char fifth, char sixth, char seventh, char eighth, char ninth) {
        this.cliTable.setBoard(first, second, third, forth, fifth, sixth, seventh, eighth, ninth);
    }
}

