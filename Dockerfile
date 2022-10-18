FROM openjdk:15

COPY MtServer.java /deployments/
COPY ClientHandler.java /deployments/
COPY Client.java /deployments/
COPY MtClient.java /deployments/
COPY ClientListener.java /deployments/
RUN cd /deployments && javac *.java
CMD cd /deployments; java MtServer
CMD cd /deployments; java MtClient



