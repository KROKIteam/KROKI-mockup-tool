/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model;

import java.io.Serializable;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Margins implements Serializable{

    public int top = 1;
    public int bottom = 1;
    public int left = 1;
    public int right = 1;

    public Margins() {
    }

    public void setMargins(int top, int bottom, int right, int left) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
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
}
