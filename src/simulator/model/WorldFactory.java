package simulator.model;

import java.util.ArrayList;

import simulator.agent.Agent;

public class WorldFactory
{
  public static World newInstance(boolean alternating, Model model, ArrayList<Agent> _agents, LightController[][] intersection, AnimatorBuilder builder)
  {
    if (alternating) return new Alternating(model, _agents, intersection, builder);
    return new Simple(model, _agents, intersection, builder);
  }
}