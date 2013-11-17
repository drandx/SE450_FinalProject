package simulator.model;

import java.util.ArrayList;

import simulator.agent.Agent;
import simulator.agent.TimeServer;

public class WorldFactory
{
  public static World newInstance(boolean alternating, Model model, ArrayList<Agent> _agents, LightController[][] intersection, AnimatorBuilder builder, TimeServer timeServer)
  {
    if (alternating) new Alternating(model, _agents, intersection, builder, timeServer);
    return new Simple(model, _agents, intersection, builder, timeServer);
  }
}