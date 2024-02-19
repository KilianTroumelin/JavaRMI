import java.rmi.*; 
import java.rmi.server.*;
import java.util.ArrayList;
import java.rmi.registry.*;
public class ChatServer {

    
    /** 
     * The class implementing the server. Very simply, it exports its functionalities in the rmi registry.
     * For the functionnalities, @see ServerInterfaceImpl.
     * 
     * @param none
     */
    public static void main(String[] args) {
        try{
            ServerInterfaceImpl h = new ServerInterfaceImpl();
            ServerInterface h_stub = (ServerInterface) UnicastRemoteObject.exportObject(h, 0);

            // Register the remote object in RMI registry with a given identifier

            Registry registry = LocateRegistry.getRegistry(); 
            registry.rebind("ChatFunctions", h_stub);
            System.out.println ("Server ready");
        }
        catch(Exception e){

        }
    }
}
