package simulator.model;

import simulator.agent.Agent;
import simulator.model.Light.lightState;
import simulator.util.MP;

public class LightController implements Agent{
	public enum lightControlState {GreenNS_RedEW, YellowNS_RedEW, RedNS_GreenEW,  RedNS_YellowEW};
	Light[] _lights;
	private double _prevTime;
	private static int NorthSouth = 0;
	private static int EastWest = 1;
	
	public enum lightDurations{
		 greenDurationNS(55.0),
		 yellowDurationNS(5.0),
		 greenDurationEW(25.0),
		 yellowDurationEW(5.0);
		
		private final double _time;
		
		lightDurations(double time){
			this._time = time;
		}
		
		public double _time() { 
	        return _time; 
	    }
	}
	
	private lightControlState _currentState;
	
	public LightController(Light nsLight, Light ewLight) {
		this._currentState = lightControlState.GreenNS_RedEW;
		this._lights = new Light[2];
		
		nsLight.set_state(lightState.GREEN);
		ewLight.set_state(lightState.RED);
		
		this._lights[NorthSouth] = nsLight;
		this._lights[EastWest] = ewLight;
		
		this._prevTime = 0;
	}
	
	public void run(double time){
		if(_currentState == lightControlState.GreenNS_RedEW){
			if(time-this._prevTime  > MP.greenDurationNS){
				_lights[NorthSouth].set_state(lightState.YELLOW);
				_lights[EastWest].set_state(lightState.RED);
				_currentState = lightControlState.YellowNS_RedEW;
				this._prevTime = time;
			}
		}
		else if(_currentState == lightControlState.YellowNS_RedEW){
			if(time-this._prevTime > MP.yellowDurationNS){
				_lights[NorthSouth].set_state(lightState.RED);
				_lights[EastWest].set_state(lightState.GREEN);
				_currentState = lightControlState.RedNS_GreenEW;
				this._prevTime = time;
			}
		}
		else if(_currentState == lightControlState.RedNS_GreenEW){
			if(time-this._prevTime > MP.greenDurationEW){
				_lights[NorthSouth].set_state(lightState.RED);
				_lights[EastWest].set_state(lightState.YELLOW);
				_currentState = lightControlState.RedNS_YellowEW;
				this._prevTime = time;
			}
		}
		else if(_currentState == lightControlState.RedNS_YellowEW){
			if(time-this._prevTime > MP.yellowDurationEW){
				_lights[NorthSouth].set_state(lightState.GREEN);
				_lights[EastWest].set_state(lightState.RED);
				_currentState = lightControlState.GreenNS_RedEW;
				this._prevTime = time;
			}
		}		
	}

}
