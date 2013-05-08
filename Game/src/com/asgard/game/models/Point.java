package com.asgard.game.models;

import java.io.Serializable;


/**
 * A PODO for representing coordinates
 * 
 * @author Benjamin
 * 
 */
public class Point implements Serializable {

	/**
	 * Used for serialization
	 */
	private static final long serialVersionUID = -7181200764331703680L;
	
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "x: " + x + " y: " + y;
	}
}
