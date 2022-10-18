# dockerchat
A copy of the mtchat repo with updated docker files.

This repo contains programs to implement a multi-threaded TCP chat server and client

* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client
* ClientHandler.java receives messages from a client and relays it to the other clients.
* Client.java has the constructor class instructions for saving username and socket info when passed by MtServer.
* Dockerfile handles set up instructions regarding copying and runing files in the docker container when initialized.


## Identifying Information

* Name: Jeevan Acharya,
* Student ID: 2313321,
* Email: acharya@chapman.edu,
* Course: CPSC 353 - 01
* Assignment: PA 04

* Name: Marco Mauricio
* Student ID: 2344979
* Email: Mauricio@chapman.edu
* Course: CPSC-353-01
* Assignment: PA04

* Name: Joseph Sneifer
* Student ID: 2351513
* Email: sneifer@chapman.edu

## Source Files

* MtServer.java
* MtClient.java
* ClientListener.java
* ClientHandler.java
* Client.java
* Dockerfile

## References

* Professor Fahy

## Known Errors

## Build Insructions

* Start docker
* docker image build -t server .

* Open another terminal, we will call this terminal 2
* docker image build -t client .



## Execution Instructions
For the server terminal
* docker container run --rm -it --name server -p 9009:9009 server
Windows users may need to run this command:
* winpty docker container run --rm -it --name server -p 9009:9009 server
For the client terminal(s)
for each terminal window we want to use as a client, change the name of the client
  for each one in place of the "rename" in the command below.
* docker container run --rm -it --name rename client

## Commands for Host
* QUESTION - Allows Host to type and send out a question to all users.
* ANSWER - Allows Host to type the name of the winning user to allocate points.
* SCORE - Returns list of users in chat with their specific scores.

##Commands for all Users
* Who? - Returns a list of users in the chat.

## Contributions

* Jeevan: Completed PA04.
* Marco: Worked on username checking and goodbye condition
* Joseph: Helped debug and worked on goodbye condition
