package com.panelcomoser.components.exception;

/**
 * Izuzetak se baca ako se proba postaviti negativna vrednost u komponentu za unos iznosa
 * pri cemu to nije dozvoljeno
 */
@SuppressWarnings("serial")
public class NegativeValueException extends Exception{

	public NegativeValueException(String message){
		super(message);
	}
}
