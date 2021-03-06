package de.dfki.lt.data;

/**
 * A simpel calss for defining tuples.
 * 
 * @author gune00
 *
 * @param <L>
 * @param <R>
 */
public class Pair<L,R> {
	private L l;
	private R r;
	public Pair(L l, R r){
		this.l = l;
		this.r = r;
	}
	public L getL(){ return l; }
	public R getR(){ return r; }
	public void setL(L l){ this.l = l; }
	public void setR(R r){ this.r = r; }
	
	public String toString (){
		return "<" + this.getL() + "," + this.getR() + ">";
	}
}
