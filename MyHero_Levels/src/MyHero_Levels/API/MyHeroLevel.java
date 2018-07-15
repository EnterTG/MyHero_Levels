package MyHero_Levels.API;

import MyHero_Levels.Core.MyHeroLevelsMain;
import MyHero_Levels.Events.MyHeroExpSource;
import MyHero_Levels.Events.MyHeroPlayerExpGetEvent;
import MyHero_Levels.Events.MyHeroPlayerLevelUpEvent;

public class MyHeroLevel {

	private long PlayerEXP = 0;
	private int Level = 0;
	

	/**
	 *  Convert player level to correct exp
	 */
	public void adaptLevel()
	{
		MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
		for(int i = 0; i < api.getMaxLevel(); i++) if(PlayerEXP < api.getExpFromLevel(i)) {Level = i;break;}
	}
	/**
	 *  Convert player exp to correct level
	 */
	public void adaptExp()
	{
		PlayerEXP = MyHeroLevelsMain.getAPI().getExpFromLevel(Level);
	}
	
	/**
	 *  Check if player should have level up
	 */
	public boolean checkLevel()
	{
		MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
		return (Level < api.getMaxLevel()) && (PlayerEXP >= api.getExpFromLevel(Level));
	}
	
	
	public void playerLevelUp()
	{
		MyHeroPlayerLevelUpEvent plue = new MyHeroPlayerLevelUpEvent(this,Level,Level+1);
		MyHeroLevelsMain.getMainClass().getServer().getPluginManager().callEvent(plue);
		
		if(!plue.isCancelled())
			Level += 1;
	}
	
	public void addExp(long Amount, MyHeroExpSource source)
	{
		MyHeroPlayerExpGetEvent expg = new MyHeroPlayerExpGetEvent(Amount,this, source);
		MyHeroLevelsMain.getMainClass().getServer().getPluginManager().callEvent(expg);
		
		if(!expg.isCancelled())
		{
			if(PlayerEXP + expg.getExp() < Long.MAX_VALUE)
				PlayerEXP += expg.getExp();
			else
				PlayerEXP = Long.MAX_VALUE;
			
			if(checkLevel()) playerLevelUp();
		}
	}
	public void addExp(long Amount)
	{
		addExp(Amount,MyHeroExpSource.Plugin);
	}
	public void subtractExp(long Amount)
	{
		subtractExp(Amount,MyHeroExpSource.Plugin);
	}
	public void subtractExp(long Amount, MyHeroExpSource source)
	{
		MyHeroPlayerExpGetEvent expg = new MyHeroPlayerExpGetEvent(Amount*-1,this, source);
		MyHeroLevelsMain.getMainClass().getServer().getPluginManager().callEvent(expg);
		if(!expg.isCancelled())
		{
			if(PlayerEXP - Amount >= 0)
				PlayerEXP -= Amount;
			else
				clearPlayerExp();
			adaptLevel();
		}
	}
	
	public void addLevel(int i)
	{
		MyHeroPlayerLevelUpEvent plue = new MyHeroPlayerLevelUpEvent(this,Level,Level+i);
		MyHeroLevelsMain.getMainClass().getServer().getPluginManager().callEvent(plue);
		
		if(!plue.isCancelled())
		{
			Level += i;
			adaptExp();
		}
	}
	
	public void subtractLevel(int i)
	{
		MyHeroPlayerLevelUpEvent plue = new MyHeroPlayerLevelUpEvent(this,Level,Level-i);
		MyHeroLevelsMain.getMainClass().getServer().getPluginManager().callEvent(plue);
		
		if(!plue.isCancelled())
		{
			Level -= i;
			adaptExp();
		}
	}
	
	
	public void clearPlayerExp()
	{
		PlayerEXP = 0;
	}
	
	public void setPlayerLevel(int lvl)
	{
		Level = lvl;
	}
	public int getPlayerLevel()
	{
		return Level;
	}
	public void setPlayerExp(long exp)
	{
		PlayerEXP = exp;
	}
	
	public long getPlayerExp()
	{
		return PlayerEXP;
	}
	
	
	
	
}
