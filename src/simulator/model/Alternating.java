package simulator.model;

import java.util.ArrayList;

import simulator.agent.Agent;
import simulator.agent.TimeServer;

public class Alternating implements World{

	public Alternating(Model model, ArrayList<Agent> _agents,LightController[][] intersection, AnimatorBuilder builder, TimeServer timeServer) {
		int rows = MP.getRows();
		int columns = MP.getColumns();
	
		boolean eastToWest = false;
		
		// Add Horizontal Roads
		for (int i=0; i<rows; i++) {
			
			Road firstRoad = null;
			Road lastRoad = null;

			for (int j=0; j<=columns; j++) {
				Road oldLast = lastRoad;
				lastRoad = new Road();
				lastRoad._nextAcceptor = null;

				if(firstRoad == null){
					firstRoad = lastRoad;
					Source source = new Source(firstRoad,timeServer);
					timeServer.enqueue(1, source);
				}
				else oldLast._nextAcceptor = lastRoad;

				builder.addHorizontalRoad(lastRoad, i, j, eastToWest);
				
			}
			eastToWest = !eastToWest;
			Sink sink = new Sink();
			lastRoad._nextAcceptor = sink;
		}

		// Add Vertical Roads
		for (int j=0; j<columns; j++) {
			boolean southToNorth = false;
			Road firstNSRoad = null;
			Road lastNSRoad = null;

			for (int i=0; i<=rows; i++) {
				Road oldNSRoad = lastNSRoad;
				lastNSRoad = new Road();
				lastNSRoad._nextAcceptor = null;
				if(firstNSRoad == null){
					firstNSRoad = lastNSRoad;
					Source source = new Source(firstNSRoad,timeServer);
					timeServer.enqueue(1, source);
				}
				else oldNSRoad._nextAcceptor = lastNSRoad;
				builder.addVerticalRoad(lastNSRoad, i, j, southToNorth);
			}
			Sink sink = new Sink();
			lastNSRoad._nextAcceptor = sink;
			southToNorth = !southToNorth;
		}
	}

}
