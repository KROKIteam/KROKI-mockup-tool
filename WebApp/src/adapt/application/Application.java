package adapt.application;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class Application {

	public static void main(String[] args) {
		
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, 8182);
			component.getClients().add(Protocol.FILE);
			component.getClients().add(Protocol.HTTP);
			component.getDefaultHost().attach(new AdaptApplication());
			component.start();
			System.out.println("[APPLICATION] up and running...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
