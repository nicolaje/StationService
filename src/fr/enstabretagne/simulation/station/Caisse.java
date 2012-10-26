package fr.enstabretagne.simulation.station;
import edu.ensieta.simulation.EventQueue;
import java.util.LinkedList;

public class Caisse {
	
	public LinkedList<Pompe> fileattente;



	public Caisse() {
		
		this.fileattente = new LinkedList<Pompe>() ;
	}
	
	
	public void addpompe(Pompe p){
		fileattente.addFirst(p);
		System.out.println("++++++++++++++++");
	}
	
	public Pompe removepompe(){
		System.out.println("----------");
		Pompe temp=fileattente.getLast();
		fileattente.removeLast();
		return temp;
		
	}
	
	double gettempsattente(){
		
		double temp = 0;
	
		for(int i =0;i<this.fileattente.size();i++){
			temp=temp+fileattente.get(i).getTc();
			
		}
		return temp;
		
	}
	
	

}
