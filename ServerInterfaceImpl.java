import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerInterfaceImpl implements ServerInterface {

	/**
	 * The server functionlities. Requires a List (or an Hashmap) to remember which client to notify,
	 * the rmi registry, a file to write the historic in, and the writer and reader parts to deal with it.
	 * For now, and it's just a choice, the server looks for clientname, which means if you whispers to Pablo,
	 * and there's 2 Pablo in the client list, it will only notify one of them but may not be the correct one.
	 * We could have used IDs, that wouldn't change the implementation too much.
	 *  
	 */
	ArrayList <ClientInterface> clients;
	Registry registry;
	File historyfile;
	FileReader fr;
	BufferedReader br;
	FileWriter fw;
	BufferedWriter bw;
	
	public ServerInterfaceImpl() throws IOException {
		clients = new ArrayList<ClientInterface>();
		try {
			registry = LocateRegistry.getRegistry();
		}
		catch(RemoteException e){
			System.err.println("Remote Problem !");
		}
		historyfile = new File("historyfile.txt");
	}
	
	
	/** 
	 * @param clientName - A string. The name of the client who joins.
	 * Adds the client to the ArrayList of clients to be notified by broadcast, or to be looked into
	 * when whispering by looking into the registry for its functionalities.
	 * Notifies everyone that someone joined the chat (and ask to put it into the historic.)
	 *
	 * @throws RemoteException
	 */
	public synchronized void join(String clientName) throws RemoteException {
		try {
			ClientInterface cli = (ClientInterface) registry.lookup(clientName);
			clients.add(cli);
			String s = clientName + " has joined the group!" ;
			for (ClientInterface clientInterface : clients) {
				clientInterface.notify(s);
			}
			keepInMind(s);
		} catch(Exception e){}
	}

	
	/** 
	 * @param clientName
	 * Removes the client of the ArrayList of clients to be notified by broadcast, or to be looked into
	 * when whispering by looking into the registry for its functionalities.
	 * Notifies everyone that someone left the chat (and ask to put it into the historic.)
	 * @throws RemoteException
	 */
	public synchronized void exit(String clientName) throws RemoteException {
		try {
			ClientInterface cli = (ClientInterface) registry.lookup(clientName);
			String s = clientName + " has left the group!" ;
			for (ClientInterface clientInterface : clients) {
				clientInterface.notify(s);
			}
			clients.remove(cli);
			keepInMind(s);
		}catch (Exception e){}
	}
	
	/** 
	 * @param clientName - a String
	 * @param message - a String
	 * 
	 * Pretty straightforward. Look into the ArrayList of Clients, and notify each of them with the message (which means 
	 * call the notify function, that just prints the message on each of their standard output.)
	 * Ask the correct funtion to write the message in historic.
	 * @throws RemoteException
	 */
	public synchronized void broadcast (String clientName, String message) throws RemoteException {
		try {
			String s = clientName + " says : " + message ;
			for (ClientInterface clientInterface : clients) {
				clientInterface.notify(s);
			}
			keepInMind(s);
		}catch (Exception e){}
	}

	
	/** 
	 * @param clientName - A String
	 * @param message - A String
	 * @param dest - A String, the recipient of the whisper.
	 * Directs the message to only one of the client.
	 * Note that, and we could just remove line 114 to stop doing so, we register whispers in the historic aswell.
	 * 
	 * @throws RemoteException
	 */
	public synchronized void whisper(String clientName, String message, String dest) throws RemoteException {
		try {
			String s = clientName + " whispered : " + message ;
			ClientInterface cli = (ClientInterface) registry.lookup(dest);
			cli.notify(s);
			keepInMind(s);
		}catch (Exception e){}
	}
	
	
	/** 
	 * @param s - a String, what's to be noted in the historic. If there's no historic file yet, creates it. Else, appends s.
	 * @throws IOException
	 */
	public synchronized void keepInMind(String s) throws IOException{
		FileWriter fw = new FileWriter(historyfile, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(s);
		bw.newLine();
		bw.close();
		fw.close();
	}

	
	/** 
	 * @param clientName - A String
	 * On request, sends to the client the historic if it exists. Else, just notifies him that there's none.
	 * 
	 * @throws RemoteException
	 */
	public synchronized void recall(String clientName) throws RemoteException{
		try {
			ClientInterface cli = (ClientInterface) registry.lookup(clientName);
			if (!historyfile.exists()){
				cli.notify("There's no history on this server yet.");
			}
			else {
				fr = new FileReader(historyfile);
				br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null){
					cli.notify(s);
				}
				br.close();
				fr.close();
			}
		}
		catch(Exception e){
			System.err.println("yolo");
		}
	}
}
