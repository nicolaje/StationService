package fr.enstabretagne.simulation.station;

public class Pompe {

	public boolean status;//status libre/occupé
	public boolean CB;//carte bancaire
	public double Tc; // temps de passage a la caisse si status = false sinon heure de sortie de la caisse
	public Queue Q;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isCB() {
		return CB;
	}
	public void setCB(boolean cB) {
		CB = cB;
	}
	
	
	public double getTc() {
		return Tc;
	}
	public void setTc(double tc) {
		Tc = tc;
	}
	
	public void setQ(Queue q){
		
		this.Q=q;
	}

	public Pompe( boolean cB) {
		
		this.status = true;
		this.CB = cB;
		this.Tc=0;
		Q=null;
		
	
	}
	
	
	
	public void free(){
		
		
		this.status=true;
	}
	
	
public boolean queueisnotEmpty(){
	
	return this.Q.getTaille()>0;
}

public void addclient(double Tc){ // change status et vide la queue d'attente et ajoute son temps de passage a la caisse Tc
	this.Tc=Tc;
	this.Q.videqueue();
	
}

	
}
