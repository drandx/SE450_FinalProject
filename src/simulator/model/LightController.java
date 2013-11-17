package simulator.model;

import simulator.agent.Agent;
import simulator.model.Light.lightState;

public class LightController implements Agent{
	public enum lightControlState {GreenNS_RedEW, YellowNS_RedEW, RedNS_GreenEW,  RedNS_YellowEW};
	
	Light[] _lights;
	private static int NorthSouth = 0;
	private static int EastWest = 1;

	private double greenDurationNS = Math.random() * (MP.maxGreenLightTime - MP.minGreenLightTime) + MP.minGreenLightTime;
	private double yellowDurationNS = Math.random() * (MP.maxYellowLightTime - MP.minYellowLightTime) + MP.minYellowLightTime;
	private double greenDurationEW = Math.random() * (MP.maxGreenLightTime - MP.minGreenLightTime) + MP.minGreenLightTime;
	private double yellowDurationEW = Math.random() * (MP.maxYellowLightTime - MP.minYellowLightTime) + MP.minYellowLightTime;

	private lightControlState _currentState;
	private double _prevTime;

	public LightController(Light nsLight, Light ewLight) {
		this._currentState = lightControlState.GreenNS_RedEW;
		this._lights = new Light[2];

		nsLight.set_state(lightState.GREEN);
		ewLight.set_state(lightState.RED);

		this._lights[NorthSouth] = nsLight;
		this._lights[EastWest] = ewLight;

		this._prevTime = 0;
	}

	public Light getNSLight(){
		return this._lights[NorthSouth];
	}

	public Light getEWLight(){
		return this._lights[EastWest];
	}

	
	public void run(double time){
		if(_currentState == lightControlState.GreenNS_RedEW){
			if(time-this._prevTime  > greenDurationNS){
				_lights[NorthSouth].set_state(lightState.YELLOW);
				_lights[EastWest].set_state(lightState.RED);
				_currentState = lightControlState.YellowNS_RedEW;
				this._prevTime = time;
			}
		}
		else if(_currentState == lightControlState.YellowNS_RedEW){
			if(time-this._prevTime > yellowDurationNS){
				_lights[NorthSouth].set_state(lightState.RED);
				_lights[EastWest].set_state(lightState.GREEN);
				_currentState = lightControlState.RedNS_GreenEW;
				this._prevTime = time;
			}
		}
		else if(_currentState == lightControlState.RedNS_GreenEW){
			if(time-this._prevTime > greenDurationEW){
				_lights[NorthSouth].set_state(lightState.RED);
				_lights[EastWest].set_state(lightState.YELLOW);
				_currentState = lightControlState.RedNS_YellowEW;
				this._prevTime = time;
			}
		}
		else if(_currentState == lightControlState.RedNS_YellowEW){
			if(time-this._prevTime > yellowDurationEW){
				_lights[NorthSouth].set_state(lightState.GREEN);
				_lights[EastWest].set_state(lightState.RED);
				_currentState = lightControlState.GreenNS_RedEW;
				this._prevTime = time;
			}
		}		
	}

}
