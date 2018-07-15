package MyHero_Levels.API;

import java.util.HashMap;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import MyHero_Levels.Core.MyHeroLevelsMain;
import cn.nukkit.Player;

public class MyHeroLevelsAPI {

	private HashMap<UUID,MyHeroLevel> PlayersData = new HashMap<UUID, MyHeroLevel>();
	
	
	
	
	private int MaxLevel;
	private String Equation;
	
	private long[] LevelsTable;
	/**
	 * @param playername
	 * @return return null if player player dont exist 
	 */
	public MyHeroLevel getMyHeroLevel(String playername)
	{
		Player p = MyHeroLevelsMain.getMainClass().getServer().getPlayer(playername);
		return getMyHeroLevel(p);

	}
	
	/**
	 * @param player
	 * @return return null if player player dont exist 
	 */
	public MyHeroLevel getMyHeroLevel(Player player)
	{
		if(player != null && PlayersData.containsKey(player.getUniqueId())) return PlayersData.get(player.getUniqueId());
		else return null;
	}
	
	public void clearPlayerData(Player p)
	{
		clearPlayerData(p.getUniqueId());
	}
	public void clearPlayerData(UUID uuid)
	{
		if(PlayersData.containsKey(uuid))PlayersData.remove(uuid);
	}
	
	public void addPlayerData(MyHeroLevel data,UUID uuid)
	{
		PlayersData.put(uuid, data);
	}
	
	/**
	 * @param level
	 * @return exp needed to reach a given level
	 */
	public long getExpFromLevel(int level)
	{
		if(level > MaxLevel)
			return LevelsTable[MaxLevel-1];
		else
			return LevelsTable[level - 1];
	}
	/**
	 * @param level
	 * @return level from given exp
	 */
	public int getLevelformExp(long exp)
	{
		for(int i = 0; i < getMaxLevel(); i++) if(exp >= getExpFromLevel(i)) {return i;}
		return 1;
	}
	
	
	
	private void generateEquation()
	{
		long[] exptable = new long[MaxLevel];
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		
		try 
		{
			for(int i = 0; i < MaxLevel;i++)
			{
				exptable[i] = ((Integer )engine.eval(Equation.replaceAll("%level%", i+""))).longValue();
			}
		} 
		catch (ScriptException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public void setMaxLevel(int maxlevel)
	{
		if(Integer.MAX_VALUE > maxlevel)
			MaxLevel = maxlevel;
		else
			MaxLevel = Integer.MAX_VALUE;
	}

	public void setEquation(String s)
	{
		Equation = s;
		generateEquation();
	}
	
	public int getMaxLevel()
	{
		return MaxLevel;
	}
	
	public HashMap<UUID,MyHeroLevel>  getPlayersData()
	{
		return  PlayersData;
	}
	
}
