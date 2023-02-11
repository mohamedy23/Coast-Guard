package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class Search {

	public static int min = 5;
	public static int max = 15;
	static ArrayList<Location>stateSpace = new ArrayList<>();
	int capacity;

	
	public static boolean toBeExpanded(Node n, Queue<Node>nodes) {
		for(Node node : nodes) {
			if(equalNodes(n,node))
				return true;
		}
		return false;
	}
	
	public static boolean isExpanded(Node n, ArrayList<Node>nodes) {
		for(Node node : nodes) {
			if(equalNodes(n,node))
				return true;
		}
		return false;
	}
	
	public static boolean equalNodes(Node n1, Node n2) {
		if(equalStates(n1.state,n2.state) && n1.location.x == n2.location.x 
		   && n1.location.y == n2.location.y && n1.pathCost[0] == n2.pathCost[0]
		   && n1.pathCost[1] == n2.pathCost[1])
			return true;
		return false;
	}

	public static boolean equalStates(State s1, State s2) {
		if(equalShips(s1.ships,s2.ships) && equalWrecks(s1.wrecks,s2.wrecks) && s1.currentcapacity == s2.currentcapacity)
			return true;
		return false;
	}
	public static boolean equalShips(ArrayList<Ship>s1, ArrayList<Ship>s2) {
		//Collections.sort(s1, new CustomComparator());
		//Collections.sort(s2, new CustomComparator());
		if(s1.size() == s2.size()) {
			for(int i=0;i<s1.size();i++) {
				Ship a = s1.get(i);
				Ship b = s2.get(i);
				if((a.location.x != b.location.x || a.location.y != b.location.y) || a.Passengers != b.Passengers)
					return false;
			}
			return true;
		}
		return false;
	}
	public static boolean equalWrecks(ArrayList<Wreck>w1, ArrayList<Wreck>w2) {
		//Collections.sort(s1, new CustomComparator());
		//Collections.sort(s2, new CustomComparator());
		if(w1.size() == w2.size()) {
			for(int i=0;i<w1.size();i++) {
				Wreck a = w1.get(i);
				Wreck b = w2.get(i);
				if((a.location.x != b.location.x || a.location.y != b.location.y) || a.counter != b.counter)
					return false;
			}
			return true;
		}
		return false;
	}
	
	
	
	public static String ListToString(ArrayList<String>arr) {
		String path = "";
		for(String s : arr) {
			path+= s+",";
		}
		return path.substring(0,path.length()-1);
	}
    
	public static ArrayList<Node> pathNodes(Node curr){
		ArrayList<Node>nodes = new ArrayList<>();
		while(curr.ParentNode != null) {
			nodes.add(curr);
			Node parent = curr.ParentNode;
			curr = parent;
		}
		nodes.add(curr);
		return nodes;
	}
	

	
	public static void printNode(Node curr) {
		System.out.println(curr.location.x+","+curr.location.y+" "+curr.Operator+" "+curr.extraActions+" "+curr.state.currentcapacity
				           +" "+livingPassengers(curr.state.ships) +" "+curr.pathCost[0]);
	}
	
	public static void drop(Node currNode) {
		currNode.state.currentcapacity = currNode.state.initialCapacity;
		currNode.extraActions = "drop";
	}
	
	public static void retrieve(Location currLocation,Node currNode, ArrayList<Wreck>wrecks) {
		Wreck wreck = isWreck(currLocation, wrecks);
		wrecks.remove(wreck);
		currNode.state.wrecks = wrecks;
		if(currNode.extraActions == "")
		    currNode.extraActions = "retrieve";
		else
			currNode.extraActions = currNode.extraActions +",retrieve";
	}
	
	public static Station isStation(Location s, ArrayList<Station>stations) {
		for(Station station : stations) {
			if(station.location.x == s.x && station.location.y == s.y)
				return station;
		}
		return null;
	}
	
	public static void pickUp(Location currLocation, Node currNode, ArrayList<Ship>ships) {
		Ship ship = isShip(currLocation, ships);
		int min = Math.min(ship.Passengers, currNode.state.currentcapacity);
		ship.Passengers -= min;
		currNode.state.currentcapacity -= min;
		//System.out.println(ship.Passengers);
		if(ship.Passengers == 0) {
			Wreck wreck = new Wreck(new Location(ship.location.x,ship.location.y),20);
			currNode.state.wrecks.add(wreck);
			ships.remove(ship);
		}
			
		currNode.state.ships = ships;
		currNode.extraActions = "pickup";
	}
	
	public static void expand(Node current, ArrayList<Node>expandedNodes, Queue<Node>nodes, int n,int m, ArrayList<Ship>ships,
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
	
	public static ArrayList<Wreck> copyListOfWrecks(ArrayList<Wreck>wrecks){
		ArrayList<Wreck> newWrecks = new ArrayList<>();
		for(Wreck oldWreck : wrecks) {
			int x = oldWreck.location.x;
			int y = oldWreck.location.y;
			int c = oldWreck.counter;
			Wreck newWreck = new Wreck(new Location(x,y),c);
			newWrecks.add(newWreck);
		}
		return newWrecks;
	}
	
	public static ArrayList<Ship> copyList(ArrayList<Ship>ships){
		ArrayList<Ship> newShips = new ArrayList<>();
		for(Ship oldShip : ships) {
			int x = oldShip.location.x;
			int y = oldShip.location.y;
			int p = oldShip.Passengers;
			Ship newShip = new Ship(new Location(x,y),p);
			newShips.add(newShip);
		}
		return newShips;
	}
	
	public static State copyState(State oldState) {
		int cp = oldState.currentcapacity;
		int ip = oldState.initialCapacity;
		ArrayList<Ship>newShips = new ArrayList<>();
		ArrayList<Wreck>newWrecks = new ArrayList<>();
		newShips = copyList(oldState.ships);
		newWrecks = copyListOfWrecks(oldState.wrecks);
		State newState = new State(cp,ip,newShips,newWrecks);
		return newState;
	}
	
 	public static ArrayList<String> findPath(Node curr,ArrayList<Station>stations) {
		String path = "";
		ArrayList<String>out = new ArrayList<>();
		//path += curr.Operator+" ";
		//System.out.println(curr.location.x+" "+curr.location.y);
		while(curr.ParentNode != null) {
			//printNode(curr);
			if(curr.extraActions != "")
				out.add(curr.extraActions);
			out.add(curr.Operator);
			path += curr.extraActions+" "+curr.Operator +" ";
			Node parent = curr.ParentNode;
			curr = parent;
		}
		Collections.reverse(out);
		return out;
	}
	
 	public static int livingPassengers(ArrayList<Ship>ships) {
 		int count = 0;
 		for(Ship ship : ships) {
 			count += ship.Passengers;
 		}
 		return count;
 	}
 	
	public static void printNodes(ArrayList<Node>nodes) {
		for(Node node : nodes) {
			System.out.print(node.location.x+","+node.location.y+" ");
		}
	}
	
	public static boolean isVisited(ArrayList<Node>nodes,Node n) {
		for(Node node : nodes) {
			if(node.location.x ==n.location.x && node.location.y == n.location.y && node.Operator == n.Operator)
				return true;
		}
		return false;
	}
	
	public static boolean isValid(Location s, int n, int m) {
		if(s.x <= n-1 && s.x >= 0 && s.y <= m-1 && s.y >= 0)
			return true;
		return false;
	}
	
	public static void UpdateShips(Node curr) {
		ArrayList<Ship>ships = curr.state.ships;
		ArrayList<Ship>destroyed = new ArrayList<>();
		for(Ship ship : ships) {
			ship.Passengers -= 1;
			curr.pathCost[0] += 1;
			if(ship.Passengers == 0) {
				destroyed.add(ship);
				Wreck wreck = new Wreck(new Location(ship.location.x,ship.location.y),20);
				curr.state.wrecks.add(wreck);
			}
		}
		ships.removeAll(destroyed);
	}
	
	public static void UpdateWrecks(Node curr) {
		ArrayList<Wreck>wrecks = curr.state.wrecks;
		ArrayList<Wreck>destroyed = new ArrayList<>();
		for(Wreck wreck : wrecks) {
			wreck.counter -= 1;
			if(wreck.counter == 1) {
				destroyed.add(wreck);
				curr.pathCost[1] += 1;
			}
		}
		wrecks.removeAll(destroyed);
		//curr.state.wrecks = wrecks;
	}
	
	public static Ship isShip(Location s, ArrayList<Ship>ships) {
		for(Ship ship : ships) {
			if(ship.location.x == s.x && ship.location.y == s.y)
				return ship;
		}
		return null;
	}
	
	public static Wreck isWreck(Location s, ArrayList<Wreck>wrecks) {
		for(Wreck wreck : wrecks) {
			if(wreck.location.x == s.x && wreck.location.y == s.y) {
				return wreck;
			}
		}
		return null;
	}
	
	public static boolean GoalTest(Node curr) {
		ArrayList<Ship>ships = curr.state.ships;
		ArrayList<Wreck>wrecks = curr.state.wrecks;
		int currentCapacity = curr.state.currentcapacity;
		int initialCapacity = curr.state.initialCapacity;
		if(ships.isEmpty() && currentCapacity == initialCapacity && wrecks.isEmpty())
			return true;
		return false;
	}
	
	public static int distance(Location a, Location b) {
		int h = Math.abs(a.y - b.y);
		int v = Math.abs(a.x - b.x);
		return h+v;
	}
	
	public static Ship nearestShip(Node curr) {
		Ship nearest = null;
		int min = Integer.MAX_VALUE;
		for(Ship ship : curr.state.ships) {
			int distance = distance(ship.location,curr.location);
			if(distance < min ) {
				min = distance;
				nearest =ship;
			}
		}
		return nearest;
	}
	
	public static Wreck nearestWreck(Node curr) {
		Wreck nearest = null;
		int min = Integer.MAX_VALUE;
		for(Wreck wreck : curr.state.wrecks) {
			int distance = distance(wreck.location,curr.location);
			if(distance < min) {
				min = distance;
				nearest = wreck;
			}
		}
		return nearest;
	}

	public static Station nearestStation(Node curr, ArrayList<Station>stations) {
		Station nearest = null;
		int min = Integer.MAX_VALUE;
		for(Station s: stations) {
			int distance = distance(s.location,curr.location);
			if(distance < min) {
				min = distance;
				nearest = s;
			}
		}
		return nearest;
	}
	
	 public static Ship heaviestShip(Node curr) {
	    	int max = Integer.MIN_VALUE;
	    	Ship heaviest = null;
	    	for(Ship ship : curr.state.ships) {
	    		int remaining = ship.Passengers - distance(ship.location,curr.location);
	    		if(remaining > max) {
	    			max = remaining;
	    			heaviest = ship;
	    		}
	    	}
	    	return heaviest;
	    }

	 public static void visualize(ArrayList<String>path,State state,ArrayList<Station>stations,Location guard) {
			for(String action : path) {
				if(action.contains(",")) {
					String[] actions = action.split(",");
					System.out.println(actions[0]);
					update(actions[0],path,state,guard);
					printGrid(state,stations,guard);
					System.out.println(actions[1]);
					update(actions[1],path,state,guard);
					printGrid(state,stations,guard);
				}
				else {
				System.out.println(action);
				update(action,path,state,guard);
				printGrid(state,stations,guard);
			}
		  }
		}
		public static void update(String action,ArrayList<String>path,State state,Location guard) {
			if(action.equals("pickup")) {
				Ship s = null;
				for(Ship ship : state.ships) {
					if(ship.location.x == guard.x && ship.location.y == guard.y) {
						s = ship;
						break;
					}
				}
				int min = Math.min(s.Passengers, state.currentcapacity);
				s.Passengers -= min;
				state.currentcapacity -= min;
				if(s.Passengers == 0) {
					Wreck wreck = new Wreck(new Location(s.location.x,s.location.y),1);
					state.wrecks.add(wreck);
					state.ships.remove(s);
				}
			}
			if(action.equals("retrieve")) {
				Wreck w = null;
				for(Wreck wreck : state.wrecks) {
					if(wreck.location.x == guard.x && wreck.location.y == guard.y) {
						w = wreck;
						break;
					}
				}
				state.wrecks.remove(w);
			}
			if(action.equals("drop")) {
				state.currentcapacity = state.initialCapacity;
			}
			ArrayList<Wreck>destroyedWrecks = new ArrayList<>();
			for(Wreck wreck : state.wrecks) {
				if(wreck.location.x != guard.x || wreck.location.y != guard.y) {
					wreck.counter += 1;
					if(wreck.counter == 20) {
						destroyedWrecks.add(wreck);
					}
				}
			}
			state.wrecks.removeAll(destroyedWrecks);
			ArrayList<Ship>destroyedShips = new ArrayList<>();
			for(Ship ship : state.ships) {
				ship.Passengers -= 1;
				if(ship.Passengers <= 0) {
					destroyedShips.add(ship);
					Wreck wreck = new Wreck(new Location(ship.location.x,ship.location.y),1);
					state.wrecks.add(wreck);
				}
			}
			state.ships.removeAll(destroyedShips);
			if(action.equals("up"))
				guard.x -=1;
			if(action.equals("down"))
				guard.x += 1;
			if(action.equals("left"))
				guard.y -= 1;
			if(action.equals("right"))
				guard.y += 1;
		}
		
		public static void printGrid(State state,ArrayList<Station>stations,Location guard) {
			System.out.println("Guard ----> "+guard.x + ","+guard.y +","+state.currentcapacity);
		    printShips(state.ships);
		    printWrecks(state.wrecks);
		    printStations(stations);
		    System.out.println("--------------------");
		}
		
		
		public static void printShips(ArrayList<Ship>ships) {
			System.out.print("Ships ----> ");
			for(Ship ship : ships) {
				System.out.print(ship.location.x+","+ship.location.y+","+ship.Passengers+" ");
			}
			System.out.println();
		}
		public static void printWrecks(ArrayList<Wreck>wrecks) {
			System.out.print("Wrecks ----> ");
			for(Wreck wreck : wrecks) {
				System.out.print(wreck.location.x+","+wreck.location.y+","+wreck.counter+" ");
			}
			System.out.println();
		}
	    public static void printStations(ArrayList<Station>stations) {
	    	System.out.print("Stations ----> ");
	    	for(Station station : stations) {
	    		System.out.print(station.location.x+","+station.location.y+" ");
	    	}
	    	System.out.println();
	    }

	    

	    public static int heuristic1(Node curr, ArrayList<Station>stations, PriorityQueue<Node>nodes) {
	    	int h = 0;
	    	Ship nearestShip = nearestShip(curr);
	    	Station nearestStation = nearestStation(curr,stations);
	    	Wreck nearestWreck = nearestWreck(curr);
	    	if(curr.state.currentcapacity == 0 || ((nearestShip == null) && curr.state.currentcapacity != curr.state.initialCapacity)) {
	    		 h = Math.min(livingPassengers(curr.state.ships),distance(nearestStation.location,curr.location));
	    	}
	    	else {
		    	if(nearestShip != null) { 
		    		    h = Math.min(nearestShip.Passengers, distance(nearestShip.location,curr.location));
		    	}
		    	else if(nearestWreck != null) {
					if(nearestWreck.counter > distance(nearestWreck.location,curr.location))
						h = 0;
					else
						h = 1;
				}
		    	else
		    		h = 0;
	    	}
	    	return h;
	    }

	    public static int heuristic2(Node curr, ArrayList<Station>stations) {
	    	int h = 0;
	    	Ship heaviestShip = heaviestShip(curr);
	    	Station nearestStation = nearestStation(curr,stations);
	    	Wreck nearestWreck = nearestWreck(curr);
	    	if(curr.state.currentcapacity == 0 || ((heaviestShip == null) && curr.state.currentcapacity != curr.state.initialCapacity)) {
	    		h = Math.min(livingPassengers(curr.state.ships),distance(nearestStation.location,curr.location));
	    	}
	    	else {
		    	if(heaviestShip != null) { 
		    		    h = Math.min(heaviestShip.Passengers,distance(heaviestShip.location,curr.location));
		    	}
		    	else if(nearestWreck != null) {
		    		if(nearestWreck.counter > distance(nearestWreck.location,curr.location))
						h = 0;
					else
						h = 1;
				}
		    	else
		    		h = 0;
	    	}
	    	return h;
	    }

	  



}
