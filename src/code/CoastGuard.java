package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class CoastGuard extends GenericSearchProblem{

	public static int min = 5;
	public static int max = 15;
	static ArrayList<Location>stateSpace = new ArrayList<>();
	int capacity;
	
    public CoastGuard() {
		super();
		
	}

	public static String genGrid() {
		//choose random dimensions for n,m between 5,15 inclusively
		int n = (int)Math.floor(Math.random()*(max-min+1)+min) ;
		int m = (int)Math.floor(Math.random()*(max-min+1)+min) ;
		String dimension = m+","+n;
		String capacity = (int)Math.floor(Math.random()*(100-30+1)+30)+"";
		String guard = GenerateGuardLocation(n,m);
		
		//create state space contains all possible states(locations) in the grid
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				if(i != Integer.parseInt(guard.split(",")[0]) || 
				   j != Integer.parseInt(guard.split(",")[1]))
				    stateSpace.add(new Location(i,j));
			}
		}
		
		int noOfShips = (int)Math.floor(Math.random()*(stateSpace.size()-1)+1);
		int noOfsStations = (int)Math.floor(Math.random()*(stateSpace.size()-1-noOfShips)+1);
		Collections.shuffle(stateSpace);
		String ships = GenerateShips(noOfShips);
		String stations = GenerateStations(noOfsStations);	
		//System.out.println(noOfShips+" : "+ships);
		//System.out.println(noOfsStations+" : "+stations);

		return dimension+";"+capacity+";"+guard+";"+stations+";"+ships;
		}
		
	
	
	public static String solve(String grid,String strategy, boolean visualize) {
		String solution = "";
		switch(strategy) {
		    case "BF" : 
		    	BFS bfs = new BFS();
				solution =  bfs.SearchBFS(grid,visualize);
				break;
		    case "DF" :
		    	DFS dfs = new DFS();
		    	solution = dfs.SearchDFS(grid,visualize);
		    	break;
		    case "ID" :
		    	IDS ids = new IDS();
		    	solution = ids.SearchIDS(grid,visualize);
		    	break;
		    case "UC" :
		    	UC uc = new UC();
		    	solution = uc.SearchUC(grid,visualize);
		    	break;	
		    case "GR1" :
		    	Greedy g = new Greedy();
		    	solution = g.SearchGR(grid,strategy,visualize);
		    	break;	
		    case "GR2" :
		    	Greedy g2 = new Greedy();
		    	solution = g2.SearchGR(grid,strategy,visualize);
		    	break;
		    case "AS1" :
		    	AStar as1 = new AStar();
		    	solution = as1.SearchAS(grid,strategy,visualize);
		    	break;
		    case "AS2" :
		    	AStar as2 = new AStar();
		    	solution = as2.SearchAS(grid,strategy,visualize);
		    	break;
		}
		
		return solution;
	}
	
	
	
	
	public static void printState(Node curr) {
		System.out.println(curr.state.currentcapacity+" "+curr.state.initialCapacity+" "+
	                       curr.state.ships.size()+" "+curr.state.wrecks.size());
	}
	
	public static String GenerateGuardLocation(int n, int m) {
		String guard = "";
		guard += (int)Math.floor(Math.random()*(n)) +","+(int)Math.floor(Math.random()*(m));
		return guard;
	}
	
	public static String GenerateShips(int noOfShips) {
		String ships = "";
		for(int i=0;i<noOfShips;i++) {
			ships += stateSpace.get(i).getX()+","+stateSpace.get(i).getY()+","+(int)Math.floor(Math.random()*(100)+1)+",";
		}
		return ships.substring(0, ships.length()-1);
	}
	
	public static String GenerateStations(int noOfStations) {
		String stations = "";
		for(int i=0;i<noOfStations;i++) {
			stations += stateSpace.get(stateSpace.size()-1-i).getX()+","+stateSpace.get(stateSpace.size()-1-i).getY()+",";
		}
		return stations.substring(0, stations.length()-1);
	}
		
	public static void printShips(ArrayList<Ship>ships) {
		for(Ship ship : ships) {
			System.out.println(ship.location.x +" "+ship.location.y+" "+ship.Passengers+" ");
		}
	}
	
	public static void printStations(ArrayList<Station>stations) {
		for(Station station : stations) {
			System.out.println(station.location.x +" "+station.location.y+" ");
		}
	}
	
	public static void print() {
	  System.out.println("haloo");	
	}

}

