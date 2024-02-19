IDS - Lab JavaRMIChat

Our Chat in Java using RMI for the IDS option. Everything on how it works is in comments.
To compile it, you can just javac *.java, works just fine. To test it, run the rmiregistry from the .class files location and you can run ChatServer and ChatClient from terminal with "java ChatClient " and "java ChatServer".
For the Client a login will be ask and you will directly be connected to server, after that every option will be available.

Client fonctionality :
- Join : When a ChatClient is launch his name is given in command line, it will automatically join the server (method join)
- Exit : method exit is invoked by writing "e" during mode selection, remove the current Cilent from the server.
- Broadcast : writing "b" during mode selection, after that the message you will put in console will be transfered to all connected clients.
- Whisper : writing "w " during mode selection, after that the message will be delivered to the specified client.


Server fonctionality :
- KeepInMind method is invoked to write in persistent history all event that occured on the chat, Client joining and leaving, broadcast message and whispered message.
- Recall method is used when a Client ask for printing history.