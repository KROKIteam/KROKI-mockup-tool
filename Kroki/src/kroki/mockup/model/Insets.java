/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model;

import java.io.Serializable;

/**
 * Class whose attributes represent distance between a compoenent's border and it's parent component
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class Insets implements Serializable{

    /**Top inset */
    public int top;
    /**Bottom inset */
    public int bottom;
    /**Left inset */
    public int left;
    /**Right inset */
    public int right;

    public Insets(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public Insets(){
        this.top = 3;
        this.bottom = 3;
        this.left = 3;
        this.right = 3;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Insets other = (Insets) obj;
        if (this.top != other.top) {
            return false;
        }
        if (this.bottom != other.bottom) {
            return false;
        }
        if (this.left != other.left) {
            return false;
        }
        if (this.right != other.right) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.top;
        hash = 89 * hash + this.bottom;
        hash = 89 * hash + this.left;
        hash = 89 * hash + this.right;
        return hash;
    }

    @Override
    public String toString() {
        return "Insets{" + "top=" + top + "bottom=" + bottom + "left=" + left + "right=" + right + '}';
    }
}
