import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatClient {
    static String name;
    
    /** 
     * @param args - should be localhost
     * Creates a client identified by their name, and sends its functionalities to the rmi registry.
     * When launched, the client get asked his name, and is connected to the server. If you want to reconnect to the server,
     * you have to relaunch the application. On each messages you have to first choose the thing you want to do by entering a letter :
     * - b is broadcast, type b, then return, then your message and then return.
     * - w is whisper, type w <the name of the person you're trying to reach>, return, type your message then return. If the person you're trying to
     * doesn't exists, nothing happens and the message isn't written in the historic.
     * - e is exit. Just type e, then return, and you'll be out of there.
     * - h is historic. Type h, then return, and you'll get the full historic of the server.
     * 
     */
    public static void main(String[] args) {
        
        try{
            if (args.length < 1){
                System.out.println("Usage: java HelloClient <rmiregistry host>");
	        return;}
            ClientInterfaceImpl cli = new ClientInterfaceImpl();
            ClientInterface c_stub = (ClientInterface) UnicastRemoteObject.exportObject(cli, 0);

            String mode;
            Scanner s = new Scanner(System.in).useDelimiter("\\n");
            String host = args[0];
            System.out.println("Enter your name : ");
            name = s.next();
            Registry registry = LocateRegistry.getRegistry(host);
            registry.rebind(name, c_stub);
            ServerInterface chatFun = (ServerInterface) registry.lookup("ChatFunctions");
                        
            chatFun.join(name);
            do {
                mode = s.next();
                if (mode.length() < 1){
                    System.out.println("Please choose a mode");
                    continue;
                }
                else if (mode.charAt(0) == 'b'){
                    chatFun.broadcast(name, s.next());
                }
                else if (mode.charAt(0) == 'w'){
                    if (mode.split("\\s+").length != 2){
                        System.out.println("Usage: w <name of the recipient>");
                    }
                    try{
                        String dest = mode.split("\\s+")[1];
                        chatFun.whisper(name, s.next(), dest);
                    }
                    catch(Exception e){
                        System.out.println("Usage: w <name of the recipient>");
                    }
                }
                else if (mode.charAt(0) == 'e'){
                    chatFun.exit(name);
                    registry.unbind(name);
                }
                else if (mode.charAt(0) == 'h'){
                    chatFun.recall(name);
                }
                else {
                    System.out.println("Please choose a mode - b for broadcast or w for whisper - or exit - e.");      
                }
            } while (mode.length()< 1 || mode.charAt(0) != 'e') ;
            s.close();
        }
        catch(Exception e){
            System.err.println("Error in client");
            e.printStackTrace();
        }
        
     }
}