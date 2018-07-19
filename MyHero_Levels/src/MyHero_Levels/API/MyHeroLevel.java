package MyHero_Levels.API;

import MyHero_Core.Core.MyHeroMain;
import MyHero_Levels.Core.MyHeroMain_Levels;
import MyHero_Levels.Events.MyHeroExpSource;
import MyHero_Levels.Events.MyHeroPlayerExpGetEvent;
import MyHero_Levels.Events.MyHeroPlayerLevelUpEvent;
import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.scheduler.Task;

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
		MyHeroLevelsAPI api = MyHeroMain_Levels.getAPI();
		for(int i = 1; i <= api.getMaxLevel(); i++) if(PlayerEXP < api.getExpFromLevel(i)) {Level = i;break;}
	}
	
	/**
	 *  Convert player exp to correct level
	 */
	public void adaptExp()
	{
		PlayerEXP = MyHeroMain_Levels.getAPI().getExpFromLevel(Level);
	}
	
	
	/**
	 *  Check if player should have level up
	 */
	public boolean checkLevel()
	{
		MyHeroLevelsAPI api = MyHeroMain_Levels.getAPI();
		return (Level <= api.getMaxLevel()) && (PlayerEXP >= api.getExpFromLevel(Level-1));
	}
	
	
	public void playerLevelUp(int OldLeve, int NewLevel)
	{
		MyHeroPlayerLevelUpEvent plue = new MyHeroPlayerLevelUpEvent(this,OldLeve,NewLevel);
		MyHeroMain_Levels.getMainClass().getServer().getPluginManager().callEvent(plue);
		if(!plue.isCancelled())
			Level = NewLevel;
	}
	
	public void updatePlayerView()
	{
		MyHeroLevelsAPI api = MyHeroMain_Levels.getAPI();
		UpdateAttributesPacket pk = new UpdateAttributesPacket();
		pk.entityId = player.getId();
		pk.entries = new Attribute[]{
				Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(Level),
				Attribute.getAttribute(Attribute.EXPERIENCE).setValue(
						
						( (float) ( ( ( PlayerEXP - api.getExpFromLevel(Level-1) ) * 100 ) / ( api.getExpFromLevel(Level) - api.getExpFromLevel(Level-1))) /100 ) 
						
						)
		};
		
		MyHeroMain.getMain().getServer().getScheduler().scheduleDelayedTask(new Task(){
			@Override
			public void onRun(int arg0) {
				player.setExperience(0, Level);
				player.dataPacket(pk);
				
			}
		
		}, 1);
		
	}
	
	
	public void addExp(long Amount, MyHeroExpSource source)
	{
		MyHeroLevelsAPI api = MyHeroMain_Levels.getAPI();
		MyHeroPlayerExpGetEvent expg = new MyHeroPlayerExpGetEvent(Amount,this, source);
		MyHeroMain_Levels.getMainClass().getServer().getPluginManager().callEvent(expg);
		if(!expg.isCancelled())
		{
			if(PlayerEXP + expg.getExp() < Long.MAX_VALUE)
				PlayerEXP += expg.getExp();
			else
				PlayerEXP = Long.MAX_VALUE;
			

			if(checkLevel()) playerLevelUp(Level, api.getLevelFromExp(PlayerEXP));
			
			updatePlayerView();
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
		MyHeroMain_Levels.getMainClass().getServer().getPluginManager().callEvent(expg);
		if(!expg.isCancelled())
		{
			if(PlayerEXP - Amount >= 0)
				PlayerEXP -= Amount;
			else
				clearPlayerExp();
			adaptLevel();
		}
		updatePlayerView();
	}
	
	
	public float getExpForNextLevel()
	{
		return MyHeroMain_Levels.getAPI().getExpFromLevel(Level) - PlayerEXP;
	}
	
	public float getExpToLevel(int level)
	{
		return MyHeroMain_Levels.getAPI().getExpFromLevel(level) - PlayerEXP;
	}
	
	public void addLevel(int i)
	{
		MyHeroPlayerLevelUpEvent plue = new MyHeroPlayerLevelUpEvent(this,Level,Level+i);
		MyHeroMain_Levels.getMainClass().getServer().getPluginManager().callEvent(plue);
		
		if(!plue.isCancelled())
		{
			Level += i;
			adaptExp();
		}
		updatePlayerView();
	}
	
	public void subtractLevel(int i)
	{
		MyHeroPlayerLevelUpEvent plue = new MyHeroPlayerLevelUpEvent(this,Level,Level-i);
		MyHeroMain_Levels.getMainClass().getServer().getPluginManager().callEvent(plue);
		
		if(!plue.isCancelled())
		{
			Level -= i;
			adaptExp();
		}
		updatePlayerView();
	}
	
	
	public void clearPlayerExp()
	{
		PlayerEXP = 0;
		adaptLevel();
		updatePlayerView();
	}
	
	public void setPlayerLevel(int lvl)
	{
		Level = lvl;
		adaptExp();
		updatePlayerView();
	}
	public int getPlayerLevel()
	{
		return Level;
	}
	public void setPlayerExp(long exp)
	{
		PlayerEXP = exp;
		adaptLevel();
		updatePlayerView();
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
