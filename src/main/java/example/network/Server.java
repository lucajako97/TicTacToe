package example.network;

import example.controller.Game;
import example.model.Table;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {



    public static void main(String[] args) {
        //initializing the variables of server
        int port = 12345;
        short turnOfPlayerNumber;
        boolean in;
        boolean goodNickname;
        boolean isFinish = false;
        boolean rightMove;
        String s;
        List<ClientForServer> players = new ArrayList<>();
        /*
        List<Socket> players = new ArrayList<>();
        List<String> playersName = new ArrayList<>();
        List<ObjectOutputStream> ooslist = new ArrayList<>();
        List<ObjectInputStream> oislist = new ArrayList<>();
        */
        Table gameTable = new Table();
        Game controller = new Game();
        HandlerConnection handlerConnection = new HandlerConnection();

        try {
            //initializing the server
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is ready to receive the client!");

            //receiving the clients
            while (players.size() < 2) {
                goodNickname = false;
                System.out.println("Waiting to receive the #" + (players.size()+1) + " player...");
                ClientForServer clientForServer = new ClientForServer(serverSocket.accept());
                players.add(clientForServer);

                while ( !goodNickname ) {

                    System.out.println("Attempt to receive the #" + players.size() + " player's nickname...");

                    try {
                        s = (String) players.get(players.size()-1).getOis().readObject();
                        //i have to check if it is already present in the other players
                        //i have to check the connection and the nickname
                        handlerConnection.checkTheConnections(players);
                        goodNickname = handlerConnection.checkNickname(s, players);

                        if ( goodNickname ) {
                            players.get(players.size()-1).getOos().writeObject("ok");
                            players.get(players.size()-1).setNickname(s);
                            System.out.println("The #" + players.size() + " player has chosen the nickname: " + players.get(players.size()-1).getNickname());

                        }
                        else {
                            players.get(players.size()-1).getOos().writeObject("not ok");
                            System.out.println("The # " + players.size() + " player has chosen an invalid nickname...");

                        }

                    } catch ( IOException e) {
                        System.out.println("The #" + players.size() + " player is disconnected...");
                        players.get(players.size()-1).getSocket().close();
                        players.remove(players.size()-1);
                        goodNickname = true;
                    }

                }

            }

            //the game can start
            players.get(0).getOos().writeObject("start");
            players.get(0).getOis().readObject();
            players.get(1).getOos().writeObject("start");
            players.get(1).getOis().readObject();
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
                System.out.println("The player number " + turnOfPlayerNumber + ": [" + players.get(turnOfPlayerNumber-1).getNickname() + "] has to make a move...");
                //System.out.println();

                //send the table
                players.get(0).getOos().writeObject("map");
                players.get(1).getOos().writeObject("map");
                int i = 0;
                int j = 0;
                while ( i < 5 ) {
                    while ( j < 5 ) {
                        players.get(0).getOos().writeObject(gameTable.getBoard()[i][j]);
                        players.get(1).getOos().writeObject(gameTable.getBoard()[i][j]);
                        j = j + 2;
                    }
                    j = 0;
                    i = i + 2;
                }

                //send the "your turn" to the actual player
                players.get(turnOfPlayerNumber-1).getOos().writeObject("your turn");

                //send the "not your turn" to the actual player
                if (turnOfPlayerNumber == 1) players.get(1).getOos().writeObject("not your turn");
                else players.get(0).getOos().writeObject("not your turn");
                in = true;

                while (in) {
                    //waiting a response
                    short pos = (short) players.get(turnOfPlayerNumber-1).getOis().readObject();
                    rightMove = controller.rightMove( gameTable , pos);

                    //check the response
                    if (rightMove) {
                        //respond "valid"
                        controller.updateTheTable(gameTable, turnOfPlayerNumber, pos);
                        players.get(turnOfPlayerNumber-1).getOos().writeObject("ok");
                        in = false;
                    }
                    else
                        players.get(turnOfPlayerNumber-1).getOos().writeObject("not ok");

                }

                //i have to control if the game is finished (tie or if there is a winner)
                short tie = controller.isTie(gameTable);
                short win = controller.thereIsAWinner(gameTable);

                //i have to control also if there is a disconnected player

                short disconnection = (short) handlerConnection.whichIsNotDisconnected(players);

                //0 is tie
                //1 the winner is the first player
                //2 the winner is the second player
                //3 there is no tie and no a winner
                if ( tie == 0 ) {
                    players.get(0).getOos().writeObject("result");
                    players.get(1).getOos().writeObject("result");
                    players.get(0).getOos().writeObject("tie");
                    players.get(1).getOos().writeObject("tie");
                    isFinish = true;
                    System.out.println("There is a tie!");
                }
                else if ( win == 1 ) {
                    players.get(0).getOos().writeObject("result");
                    players.get(1).getOos().writeObject("result");
                    players.get(0).getOos().writeObject("you win");
                    players.get(1).getOos().writeObject("you lose");
                    isFinish = true;
                    System.out.println("The first player is the winner!");
                }
                else if ( win == 2 ) {
                    players.get(0).getOos().writeObject("result");
                    players.get(1).getOos().writeObject("result");
                    players.get(0).getOos().writeObject("you lose");
                    players.get(1).getOos().writeObject("you win");
                    isFinish = true;
                    System.out.println("The second player is the winner!");
                }
                else if ( disconnection != -1) {
                    // there is a disconnection
                    players.get(disconnection).getOos().writeObject("you win for retired");
                    isFinish = true;
                }

                //else the game must go on

                //the turn is ending for the actual player
                if (turnOfPlayerNumber == 1) {
                    turnOfPlayerNumber = 2;
                }
                else {
                    turnOfPlayerNumber = 1;
                }

            }//end while isFinish

            //the game is finished
            //i have to close every streams and socket
            System.out.println("The server is closing all the connections...");
            players.get(0).closeConnections();
            players.get(1).closeConnections();

            while (players.size() != 0) {
                players.remove(0);
            }

            serverSocket.close();

        } catch (IOException | ClassNotFoundException e) {
            //here i have to check how is the player who is disconnected
            if (isFinish) {
                e.printStackTrace();
            } else {

                int number = handlerConnection.whichIsNotDisconnected(players);
                try {
                    players.get(number).getOos().writeObject("you win for retired");
                    players.get(number).closeConnections();
                    players.remove(number);
                    players.remove(0);
                    System.out.println("The player #" + number + " has win because the other one retired");

                } catch (IOException e1) {
                    System.out.println("Every player is disconnected...");
                }

            }

        }
        finally {

            while (players.size() != 0) {
                players.remove(0);
            }

        }

    }

}