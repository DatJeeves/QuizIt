import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Jeevan Acharya
 * 2313321
 * CPSC 353 - 01
 * PA03
 * Client.java
 *
 * <p>This class is the basis for
 * each client connection. Keeps
 * track of client scores, usernames
 * and socket connections.
 *
 */

public class Client {
    public Socket connectionSock = null;
    public String username = "";
    public int score;

    Client(Socket sock, String username, int score) {
        this.connectionSock = sock;
        this.username = username;
        this.score = score;
    }
}
//