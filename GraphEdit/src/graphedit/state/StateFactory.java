package graphedit.state;

import graphedit.model.diagram.GraphEditModel;

import java.util.HashMap;
import java.util.Map;


/**
 * Factory objekat za stanja, postoji maksimalno jedna instanca svakog stanja, cime se stede resursi
 * posto bi se u suprotnom veoma cesto instancirala stanja
 * @author xxx
 *
 */
public class StateFactory {


	private Map<String, State> states = new HashMap<String, State>();
	private GraphEditModel model;
	
	
	public StateFactory(GraphEditModel model){
		this.model = model;
		
	}


	public State getState(String stateClassName){
		State state = states.get(stateClassName);
		if (state == null)
			try {
				//instanciraj putem refleksije
				state = (State) Class.forName(stateClassName).newInstance();
				state.setModel(model);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		states.put(stateClassName, state);
		return state;
	}

}