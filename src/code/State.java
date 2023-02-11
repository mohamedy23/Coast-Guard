package code;

import java.util.ArrayList;

public class State {

	int currentcapacity;
	int initialCapacity;
	ArrayList<Ship>ships = new ArrayList<>();
	ArrayList<Wreck>wrecks = new ArrayList<>();
	//ArrayList<Node>expandedNodes = new ArrayList<>();
	public State(int currentcapacity, int initialCapacity, ArrayList<Ship>ships,ArrayList<Wreck>wrecks) {
		this.currentcapacity = currentcapacity;
		this.initialCapacity = initialCapacity;
		this.ships = ships;
		this.wrecks = wrecks;
		//this.expandedNodes = expandedNodes;
	}
	public State(State state) {
		this.currentcapacity = state.currentcapacity;
		this.initialCapacity = state.initialCapacity;
		this.ships = state.ships;
	}
	
	
	
}
