import java.rmi.*;
public class ClientInterfaceImpl implements ClientInterface {

	/**
	 * The client service. Only needs to display what it's notified via the server's functionalities.
	 * 
	 * 
	 */
	public ClientInterfaceImpl() {
	}
	
	
	/** 
	 * @param s the String that's gonna be printed.
	 */
	public void notify(String s){
		System.out.println(s);
	}
}
