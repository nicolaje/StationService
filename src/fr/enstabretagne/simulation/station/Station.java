package fr.enstabretagne.simulation.station;
// magasin.java
//
// Source pour le TD3 de simulation
//

import java.util.LinkedList;

import edu.ensieta.simulation.*;


/**
 * Simule le fonctionnement d'un magasin (très simple!)
 * @author cantotp@wanadoo.fr
 */
public class Station
{
	
		public LinkedList<Queue> listeQueue= new LinkedList<Queue>();
		public LinkedList<Pompe> listePompe= new LinkedList<Pompe>();
		public Caisse caisse=new Caisse();
		public double taillemaxacceptable;
		
		
		public LinkedList<Queue> getListeQueue() {
			return listeQueue;
		}
		public void setListeQueue(LinkedList<Queue> listeQueue) {
			this.listeQueue = listeQueue;
		}
		public LinkedList<Pompe> getListePompe() {
			return listePompe;
		}
		public void setListePompe(LinkedList<Pompe> listePompe) {
			this.listePompe = listePompe;
		}
		public Caisse getCaisse() {
			return caisse;
		}
		public void setCaisse(Caisse caisse) {
			this.caisse = caisse;
		}
		
		
		
		public Station(int type) {  // type défini la station genérée
			
			this.listeQueue = new LinkedList<Queue>();;
			this.listePompe = new LinkedList<Pompe>();
			this.caisse = new Caisse();
			this.taillemaxacceptable=10; // a modifier
			
			listeQueue.add(new Queue());
			listePompe.add(new Pompe(false)); // ajout  d'une pompe normale
			
			listeQueue.add(new Queue());
			listePompe.add(new Pompe(false)); // ajout  d'une pompe normale
			
			listeQueue.add(new Queue());
			listePompe.add(new Pompe(true)); // ajout  d'une pompe normale
			
			for (int i = 0;i<3;i++){
			listeQueue.get(i).lieaunepompe(listePompe.get(i));	
							
			}
			
			
			
			
			
		}
		
		public boolean fileFree(boolean cb){
			
			boolean temp=true;
			
			for(int i =0;i<this.listeQueue.size();i++){
				System.out.println(this.listeQueue.get(i).getListePompe().getFirst().isCB()==cb);
				if(this.listeQueue.get(i).getListePompe().getFirst().isCB()==cb)    // on considere qu'une que n'est relier qu'a un type de station
				temp=temp&this.listeQueue.get(i).getTaille()<taillemaxacceptable;
			}
			return temp;
		}
		
		
		public Queue remplirQueue(boolean cb){
			int min = 200;
			int id=0;
			for(int i =0;i<this.listeQueue.size();i++){
				if(this.listeQueue.get(i).getListePompe().getFirst().isCB()==cb)  
					if (this.listeQueue.get(i).getTaille()<min){
						min=this.listeQueue.get(i).getTaille();
						id=i;
					}
						
				
			}
			
			System.out.println("rentre dans la queue"+id);
			this.listeQueue.get(id).addclient();
			
			
			return this.listeQueue.get(id);
		}
		
		public void addpompeTocaisse(Pompe p){ // ajout d'une pompe dans la fifo de la caisse
			
			this.caisse.addpompe(p);
			
		}
		
		public double gettempscaisse(){
			
			
			return this.caisse.gettempsattente();
		}
		
		public Pompe freecaisse(){
			
			return caisse.removepompe();
		}
		
		public Pompe getpompecbtime(double time){
			for(int i =0;i<this.listeQueue.size();i++){
				for(int j =0;j<this.listeQueue.get(i).getListePompe().size();j++){
					System.out.println(this.listeQueue.get(i).getListePompe().get(j).getTc()==time);
					if(this.listeQueue.get(i).getListePompe().get(j).getTc()==time){
						return this.listeQueue.get(i).getListePompe().get(j);
					}
						
				}
			}
			
			return null;
		}
	
		
		
		
		
		
	
}