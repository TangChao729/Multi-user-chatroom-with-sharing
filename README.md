# Multi-user chartroom with drawing and file sharing 

This software is a Java application designed to create a collaborative environment through a shared whiteboard platform that include multiple features. The application provides real-time synchronization of drawings and messages across connected clients as well as file sharing. The application also provides user management capabilities like kicking a user and synchronizing user lists. 

## Key Components

### MessageReadyToConnectThread

This is a Thread responsible for listening and accepting incoming message connections on a defined port. For each connection, it creates a `ChatMessageThread`, which is then added to a shared list (`CreateWhiteBoard.messageConnections`), which represents all active message connections.

### ReadyToConnectThread

This is another Thread that listens for general incoming connections related to the whiteboard. Upon receiving a connection, it creates a `ConnectionThread` for handling the client's requests and adds it to the shared list (`CreateWhiteBoard.connections`) of active connections.

### Synchronizer

The `Synchronizer` class handles the task of ensuring all connected clients have the same view of the whiteboard and user list. It provides methods to synchronize messages, whiteboard drawings, user lists and handle user removals (either through a user leaving or being kicked). It directly interacts with the active connections and sends necessary updates to keep all clients synced.

### CreateWhiteBoard

`CreateWhiteBoard` appears to be a central class that maintains the state of the whiteboard, including active connections (`CreateWhiteBoard.connections`), active message connections (`CreateWhiteBoard.messageConnections`), and current usernames (`CreateWhiteBoard.usernames`).

## How to Use

1. **Starting the Server**: Start the WhiteBoard Manager server application by running the `CreateWhiteBoard` class. Make sure to correctly set up the listening ports for message and whiteboard connections (`MessageReadyToConnectThread.messagePortNumber` and `ReadyToConnectThread.portNumber`).

2. **Connecting Clients**: Clients can connect to the server using the appropriate port. Upon connection, a new `ConnectionThread` or `ChatMessageThread` will be created to handle the client's requests.

3. **Using the Whiteboard**: Clients can draw on their local whiteboard and the drawings will be synchronized across all clients.

4. **Messaging**: Clients can send messages to all other clients. Messages are sent to the server, which then broadcasts them to all other connected clients.

5. **User Management**: The server can kick users or handle user disconnections, and it will synchronize the user list across all clients.

## Prerequisites

The application requires Java Development Kit (JDK). Please ensure that you have it installed and properly configured on your system.

## Contributing

Contributions are welcome. Please open a new issue to discuss your ideas or submit a Pull Request with your changes.

## License

This project is licensed under the MIT license.

## Issues

For any problems or bugs, please open a new issue in the GitHub repository.
