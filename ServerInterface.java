import java.io.IOException;
import java.rmi.*;
public interface ServerInterface extends Remote {
    public void join (String clientName) throws RemoteException;
    public void exit (String clientName) throws RemoteException;
    public void broadcast (String clientName, String message) throws RemoteException;
    public void whisper (String clientName, String message, String dest) throws RemoteException;
    public void keepInMind (String s) throws IOException;
    public void recall (String clientName) throws RemoteException; 
}