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
 * ClientHandler.java
 *
 * <p>This class handles communication between the client
 * and the server.  It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast.
 * Handles the reallocation of username & it's uniqeness check.
 * Also handles the host and user commands.
 *
 */

public class ClientHandler implements Runnable {
  private Client connectionClient = null;
  private ArrayList<Client> clientList;

  ClientHandler(Client client, ArrayList<Client> clientList) {
    this.connectionClient = client;
    this.clientList = clientList;  // Keep reference to master list
  }

  //Sets up our vars
  String usernameMessage = "Enter Username: " + "\n";
  String usernameErrorMessage = "Username already exists." + "\n";
  String usernameUniqueMessage = "Username is unique." + "\n";

  String clientUsername;
  String clientText;
  boolean counted = false;

  /**
   * received input from a client.
   * sends it to other clients.
   */
  public void run() {
    try {
      System.out.println("Connection made with socket " + connectionClient);
      Socket clientSock = connectionClient.connectionSock;
      DataOutputStream connectionClientOutput = new DataOutputStream(clientSock.getOutputStream());
      BufferedReader clientInput = new BufferedReader(
          new InputStreamReader(connectionClient.connectionSock.getInputStream()));
      System.out.println("DataStream est.");
      if (clientList.size() == 1) {
        connectionClientOutput.writeBytes("You are the first connection. \n"
            + "Congradulations! You are the Host. \n");
        clientUsername = "Host";
      } else {
        System.out.println("Prompting Username");
        connectionClientOutput.writeBytes(usernameMessage);
        clientUsername = clientInput.readLine();
      }

      //Checks if username is unique
      int index = 0;
      while (index < clientList.size()) {
        if (!clientList.get(index).username.toUpperCase().equals(clientUsername.toUpperCase())) {
          index++;
        } else {
          connectionClientOutput.writeBytes(usernameErrorMessage);
          connectionClientOutput.writeBytes(usernameMessage);
          clientUsername = clientInput.readLine();
          index = 0;
        }
      }
      //Adds username to our client
      connectionClientOutput.writeBytes(usernameUniqueMessage);
      connectionClient.username = clientUsername;

      for (Client c : clientList) {
        if (c.connectionSock != connectionClient.connectionSock) {
          DataOutputStream clientOutput = new DataOutputStream(c.connectionSock.getOutputStream());
          clientOutput.writeBytes(
                  "<" + connectionClient.username + "> Has joined the chat." + "\n");
        }
      }
      while (true) {
        // Get data sent from a client
        clientText = clientInput.readLine();
        if (clientText != null) {
          if (clientText.equals("QUIT")) {
            System.out.println("Closing connection for socket " + connectionClient.connectionSock);
            for (Client c : clientList) {
              if (c.connectionSock != connectionClient.connectionSock) {
                DataOutputStream clientOutput = new DataOutputStream(
                        c.connectionSock.getOutputStream());
                clientOutput.writeBytes("<" + clientUsername + "> has left the chat" + "\n");
              }
            }
            clientList.remove(connectionClient);
            connectionClient.connectionSock.close();
            break;
          } else if (clientText.equals("Who?")) {
            System.out.println(
                "Received from " + "<" + connectionClient.username + "> : " + clientText);
            connectionClientOutput.writeBytes("Connected Clients: \n");
            for (Client c : clientList) {
              if (c.connectionSock != connectionClient.connectionSock) {
                System.out.println(c.username + ", ");
                connectionClientOutput.writeBytes("<" + c.username + "> \n");
              } else {
                if (clientList.size() == 1) {
                  connectionClientOutput.writeBytes("You are the only user in the chat. \n");
                }
              }
            }
          } else if (clientText.equals("QUESTION") && connectionClient.username.equals("Host")) {
            System.out.println(
                "Received from " + "<" + connectionClient.username + "> : " + clientText);

            connectionClientOutput.writeBytes("What is your question?");
            connectionClientOutput.writeBytes("\n");
            clientText = clientInput.readLine();
            System.out.println(
                "Received from " + "<" + connectionClient.username + "> : " + clientText);
            //Sends Question to all other clients
            for (Client c : clientList) {
              if (c.connectionSock != connectionClient.connectionSock) {
                DataOutputStream clientOutput = new DataOutputStream(
                    c.connectionSock.getOutputStream());
                clientOutput.writeBytes(
                    "<" + connectionClient.username + "> " + "QUESTION: " + clientText + "\n");
              }
            }
          } else if (clientText.equals("ANSWER") && connectionClient.username.equals("Host")) {
            System.out.println(
                "Received from " + "<" + connectionClient.username + "> : " + clientText);
            connectionClientOutput.writeBytes("Which username has the correct answer first?");
            connectionClientOutput.writeBytes("\n");


            while (!counted) {
              clientText = clientInput.readLine();
              System.out.println(
                  "Received from " + "<" + connectionClient.username + "> : " + clientText);
              //Sends Question to all other clients
              for (Client c : clientList) {
                if (c.connectionSock != connectionClient.connectionSock) {
                  DataOutputStream clientOutput = new DataOutputStream(
                      c.connectionSock.getOutputStream());
                  clientOutput.writeBytes("<" + connectionClient.username + "> "
                      + "User: " + clientText + " Answered correctly first! +1 point. \n");
                }
                if (c.username.equals(clientText)) {
                  c.score += 1;
                  counted = true;
                }
              }
              if (counted == false) {
                connectionClientOutput.writeBytes("Selected user: "
                    + clientText + " Does not exist. Try again. \n");
              } else {
                connectionClientOutput.writeBytes("Point allocated. \n");
              }
            }
            counted = false;
          } else if (clientText.equals("SCORE") && connectionClient.username.equals("Host")) {
            System.out.println(
                "Received from " + "<" + connectionClient.username + "> : " + clientText);
            connectionClientOutput.writeBytes("Scores Are: \n");
            for (Client c : clientList) {
              if (c.connectionSock != connectionClient.connectionSock) {
                System.out.println(c.username + ", ");
                connectionClientOutput.writeBytes("<" + c.username + ">: " + c.score + "  \n");
              } else {
                if (clientList.size() == 1) {
                  connectionClientOutput.writeBytes("There are no other users in the chat. \n");
                }
              }
            }
          } else {
            // Turn around and output this data
            // to all other clients except the one
            // that sent us this information
            System.out.println(
                    "Received from " + "<" + connectionClient.username + "> : " + clientText);
            for (Client c : clientList) {
              if (c.connectionSock != connectionClient.connectionSock) {
                DataOutputStream clientOutput = new DataOutputStream(
                        c.connectionSock.getOutputStream());
                clientOutput.writeBytes("<" + connectionClient.username + "> " + clientText + "\n");
              }
            }
          }
        } else {
          // Connection was lost
          System.out.println("Closing connection for socket " + connectionClient.connectionSock);
          // Remove from arraylist
          clientList.remove(connectionClient);
          connectionClient.connectionSock.close();
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      // Remove from arraylist
      clientList.remove(connectionClient);
    }
  }
}

