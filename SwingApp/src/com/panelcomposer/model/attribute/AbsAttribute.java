package com.panelcomposer.model.attribute;

import java.awt.Point;

import com.panelcomposer.model.AbstractElement;

public abstract class AbsAttribute extends AbstractElement {

	protected String fieldName;
	protected Boolean hidden = false;
	protected Boolean disabled = false;
	protected String migConstant;
	protected Point position;
	protected Integer foregroundRGB;
	protected Integer backgroundRGB;
	protected Integer length;
	protected Integer precision;
	protected Boolean wrap = false;
	protected Boolean visible = true;
	protected Integer positionX;
	protected Integer positionY;
	protected Integer componentLength;
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	public Integer getForegroundRGB() {
		return foregroundRGB;
	}

	public void setForegroundRGB(Integer foregroundRGB) {
		this.foregroundRGB = foregroundRGB;
	}

	public Integer getBackgroundRGB() {
		return backgroundRGB;
	}

	public void setBackgroundRGB(Integer backgroundRGB) {
		this.backgroundRGB = backgroundRGB;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Boolean getWrap() {
		return wrap;
	}

	public void setWrap(Boolean wrap) {
		this.wrap = wrap;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getPositionX() {
		return positionX;
	}

	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	public Integer getPositionY() {
		return positionY;
	}

	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}

	public String getMigConstant() {
		return migConstant;
	}

	public void setMigConstant(String migConstant) {
		this.migConstant = migConstant;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getComponentLength() {
		return componentLength;
	}

	public void setComponentLength(Integer componentLength) {
		this.componentLength = componentLength;
	}

}
