import java.rmi.*;
public interface ClientInterface extends Remote {
    public void notify(String s) throws RemoteException;
}