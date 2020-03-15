package example.network;

import example.view.CLI;
import example.view.UI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        int port = 12345;
        short whereTheSymbol = 0;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Socket socket = null;
        UI cli = new CLI();

        try {
            InetAddress host = InetAddress.getLocalHost();
            //initializing the socket connection with the server
            socket = new Socket(host, port);
            Scanner scanner = new Scanner(System.in);
            String s;
            System.out.println("Welcome to the TicTacToc game");

            //create the streams to link to the server
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
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
                oos.writeObject("keep alive");
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
                    case "keep alive":
                        //only to keep alive the connection
                        //oos.writeObject("keep alive");
                    case "you win for retired":
                        System.out.println("You are the winner! The other player is retired!! Congratulations!! :D ");
                    default:
                        System.out.println("This is the default case, something is going wrong!?");
                        break;
                }

            }

            //the game is finished, close the streams and the socket connection
            System.out.println("We will see at the next match! ;)");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("The server connection is down...");
            System.out.println("Try again...");

        } finally {

            try {
                System.out.println("Closing the output stream...");
                oos.close();
            } catch (IOException | NullPointerException e) {
                System.out.println("The output stream is not been set up ...");
                System.out.println("Or it was open when the connection crashed...");
            }
            try {
                System.out.println("Closing the input stream...");
                ois.close();
            } catch (IOException | NullPointerException e) {
                System.out.println("The input stream is not been set up ...");
                System.out.println("Or it was open when the connection crashed...");
            }
            try {
                System.out.println("Closing the socket...");
                socket.close();
            } catch (IOException | NullPointerException e) {
                System.out.println("The socket is not been set up ...");
            }

        }

    }// END OF MAIN

}
