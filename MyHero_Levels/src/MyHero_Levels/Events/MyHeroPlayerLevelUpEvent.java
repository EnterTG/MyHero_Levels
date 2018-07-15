package MyHero_Levels.Events;

import MyHero_Levels.API.MyHeroLevel;
import cn.nukkit.event.Event;

public class MyHeroPlayerLevelUpEvent extends Event{

	public MyHeroLevel getData() {
		return data;
	}
	public void setData(MyHeroLevel data) {
		this.data = data;
	}
	public int getLevelActual() {
		return LevelActual;
	}
	public void setLevelActual(int levelActual) {
		LevelActual = levelActual;
	}
	public int getLevelConquered() {
		return LevelConquered;
	}
	public void setLevelConquered(int levelConquered) {
		LevelConquered = levelConquered;
	}
	public MyHeroPlayerLevelUpEvent(MyHeroLevel data, int levelActual, int levelConquered) {
		super();
		this.data = data;
		LevelActual = levelActual;
		LevelConquered = levelConquered;
	}
	private MyHeroLevel data;
	private int LevelActual , LevelConquered;
	
	
}
