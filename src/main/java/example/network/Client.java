package example.network;

import example.view.CLI;
import example.view.UI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws UnknownHostException {
        int port = 12345;
        short whereTheSymbol = 0;
        InetAddress host = InetAddress.getLocalHost();
        //String playerName = "null";
        UI cli = new CLI();

        try {
            //initializing the socket connection with the server
            Socket socket = new Socket(host, port);
            Scanner scanner = new Scanner(System.in);
            String s;
            System.out.println("Welcome to the TicTacToc game");

            //create the streams to link to the server
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            boolean in = true;

            //the client insert a nickname and he has to rewrite it, if it is already chosen
            s = " ";
            while (in) {
                //write the nickname in a correct way
                while (s.equals(" ") || s.equals("")) {
                    System.out.println("Write your nickname:");
                    s = scanner.nextLine();
                }
                //playerName = s;
                oos.writeObject(s);
                s = (String) ois.readObject();
                //if it is not already chosen s, contains "ok"
                if (s.equals("ok")) {
                    in = false;
                    System.out.println("Your nickname is valid!");
                }
                else {
                    System.out.println("Your nickname is not valid!");
                    s = " ";
                }

            }

            //waiting the game starts
            System.out.println("Waiting for the other client...");
            while (!s.equals("start")) {
                s = (String) ois.readObject();
            }

            //the game can start
            in = true;
            System.out.println("The game is starting!");
            char updateSymbol1;
            char updateSymbol2;
            char updateSymbol3;
            char updateSymbol4;
            char updateSymbol5;
            char updateSymbol6;
            char updateSymbol7;
            char updateSymbol8;
            char updateSymbol9;

            while (in) {

                s = (String) ois.readObject();
                switch(s) {
                    case "map":
                        updateSymbol1 = (char) ois.readObject();
                        updateSymbol2 = (char) ois.readObject();
                        updateSymbol3 = (char) ois.readObject();
                        updateSymbol4 = (char) ois.readObject();
                        updateSymbol5 = (char) ois.readObject();
                        updateSymbol6 = (char) ois.readObject();
                        updateSymbol7 = (char) ois.readObject();
                        updateSymbol8 = (char) ois.readObject();
                        updateSymbol9 = (char) ois.readObject();


                        //i have to update the CLI and use it to print the board table
                        cli.updateTable( updateSymbol1, updateSymbol2, updateSymbol3, updateSymbol4, updateSymbol5, updateSymbol6, updateSymbol7, updateSymbol8, updateSymbol9 );
                        cli.printTable();
                        break;
                    case "your turn":
                        System.out.println("It is your turn!");
                        while (in) {

                            //i have to decide where put my symbol (1-9)
                            boolean goOn = false;
                            while (!goOn) {
                                goOn = true;
                                try {

                                    System.out.println("Choose the space from 1 to 9, where put your symbol: ");
                                    whereTheSymbol = scanner.nextShort();

                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid type, please insert a number...");
                                    goOn = false;
                                    scanner.nextLine();
                                }

                            }

                            oos.writeObject(whereTheSymbol);

                            //and waiting the server check
                            s = (String) ois.readObject();
                            if (s.equals("ok")) {
                                System.out.println("It is a valid move!");
                                in = false;
                            }
                            else {
                                System.out.println("It is not a valid move!");
                                System.out.println("Try again with another space...");
                            }

                        }

                        in = true;
                        break;
                    case "not your turn":
                        System.out.println("Now is the turn of your opponent!");
                        System.out.println("Wait your turn!");
                        break;
                    case "result":
                        //if the message receive from the server is the result of the match
                        //i have to print it and terminate the game
                        s = (String) ois.readObject();
                        if ( s.equals("you win"))
                            System.out.println("You are the winner! Congratulations!! :D ");
                        else if ( s.equals("tie") )
                            System.out.println("That's a tie! Anyway, well game! :) ");
                        else
                            System.out.println("You are the loser! I'm sorry :( ");
                        in = false;
                        break;
                    default:
                        System.out.println("This is the default case, something is going wrong!?");
                        break;
                }

            }

            //the game is finished, close the streams and the socket connection
            System.out.println("We will see at the next match! ;)");
            oos.close();
            ois.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
