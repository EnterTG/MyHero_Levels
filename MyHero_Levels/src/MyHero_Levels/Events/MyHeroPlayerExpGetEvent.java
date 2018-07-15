package MyHero_Levels.Events;

import MyHero_Levels.API.MyHeroLevel;
import cn.nukkit.event.Event;

public class MyHeroPlayerExpGetEvent extends Event{

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
	
	
	
}
