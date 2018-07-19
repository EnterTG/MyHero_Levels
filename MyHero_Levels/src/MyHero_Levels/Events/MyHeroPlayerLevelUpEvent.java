package MyHero_Levels.Events;

import MyHero_Levels.API.MyHeroLevel;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class MyHeroPlayerLevelUpEvent extends Event implements Cancellable{

	
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() 
	{
		return handlers;
	}
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
	private boolean isCancelled;
	
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}
	 
	@Override
	public void setCancelled(boolean arg0) {
		this.isCancelled = arg0;
	}
	
}
