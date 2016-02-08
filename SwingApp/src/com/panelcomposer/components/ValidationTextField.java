package com.panelcomposer.components;


import java.sql.Types;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import com.panelcomposer.components.document.MaxCharactersDocumentFilter;
import com.panelcomposer.components.document.MaxDigitsDocumentFilter;
import com.panelcomposer.components.document.StartsWithZeroMaxDigitsDocumentFilter;
import com.panelcomposer.validation.Patterns;


/**
 * Tekstualno polje sa parametrima za validaciju
 * Nesto se dobija na osnovu posmatranja kolone
 * Dodatna ogranicenja unosi korisnik pri generisanju
 * @author xxx
 *
 */
public class ValidationTextField extends JTextField implements IValidationTextField{

	private static final long serialVersionUID = 1L;

	/**
	 * Indikator da li polje moze ostati prazno
	 */
	private Boolean required;

	/**
	 * Pattern po kom se mora uneti 
	 */
	private Pattern pattern;

	/**
	 * Maksimalna vrednost broja koji se unosi
	 */
	private Long maxValue;

	/**
	 * Minimalna vrednost broja koji se unosi
	 */
	private Long minValue;

	/**
	 * Maksimalni broj karaktera
	 */
	private Integer maxSize;

	/**
	 * Minimalni broj karaktera
	 */
	private Integer minSize;

	/**
	 * Indikator da li se smeju samo cifre unositi
	 */
	private Boolean onlyDigits;

	/**
	 * Indikator da li unos broja sme poceti sa nulom (recimo PTT ne moze i sl.) 
	 */
	private Boolean canStartWithZero = true;
	
	/**
	 * Originalni okvir, na sta se vraca pri dobijanju fokusa
	 */
	private Border originalBorder;
	
	private String patternKey;
	
	public ValidationTextField(){
		
	}
	
	public ValidationTextField(int nullable, int type, int size, int columns){
		super(columns);
		required = nullable == 0;
		onlyDigits = isIntNumericType(type);
		if (size != -1)
			maxSize = size;
		else
			maxSize = 1000;
		setValues(type);
		setDocumentFilter();
		originalBorder = getBorder();
	}
	
	public ValidationTextField(boolean required, String javaType, int columns){
		super(columns);
		this.required = required;
		onlyDigits = isJavaNumericType(javaType);
		maxSize = 1000;
		setValues(javaType);
		setDocumentFilter();
		originalBorder = getBorder();
		
	}
	
	public ValidationTextField(int nullable, int type, int size, boolean onlyDigits, boolean canStartWithZero, int columns){
		this(nullable, type, size, columns);
		this.onlyDigits = onlyDigits;
		if (isIntNumericType(type))
			this.onlyDigits = true;
		this.canStartWithZero = canStartWithZero;
		setDocumentFilter();
	}

	public ValidationTextField(int nullable, int type, int size,int columns, String pattern, boolean key){
		this(nullable, type, size, columns);
		if (!key)
			this.pattern = Pattern.compile(pattern);
		else{
			this.pattern = Patterns.getPattern(pattern);
			this.patternKey = pattern;
		}
	}

	/**
	 * Postavlja odgovarajuci document filter
	 */
	private void setDocumentFilter(){
		AbstractDocument doc = (AbstractDocument)getDocument();
		DocumentFilter filter;

		if (!canStartWithZero){
			filter=new StartsWithZeroMaxDigitsDocumentFilter(maxSize, onlyDigits);
		}
		else  if (onlyDigits)
			filter=new MaxDigitsDocumentFilter(maxSize);
		else
			filter = new MaxCharactersDocumentFilter(maxSize);
			
		doc.setDocumentFilter(filter);		

	}

	private boolean isIntNumericType(int type){
		return  type == Types.BIGINT || type == Types.SMALLINT 
				|| type == Types.INTEGER || type == Types.TINYINT;
	}
	
	private boolean isJavaNumericType(String type){
		return type.equals("Integer") || type.equals("Short") || type.equals("Long")
				 || type.equals("Byte");
	}

	private void setValues(int type){
		if (!isIntNumericType(type))
			return;
		if (type == Types.SMALLINT)
			maxValue =  (long) Short.MAX_VALUE;
		else if (type == Types.INTEGER)
			maxValue = (long) Integer.MAX_VALUE;
		else
			maxValue = Long.MAX_VALUE;

		maxSize = maxValue.toString().length();
	}
	
	private void setValues(String javaType){
		if (!isJavaNumericType(javaType))
			return;
		if (javaType.equals("Byte"))
			maxValue = (long) Byte.MAX_VALUE;
		if (javaType.equals("Short"))
			maxValue =  (long) Short.MAX_VALUE;
		else if (javaType.equals("Long"))
			maxValue = (long) Integer.MAX_VALUE;
		else
			maxValue = Long.MAX_VALUE;

		maxSize = maxValue.toString().length();
	}

	@Override
	public boolean isEditValid(){
		String text = getText();
		if (required && text.length() == 0)
			return false;
		if (minSize != null)
			if (text.length() < minSize)
				return false;
		if (text.length() > 0){
			if (onlyDigits){
				Long value = Long.parseLong(text);
				if (maxValue != null && value > maxValue)
					return false;
				if (minValue != null && value < minValue)
					return false;
			}
			if (pattern != null){
				return pattern.matcher(text).matches();
			}
		}
		return true;
	}
	
	public String getRequitements(){
		String ret = "";
		if (required)
			ret += "Obavezno polje. ";
		if (minSize != null)
			ret += "Minimalni broj karaktera " + minSize + ". ";
		if (maxSize != null)
			ret += "Maksimalni broj karaktera " + maxSize + ". ";
		if (onlyDigits)
			ret += "Moze sadrzati samo cifre. ";
		if (!canStartWithZero)
			ret += "Ne sme poceti nulom. ";
		if (pattern != null){
			if (patternKey == null)
				ret += "Mora biti oblika "+pattern.pattern() + ". ";
			else
				ret += "Mora biti validan "+patternKey + ".";
		}
		if (maxValue != null)
			ret += "Maksimalna vrednost je " + maxValue + ". ";
		if (minValue != null)
			ret += "Minimalna vrednost je " + minValue + ". ";
		
		return ret;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Long getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Long maxValue) {
		this.maxValue = maxValue;
	}

	public Long getMinValue() {
		return minValue;
	}

	public void setMinValue(Long minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
		setDocumentFilter();
	}

	public Integer getMinSize() {
		return minSize;
	}

	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}

	public Boolean getOnlyDigits() {
		return onlyDigits;
	}

	public void setOnlyDigits(Boolean onlyDigits) {
		this.onlyDigits = onlyDigits;
		setDocumentFilter();
	}

	public Boolean getCanStartWithZero() {
		return canStartWithZero;
	}

	public void setCanStartWithZero(Boolean canStartWithZero) {
		this.canStartWithZero = canStartWithZero;
		setDocumentFilter();
	}

	public Border getOriginalBorder() {
		return originalBorder;
	}

}
