/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.event;

import java.util.EventObject;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SelectionModelUpdateEvent extends EventObject {
    
    public SelectionModelUpdateEvent(Object source) {
        super(source);
    }
}
