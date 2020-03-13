package example.network;

import example.controller.Game;
import example.model.Table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {


    public static void main(String[] args) {
        //initializing the variables of server
        int port = 12345;
        short numberOfPlayers = 0;
        short turnOfPlayerNumber;
        boolean in = true;
        boolean isFinish = false;
        boolean rightMove;
        String s;
        List<Socket> players = new ArrayList<>();
        List<String> playersName = new ArrayList<>();
        List<ObjectOutputStream> ooslist = new ArrayList<>();
        List<ObjectInputStream> oislist = new ArrayList<>();

        Table gameTable = new Table();
        Game controller = new Game();

        try {
            //initializing the server
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is ready to receive the client!");

            //receiving the clients
            while (numberOfPlayers < 2) {

                if (numberOfPlayers == 0) {
                    players.add(0, serverSocket.accept());
                    ooslist.add(0, new ObjectOutputStream(players.get(0).getOutputStream()));
                    oislist.add(0, new ObjectInputStream(players.get(0).getInputStream()));
                    System.out.println("Attempt to receive the player's nickname...");
                    s = (String) oislist.get(0).readObject();

                    //it is the only player so i can confirm his choice
                    ooslist.get(0).writeObject("ok");
                    playersName.add(0, s);
                    System.out.println("The first player has chosen the nickname: " + playersName.get(0));
                }
                else if(numberOfPlayers == 1) {

                    players.add(1, serverSocket.accept());
                    ooslist.add(1, new ObjectOutputStream(players.get(1).getOutputStream()));
                    oislist.add(1, new ObjectInputStream(players.get(1).getInputStream()));
                    System.out.println("Attempt to receive the player's nickname...");

                    while (in) {

                        s = (String) oislist.get(1).readObject();

                        //i have to check if it is present or not
                        if (!s.equals(playersName.get(0))) {
                            //it is different so it is valid
                            ooslist.get(1).writeObject("ok");
                            playersName.add(1, s);
                            System.out.println("The first player has chosen the nickname: " + playersName.get(1));
                            in = false;
                        }
                        else {
                            //it is already chosen, so it is not valid
                            ooslist.get(1).writeObject("not ok");
                        }

                    }

                }
                numberOfPlayers++;
            }

            //the game can start
            ooslist.get(0).writeObject("start");
            ooslist.get(1).writeObject("start");
            System.out.println("The game is going to start!");
            turnOfPlayerNumber = 1;

            //this is the main cycle
            while (!isFinish) {

                //print the updated map
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        System.out.print(gameTable.getBoard()[i][j]);
                    }
                    System.out.println();
                }
                System.out.println("The player number " + turnOfPlayerNumber + "has to make a move...");
                System.out.println();

                //send the table
                ooslist.get(0).writeObject("map");
                ooslist.get(1).writeObject("map");
                int i = 0;
                int j = 0;
                while ( i < 5 ) {
                    while ( j < 5 ) {
                        ooslist.get(0).writeObject(gameTable.getBoard()[i][j]);
                        ooslist.get(1).writeObject(gameTable.getBoard()[i][j]);
                        j = j + 2;
                    }
                    j = 0;
                    i = i + 2;
                }


                //send the "your turn" to the actual player
                ooslist.get(turnOfPlayerNumber-1).writeObject("your turn");

                //send the "not your turn" to the actual player
                if (turnOfPlayerNumber == 1) ooslist.get(1).writeObject("not your turn");
                else ooslist.get(0).writeObject("not your turn");
                in = true;

                while (in) {
                    //waiting a response
                    short pos = (short) oislist.get(turnOfPlayerNumber-1).readObject();
                    rightMove = controller.rightMove( gameTable , pos);

                    //check the response
                    if (rightMove) {
                        //respond "valid"
                        controller.updateTheTable(gameTable, turnOfPlayerNumber, pos);
                        ooslist.get(turnOfPlayerNumber-1).writeObject("ok");
                        in = false;
                    }
                    else
                        ooslist.get(turnOfPlayerNumber-1).writeObject("not ok");

                }

                //i have to control if the game is finished (tie or if there is a winner)
                short tie = controller.isTie(gameTable);
                short win = controller.thereIsAWinner(gameTable);

                //0 is tie
                //1 the winner is the first player
                //2 the winner is the second player
                //3 there is no tie and no a winner
                if ( tie == 0 ) {
                    ooslist.get(0).writeObject("result");
                    ooslist.get(1).writeObject("result");
                    ooslist.get(0).writeObject("tie");
                    ooslist.get(1).writeObject("tie");
                    isFinish = true;
                    System.out.println("There is a tie!");
                }
                else if ( win == 1 ) {
                    ooslist.get(0).writeObject("result");
                    ooslist.get(1).writeObject("result");
                    ooslist.get(0).writeObject("you win");
                    ooslist.get(1).writeObject("you lose");
                    isFinish = true;
                    System.out.println("The first player is the winner!");
                }
                else if ( win == 2 ) {
                    ooslist.get(0).writeObject("result");
                    ooslist.get(1).writeObject("result");
                    ooslist.get(0).writeObject("you lose");
                    ooslist.get(1).writeObject("you win");
                    isFinish = true;
                    System.out.println("The second player is the winner!");
                }

                //else the game must go on

                //the turn is ending for the actual player
                if (turnOfPlayerNumber == 1) {
                    turnOfPlayerNumber = 2;
                }
                else {
                    turnOfPlayerNumber = 1;
                }

            }//end while ISFINISH

            //the game is finished
            //i have to close every streams and socket
            System.out.println("The server is closing all the connections...");
            ooslist.get(0).close();
            ooslist.get(1).close();
            oislist.get(0).close();
            oislist.get(1).close();
            players.get(0).close();
            players.get(1).close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}