package fr.enstabretagne.simulation.logique;

// magasin.java
//
// Source pour le TD3 de simulation
//

import edu.ensieta.simulation.Event;
import edu.ensieta.simulation.EventQueue;
import edu.ensieta.simulation.MoreRandom;
import fr.enstabretagne.simulation.station.Pompe;
import fr.enstabretagne.simulation.station.Queue;
import fr.enstabretagne.simulation.station.Station;


/**
 * Simule le fonctionnement d'un magasin (très simple!)
 * @author cantotp@wanadoo.fr
 */
public class Simulation
{
	// Déclaration des valeurs des événements
	final static int ARRIVECLIENT 		= 0;
	final static int ARRIVECAISSE	= 1;
	final static int DEPARTCAISSE	= 2;
	final static int DEPARTCB       = 3;
	final static int FIN		= 99; // 	a modifier

	// *** PARAMETRES DE LA SIMULATION ***
	//
	// Paramètre loi arrivée clients
	final static double lambda1 = 10/1.0; // pour frequence F1
	final static double lambda2=20.0;  // pour frequence  F2



	// Paramètres de la simulation
	final static double tmax		= 20.0;		// Unité de temps = minute
	final static double tmin = 6.0;  //  Unité de temps = minute
	final static double t1 = 90;
	final static double t2 = 17.0;


	public static void doSimulation()
	{
		// *** VARIABLES DE LA SIMULATION ***

		// Variables statistiques
		int nbreClients 	= 0;
		int nbreInsatisfaits= 0;

		// Variables d'état et entités
		EventQueue fileEvt = new EventQueue(true);
		Station station = new Station(1);
		double tempsSimu =tmin;

		// Générateur aléatoire
		MoreRandom alea = new MoreRandom();

		// Afficher les paramètres
		// ...

		// *** INITIALISATION DE LA SIMULATION ***



		// Poster l'événement FIN à tmax
		fileEvt.postEvent(new Event(tmax, FIN, "Fin de la simulation après " + tmax + " jours", null));// a peut etre ne pas mettre et arreter traitement au dernier client

		// Poster les événements client jusqu'à tmax
		double t =tmin + alea.nextExp(lambda1);
		
		while (t < t1)
		{
			nbreClients++;
			fileEvt.postEvent(new Event(t, ARRIVECLIENT, "Arrivée client devant la station #" + nbreClients, nbreClients));
			t += alea.nextExp(lambda1);
			
		}

		while (t < t2)
		{
			nbreClients++;
			fileEvt.postEvent(new Event(t, ARRIVECLIENT, "Arrivée client devant la station #" + nbreClients, nbreClients));
			t += alea.nextExp(lambda2);
			
		}

		while (t < tmax)
		{
			nbreClients++;
			fileEvt.postEvent(new Event(t, ARRIVECLIENT, "Arrivée client devant la station #" + nbreClients, nbreClients));
			t += alea.nextExp(lambda1);
			
		}


		







		// *** BOUCLE DE SIMULATION ***
		// Lit les évenements dans le scheduler et les traite

		Event evt;		// Variable de travail pour l'événement courant
		boolean finalizeSimulation = false;	// Flag pour sortir de la boucle

		do
		{
			// Récupère l'évenement suivant
			evt = fileEvt.getNextEvent();
			// Vérifie que c'est un objet valide (sinon bug!)
			assert(evt == null);
			// Avance le temps de la simulation à la date de l'événement
			tempsSimu = evt.getTimeStamp();
			// Affiche une trace sur la console
			System.out.print(evt.toString());

			// Traite l'événement
			// variable temporaire de traitement
			Pompe tempPompe;
			Queue tempQueue;
			boolean cb;
			double tempvar=0;
			switch(evt.getType())
			{


			case ARRIVECLIENT:
				cb=alea.nextUniform(0, 1)<2.0/3; // chosir si le client veut passer en cb
				if (station.fileFree(cb))
				{
					tempQueue =station.remplirQueue(cb);
					
					if (tempQueue.isApompeFree())
					{

						tempPompe =tempQueue.remplispompe(); // ocupe la pompe et vide la queue
						if(!tempPompe.isCB()){
							System.out.print(" ==> et passe en a la pompe normale");
							tempvar=2;//tc a calculer
							tempPompe.setTc(tempvar); 
							station.addpompeTocaisse(tempPompe);
							fileEvt.postEvent(new Event(tempsSimu + tempvar,
									ARRIVECAISSE, "rentre en caisse " , null)); //calculer temps passer a la pompe
						}
						else {
							System.out.print(" ==> et passe en a la pompe CB");
							tempvar=2;//tc a calculer
							tempPompe.setTc((double)tempsSimu + tempvar);// tc = a calculer
							System.out.println(tempsSimu + tempvar);
							fileEvt.postEvent(new Event(tempsSimu + tempvar,
									DEPARTCB, "client CB sort de la station", null));//  calculer temps passer a la pompe

						}

					}
					else
						System.out.print(" ==> et attend de pouvoir se servir");

				}
				else
				{
					// Sinon, client insatisfait!
					nbreInsatisfaits++;
					System.out.print(" ==> ne rentre pas dans la station!");
				}


				System.out.println();
				break;

			case ARRIVECAISSE:
				double tempscaisse = station.gettempscaisse();

				fileEvt.postEvent(new Event(tempsSimu + tempscaisse,
						DEPARTCAISSE, "client passe"+ " en caisse", null)); //
				System.out.println(" ==> pattiente en caisse");
				break;

			case DEPARTCAISSE:
				tempPompe = station.freecaisse(); // enleve un client de la fifo de la caisse
				tempPompe.free();
				if(tempPompe.queueisnotEmpty()){

					tempPompe.addclient(0);// calculer le temps TC a passer en argument
					station.addpompeTocaisse(tempPompe);
					fileEvt.postEvent(new Event(tempsSimu +2,
							ARRIVECAISSE, "rentre en caisse " , null));
					System.out.println(" ==> part de la station");// calculer temps passer a la pompe

				}
				break;


			case DEPARTCB:

				Pompe tempspompe = station.getpompecbtime(tempsSimu); //cherche la station qui se libere
				tempspompe.free();
				System.out.println("hahaha");
				if(tempspompe.queueisnotEmpty()){
					tempvar=2;//tc a calculer
					tempspompe.addclient(tempsSimu + tempvar);// calculer le temps TC a passer en argument
					
					fileEvt.postEvent(new Event(tempsSimu + tempvar,
							DEPARTCB, "sors de la station apres cb " , null)); // calculer temps passer a la pompe

				}
				break;	
				
			case FIN:
				System.out.println(" ==> Finalize");
				finalizeSimulation = true;
				break;

			default:
				System.out.println(" ==> Inconnu!");
				break;
			}

		} while (!finalizeSimulation);//

		// *** RESULTATS DE LA SIMULATION ***

		// Affiche les variables statistiques
		System.out.println("Nombre de clients     = " + nbreClients);
		System.out.print  ("Nombre d'insatisfaits = " + nbreInsatisfaits);
		System.out.println(" (" + String.format("%2.2f", 100.0*nbreInsatisfaits/nbreClients) + "%)");


	}
}