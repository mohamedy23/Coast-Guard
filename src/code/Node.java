package code;

public class Node {

	State state;
	Location location;
	Node ParentNode;
	String Operator;
	String extraActions;
	int depth;
	int[] pathCost;
	
	public Node(State state,Location location, Node ParentNode, String Operator, String extraActions, int depth, int[] pathCost) {
		this.state = state;
		this.location = location;
		this.ParentNode = ParentNode;
		this.Operator = Operator;
		this.extraActions = extraActions;
		this.depth = depth;
		this.pathCost = pathCost;
	}

}
