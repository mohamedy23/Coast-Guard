package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

class comparableNode2 implements Comparator<Node> {

    @Override
	public int compare(Node n1, Node n2) {
			return Integer.compare(n1.pathCost[2],n2.pathCost[2]);
	}
}


public class Greedy extends Search {

	public static String SearchGR(String grid,String strategy,boolean visualize) {
		String [] info = grid.split(";");
		String[] dimensions = info[0].split(",");
		int m = Integer.parseInt(dimensions[0]);
		int n = Integer.parseInt(dimensions[1]);
		//System.out.println("n ="+n+", m ="+m);
		int initialCapacity = Integer.parseInt(info[1]);
		int currentCapacity = initialCapacity;
		String[] guardLocation = info[2].split(",");
		int xGuard = Integer.parseInt(guardLocation[0]);
		int yGuard = Integer.parseInt(guardLocation[1]);
		String[] stationsInfo = info[3].split(",");
		String[] shipsInfo = info[4].split(",");
		
		ArrayList<Ship>ships = new ArrayList<>();
		ArrayList<Station>stations = new ArrayList<>();
		ArrayList<Wreck>wrecks = new ArrayList<>();
		for(int i=0;i<shipsInfo.length;i+=3) {
			int x = Integer.parseInt(shipsInfo[i]);
			int y = Integer.parseInt(shipsInfo[i+1]);
			int p = Integer.parseInt(shipsInfo[i+2]);
			ships.add(new Ship(new Location(x,y),p));
		}
		int InitialBoxes = ships.size();
		for(int i=0;i<stationsInfo.length;i+=2) {
			int x = Integer.parseInt(stationsInfo[i]);
			int y = Integer.parseInt(stationsInfo[i+1]);
			stations.add(new Station(new Location(x,y)));
		}
		
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(new comparableNode2());
		Node initial = new Node(new State(currentCapacity,initialCapacity,ships,wrecks),new Location(xGuard,yGuard),null,"","",0,new int[]{0,0,0}) ;
		nodes.add(initial);
		ArrayList<Node>expandedNodes = new ArrayList<>();
		Node output = initial;
		int exnodes = 0;
		while(!nodes.isEmpty()) {
			Node current = nodes.poll();

			if(GoalTest(current)) {
				output = current;
				break;
			}
			if(isExpanded(current,expandedNodes)) {
				continue;
			}
			
			expandedNodes.add(current);
			Location currLocation = current.location;
			if(isShip(currLocation, current.state.ships) != null && current.state.currentcapacity != 0) {
		        pickUp(currLocation,current,current.state.ships);		
		        UpdateWrecks(current);
		        UpdateShips(current);
		        if(!nodes.isEmpty())nodes.clear();
		        if(!expandedNodes.isEmpty()) {
		        	exnodes += expandedNodes.size();
		        	expandedNodes.clear();
		        }
				if(GoalTest(current)) {
					output = current;
					break;
				}
			}
			 if(isWreck(currLocation,current.state.wrecks) != null) {
				retrieve(currLocation,current,current.state.wrecks);
				UpdateWrecks(current);
				UpdateShips(current);
				if(!nodes.isEmpty())nodes.clear();
				if(!expandedNodes.isEmpty()) {
					exnodes += expandedNodes.size();
					expandedNodes.clear();
				}
				if(GoalTest(current)) {
					output = current;
					break;
				}
			}
		
			 if(isStation(currLocation,stations) != null && current.state.currentcapacity != current.state.initialCapacity) {
				drop(current);
				UpdateWrecks(current);
				UpdateShips(current);
				if(!nodes.isEmpty())nodes.clear();
				if(!expandedNodes.isEmpty()) {
					exnodes += expandedNodes.size();
					expandedNodes.clear();
				}
				//System.out.println(true);
				if(GoalTest(current)) {
					output = current;
					break;
				}
			}
			
			
		
			if(strategy.equals("GR1"))expand(current,expandedNodes,nodes,n,m, current.state.ships,stations);
			else expand2(current,expandedNodes,nodes,n,m, current.state.ships,stations);
			
		}
		
		if(visualize)visualize(findPath(output,stations),new State(currentCapacity,initialCapacity,ships,wrecks),stations,new Location(xGuard,yGuard));
		
		String plan = ListToString(findPath(output,stations));
		String deaths = output.pathCost[0]+"";
		String retrieved = (InitialBoxes - output.pathCost[1])+"";
		String noOfNodes = exnodes+"";
		
		return plan+";"+deaths+";"+retrieved+";"+noOfNodes;
		
		
	}
	
	public static void expand(Node current, ArrayList<Node>expandedNodes, PriorityQueue<Node>nodes, int n,int m, ArrayList<Ship>ships,
			                  ArrayList<Station>stations) {
		Location currLocation = current.location;
		Location upLocation = new Location(currLocation.x-1,currLocation.y);
		Location downLocation = new Location(currLocation.x+1,currLocation.y);
		Location rightLocation = new Location(currLocation.x,currLocation.y+1);
		Location leftLocation = new Location(currLocation.x,currLocation.y-1);
		//if(current.state.currentcapacity == 0 )
		if(isValid(upLocation,n,m)   ) {
			Node node = new Node(copyState(current.state),upLocation,current,"up","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic1(node,stations,nodes);
			nodes.add(node);
		}
		if(isValid(downLocation,n,m)  ) {
			Node node = new Node(copyState(current.state),downLocation,current,"down","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic1(node,stations,nodes);
			nodes.add(node);
		}
		if(isValid(rightLocation,n,m)  ) {
			Node node = new Node(copyState(current.state),rightLocation,current,"right","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic1(node,stations,nodes);
			nodes.add(node);
		}
		if(isValid(leftLocation,n,m) ) {
			Node node = new Node(copyState(current.state),leftLocation,current,"left","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic1(node,stations,nodes);
			nodes.add(node);
		}
	}

   
       public static void expand2(Node current, ArrayList<Node>expandedNodes, PriorityQueue<Node>nodes, int n,int m, ArrayList<Ship>ships,
            ArrayList<Station>stations) {
		Location currLocation = current.location;
		Location upLocation = new Location(currLocation.x-1,currLocation.y);
		Location downLocation = new Location(currLocation.x+1,currLocation.y);
		Location rightLocation = new Location(currLocation.x,currLocation.y+1);
		Location leftLocation = new Location(currLocation.x,currLocation.y-1);
		//if(current.state.currentcapacity == 0 )
		if(isValid(upLocation,n,m)   ) {
			Node node = new Node(copyState(current.state),upLocation,current,"up","",current.depth+1,
				             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic2(node,stations);
			nodes.add(node);
		}
		if(isValid(downLocation,n,m)  ) {
			Node node = new Node(copyState(current.state),downLocation,current,"down","",current.depth+1,
				             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic2(node,stations);
			nodes.add(node);
		}
		if(isValid(rightLocation,n,m)  ) {
			Node node = new Node(copyState(current.state),rightLocation,current,"right","",current.depth+1,
				             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic2(node,stations);
			nodes.add(node);
		}
		if(isValid(leftLocation,n,m) ) {
			Node node = new Node(copyState(current.state),leftLocation,current,"left","",current.depth+1,
				             new int[] {current.pathCost[0],current.pathCost[1],0});
			UpdateWrecks(node);
			UpdateShips(node);
			node.pathCost[2] = heuristic2(node,stations);
			nodes.add(node);
		}
}



}
