package code;

public class Runner {

	public static void main(String[] args) {
		CoastGuard g = new CoastGuard();
		//System.out.println(CoastGuard.genGrid());
		//String grid = g.genGrid();
		//System.out.println(grid);
		//System.out.println(CoastGuard.solve("7,8;88;4,4;4,2,3,2,0,0,0,2,3,1,5,1,1,2,2,0,1,0,5,5,7,2,4,0,1,5,1,4,2,3,5,4,7,0;6,3,100,1,6,6,3,4,31,0,5,97,6,4,98,4,3,80,3,5,71,1,1,58,1,3,60,3,6,30,5,2,27,7,3,56,4,6,60,2,6,64,7,1,14,5,6,49,0,6,14,3,3,39,2,5,9,6,1,39,6,5,82,6,2,48,6,6,50,7,5,56,2,2,50,7,4,24,0,3,78,7,6,45,5,0,64,6,0,22,3,0,100,2,1,59,5,3,34,2,4,70,4,5,95,0,1,71","AS2",true));
		System.out.println(CoastGuard.solve("7,5;40;2,3;3,6;1,1,10,4,5,90;","AS1",false));
		System.out.println(CoastGuard.solve("7,5;40;2,3;3,6;1,1,10,4,5,90;","AS2",false));
		System.out.println(CoastGuard.solve("7,5;40;2,3;3,6;1,1,10,4,5,90;","UC",false));
		
//		System.out.println(CoastGuard.solve("6,6;52;2,0;2,4,4,0,5,4;2,1,19,4,2,6,5,0,8;","GR1",false));
//		System.out.println(CoastGuard.solve("7,5;40;2,3;3,6;1,1,10,4,5,90;","GR1",false));
//		System.out.println(CoastGuard.solve("8,5;60;4,6;2,7;3,4,37,3,5,93,4,0,40;","GR1",false));
//		System.out.println(CoastGuard.solve("5,7;63;4,2;6,2,6,3;0,0,17,0,2,73,3,0,30;","GR1",false));
//		System.out.println(CoastGuard.solve("5,5;69;3,3;0,0,0,1,1,0;0,3,78,1,2,2,1,3,14,4,4,9;","GR1",false));
//		System.out.println(CoastGuard.solve("7,5;86;0,0;1,3,1,5,4,2;1,1,42,2,5,99,3,5,89;","GR1",false));
//		System.out.println(CoastGuard.solve("6,7;82;1,4;2,3;1,1,58,3,0,58,4,2,72;","GR1",false));
//		System.out.println(CoastGuard.solve("6,6;74;1,1;0,3,1,0,2,0,2,4,4,0,4,2,5,0;0,0,78,3,3,5,4,3,40;","GR1",false));
//		System.out.println(CoastGuard.solve("7,5;100;3,4;2,6,3,5;0,0,4,0,1,8,1,4,77,1,5,1,3,2,94,4,3,46;","GR1",false));
//		System.out.println(CoastGuard.solve("10,6;59;1,7;0,0,2,2,3,0,5,3;1,3,69,3,4,80,4,7,94,4,9,14,5,2,39;","GR1",false));
		//System.out.print(g.capacity+" "+"yarb");
		//State s = new State(3,5);
		//System.out.println(s.x +" "+ s.y);
	}

}
