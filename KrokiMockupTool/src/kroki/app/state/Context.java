/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.state;

import java.util.HashMap;
import kroki.app.KrokiMockupToolApp;
import kroki.app.controller.TabbedPaneController;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Context {

    private State currentState;
    private HashMap<String, State> stateMap;
    private TabbedPaneController tabbedPaneController;

    /*inicijalizacija hash mape*/
    {
        stateMap = new HashMap<String, State>();
        stateMap.put(State.ADD_STATE, new AddState(this));
        stateMap.put(State.MOVE_STATE, new MoveState(this));
        stateMap.put(State.SELECT_STATE, new SelectState(this));
        stateMap.put(State.RESIZE_STATE, new ResizeState(this));
        stateMap.put(State.TRANSFORM_TO_AGGREGATED_STATE, new TransformToAggregatedState(this));
        stateMap.put(State.TRANSFORM_TO_CALCULATED_STATE, new TransformToCalculatedState(this));
        stateMap.put(State.TRANSFORM_TO_COMBOZOOM_STATE, new TransformToCombozoomState(this));
    }

    public Context(TabbedPaneController tabbedPaneController) {
        this.tabbedPaneController = tabbedPaneController;
        currentState = stateMap.get(State.SELECT_STATE);
    }

    public void goNext(String name) {
        currentState = stateMap.get(name);
        KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getStatusMessage().setText(currentState.getDisplayName());
    }

    public State getCurrentState() {
        return currentState;
    }

    public TabbedPaneController getTabbedPaneController() {
        return tabbedPaneController;
    }

    public State getState(String name) {
        return stateMap.get(name);
    }
}
