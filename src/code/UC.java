package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

class comparableNode implements Comparator<Node> {

    @Override
	public int compare(Node n1, Node n2) {
		if(n1.pathCost[0] != n2.pathCost[0])
			return Integer.compare(n1.pathCost[0],n2.pathCost[0]);
		else
			return Integer.compare(n1.pathCost[1],n2.pathCost[1]);
	}
}


public class UC extends Search{

	public static String SearchUC(String grid,boolean visualize) {
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
		
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(new comparableNode());
		Node initial = new Node(new State(currentCapacity,initialCapacity,ships,wrecks),new Location(xGuard,yGuard),null,"","",0,new int[]{0,0}) ;
		nodes.add(initial);
		ArrayList<Node>expandedNodes = new ArrayList<>();
		Node output = initial;
		
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
				if(GoalTest(current)) {
					output = current;
					break;
				}
			}
			 if(isWreck(currLocation,current.state.wrecks) != null) {
				retrieve(currLocation,current,current.state.wrecks);
				UpdateWrecks(current);
				UpdateShips(current);
				if(GoalTest(current)) {
					output = current;
					break;
				}
			}
		
			 if(isStation(currLocation,stations) != null && current.state.currentcapacity != current.state.initialCapacity) {
				drop(current);
				UpdateWrecks(current);
				UpdateShips(current);
				//System.out.println(true);
				if(GoalTest(current)) {
					output = current;
					break;
				}
			}
			
			
		
			expand(current,expandedNodes,nodes,n,m, current.state.ships,stations);
			
		}
		
		if(visualize)visualize(findPath(output,stations),new State(currentCapacity,initialCapacity,ships,wrecks),stations,new Location(xGuard,yGuard));
		
		String plan = ListToString(findPath(output,stations));
		String deaths = output.pathCost[0]+"";
		String retrieved = (InitialBoxes - output.pathCost[1])+"";
		String noOfNodes = expandedNodes.size()+"";
		
		return plan+";"+deaths+";"+retrieved+";"+noOfNodes;
		
		
	}
	
	public static void expand(Node current, ArrayList<Node>expandedNodes, PriorityQueue<Node>nodes, int n,int m, ArrayList<Ship>ships,
			                  ArrayList<Station>stations) {
		Location currLocation = current.location;
		Location upLocation = new Location(currLocation.x-1,currLocation.y);
		Location downLocation = new Location(currLocation.x+1,currLocation.y);
		Location rightLocation = new Location(currLocation.x,currLocation.y+1);
		Location leftLocation = new Location(currLocation.x,currLocation.y-1);
		if(isValid(upLocation,n,m)  && current.Operator != "down"  ) {
			Node node = new Node(copyState(current.state),upLocation,current,"up","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1]});
			UpdateWrecks(node);
			UpdateShips(node);
			nodes.add(node);
		}
		if(isValid(downLocation,n,m)  && current.Operator != "up") {
			Node node = new Node(copyState(current.state),downLocation,current,"down","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1]});
			UpdateWrecks(node);
			UpdateShips(node);
			nodes.add(node);
		}
		if(isValid(rightLocation,n,m)  && current.Operator != "left") {
			Node node = new Node(copyState(current.state),rightLocation,current,"right","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1]});
			UpdateWrecks(node);
			UpdateShips(node);
			nodes.add(node);
		}
		if(isValid(leftLocation,n,m)  && current.Operator != "right") {
			Node node = new Node(copyState(current.state),leftLocation,current,"left","",current.depth+1,
					             new int[] {current.pathCost[0],current.pathCost[1]});
			UpdateWrecks(node);
			UpdateShips(node);
			nodes.add(node);
		}
	}
	
}
