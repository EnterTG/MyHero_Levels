package MyHero_Levels.Events;

import MyHero_Levels.API.MyHeroLevel;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class MyHeroPlayerExpGetEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlers() 
	{
		return handlers;
	}
	public long getExp() {
		return Exp;
	}
	public void setExp(long exp) {
		Exp = exp;
	}
	public MyHeroLevel getData() {
		return data;
	}
	public void setData(MyHeroLevel data) {
		this.data = data;
	}
	public MyHeroExpSource getSource() {
		return source;
	}
	public void setSource(MyHeroExpSource source) {
		this.source = source;
	}
	
	public MyHeroPlayerExpGetEvent(long exp, MyHeroLevel data, MyHeroExpSource source) {
		super();
		Exp = exp;
		this.data = data;
		this.source = source;
	}
	
	private long Exp;
	private MyHeroLevel data;
	private MyHeroExpSource source;
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
