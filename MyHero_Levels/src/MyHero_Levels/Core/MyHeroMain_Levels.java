package MyHero_Levels.Core;

import MyHero_Core.Core.MyHeroMain;
import MyHero_Levels.API.MyHeroLevel;
import MyHero_Levels.API.MyHeroLevelsAPI;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

public class MyHeroMain_Levels extends PluginBase implements Listener{

	private static MyHeroLevelsAPI API;
	private static MyHeroMain_Levels MainClass;
	
	@Override
	public void onEnable()
	{
		MainClass = this;
		API = DataManager.InicializeAPI();
		this.getServer().getPluginManager().registerEvents(this,this);
		MyHeroMain.getMyHeroData().MyHeroLevels = true;
	}
	
	public static MyHeroLevelsAPI getAPI()
	{
		return API;
	}
	
	public static MyHeroMain_Levels getMainClass()
	{
		return MainClass;
	}
	
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		MyHeroLevel mhl = API.getMyHeroLevel(p);
		if(mhl == null)
		{
			mhl = DataManager.LoadPlayerData(p);
			if(mhl == null)
			{
				DataManager.createPlayer(p, 0);
				mhl = DataManager.LoadPlayerData(p);
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		MyHeroLevel mhl = API.getMyHeroLevel(p);
		if(mhl != null)
		{
			DataManager.savePlayerData(p, mhl);
			API.clearPlayerData(p);
		}
	}
}
