package MyHero_Levels.API;

import MyHero_Core.Managers.LangManager;
import MyHero_Levels.Core.MyHeroLevelsMain;
import MyHero_Levels.Events.MyHeroExpSource;
import MyHero_Levels.Events.MyHeroPlayerExpGetEvent;
import MyHero_Levels.Events.MyHeroPlayerLevelUpEvent;
import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.network.protocol.UpdateAttributesPacket;

public class MyHeroLevel {

	private Player player;
	private long PlayerEXP = 0;
	private int Level = 1;
	
	public MyHeroLevel(Player p)
	{
		player = p;
	}
	
	/**
	 *  Convert player level to correct exp
	 */
	public void adaptLevel()
	{
		MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
		for(int i = 1; i <= api.getMaxLevel(); i++) if(PlayerEXP < api.getExpFromLevel(i)) {Level = i;break;}
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
	
	public void updatePlayerView()
	{
		MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
		/*
		LangManager.Log("Level: " +Level);
		LangManager.Log("Exp: " +PlayerEXP);
		LangManager.Log("%%" + ( (float) ( ( ( PlayerEXP - api.getExpFromLevel(Level-1) ) * 100 ) / ( api.getExpFromLevel(Level) - api.getExpFromLevel(Level-1))) /100 ) );*/
		player.sendMessage("Test");
		UpdateAttributesPacket pk = new UpdateAttributesPacket();
		pk.entityId = player.getId();
		pk.entries = new Attribute[]{
				Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(Level),
				Attribute.getAttribute(Attribute.EXPERIENCE).setValue(
						
						( (float) ( ( ( PlayerEXP - api.getExpFromLevel(Level-1) ) * 100 ) / ( api.getExpFromLevel(Level) - api.getExpFromLevel(Level-1))) /100 ) 
						
						)
		};
		player.sendExperienceLevel(Level);
		player.setExperience( (int) (( ( PlayerEXP - api.getExpFromLevel(Level-1) ) * 100 ) / ( api.getExpFromLevel(Level) - api.getExpFromLevel(Level-1))));
		
		player.dataPacket(pk);
	}
	
	
	public void addExp(long Amount, MyHeroExpSource source)
	{
		MyHeroPlayerExpGetEvent expg = new MyHeroPlayerExpGetEvent(Amount,this, source);
		MyHeroLevelsMain.getMainClass().getServer().getPluginManager().callEvent(expg);
		LangManager.Log("Add Player exp: " + Amount);
		LangManager.Log("Player exp: " + PlayerEXP);
		LangManager.Log("Player lvl: " + Level);
		if(!expg.isCancelled())
		{
			if(PlayerEXP + expg.getExp() < Long.MAX_VALUE)
				PlayerEXP += expg.getExp();
			else
				PlayerEXP = Long.MAX_VALUE;
			
			if(checkLevel()) adaptLevel();
			updatePlayerView();
			LangManager.Log("Add Player exp: " + Amount);
			LangManager.Log("Player exp: " + PlayerEXP);
			LangManager.Log("Player lvl: " + Level);
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
		adaptExp();
	}
	public int getPlayerLevel()
	{
		return Level;
	}
	public void setPlayerExp(long exp)
	{
		PlayerEXP = exp;
		adaptLevel();
	}
	
	public long getPlayerExp()
	{
		return PlayerEXP;
	}
	
	@Override
	public String toString()
	{
		return "Exp: " +PlayerEXP + " Level: "+Level;
	}
	
	
}
