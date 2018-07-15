package MyHero_Levels.Core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.UUID;

import MyHero_Core.Managers.ConnectionManager;
import MyHero_Core.Managers.ResourceManager;
import MyHero_Levels.API.MyHeroLevel;
import MyHero_Levels.API.MyHeroLevelsAPI;
import cn.nukkit.Player;

public class DataManager {

	private static String TableName = "PlayersLevels";
	//private static Connection conn;
	
	
	public static MyHeroLevelsAPI InicializeAPI()
	{
		MyHeroLevelsAPI api = new MyHeroLevelsAPI();

		Map<String, Object> Config = ResourceManager.getConfig("Levels");
		for(Map.Entry<String, Object> conf : Config.entrySet())
		{
			if(conf.getKey().equalsIgnoreCase("MaxLevel"))
				api.setMaxLevel(Integer.parseInt(conf.getValue().toString()));
			else if(conf.getKey().equalsIgnoreCase("Equation"))
				api.setEquation(conf.getValue().toString());
			else if(conf.getKey().equalsIgnoreCase("ForceLoad"))
				if(((boolean)conf.getValue())) loadAllPlayerData();
		}
		
		dataBaseInicialization();
		return api;
	}
	public static void SaveAllPlayersData()
	{
		MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
		api.getPlayersData().forEach( (uuid,mhl) -> 
		{
			savePlayerData(uuid,mhl);
		});
	}
	
	public static void createPlayer(Player player,long exp)
	{
		createPlayer(player.getUniqueId(),exp);
	}
	public static void createPlayer(Player player,MyHeroLevel mhl)
	{
		createPlayer(player.getUniqueId(),mhl.getPlayerExp());
	}
	public static void createPlayer(UUID uuid,MyHeroLevel mhl)
	{
		createPlayer(uuid,mhl.getPlayerExp());
	}
	
	public static void createPlayer(UUID uuid,long exp)
	{
		try 
		{
			Connection con =ConnectionManager.getConnection("PlayersLevels");
			Statement st = con.createStatement();
			st.execute("INSERT INTO "+TableName+ " (uuid,exp) VALUES ('" +uuid.toString()+"',"+exp+ ")" );
			con.close();
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void savePlayerData(Player player,long exp) 
	{
		savePlayerData(player.getUniqueId(), exp);
	}
	public static void savePlayerData(Player player,MyHeroLevel mhl) 
	{
		savePlayerData(player.getUniqueId(), mhl.getPlayerExp());
	}
	public static void savePlayerData(UUID uuid,MyHeroLevel mhl)
	{
		savePlayerData(uuid, mhl.getPlayerExp());
	}
	
	public static void savePlayerData(UUID uuid,long exp)
	{
		try 
		{
			Connection con =ConnectionManager.getConnection("PlayersLevels");
			Statement st = con.createStatement();
			if(!st.execute("UPDATE " + TableName + " SET uuid = '" + uuid.toString() + "', exp = "+ exp ))
				createPlayer(uuid,exp);
			con.close();
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * @param playeruuid
	 * @return null if player dont exist
	 */
	public static MyHeroLevel LoadPlayerData(Player player)
	{
		return LoadPlayerData(player.getUniqueId());
	}
	
	/**
	 * @param playeruuid
	 * @return null if player dont exist
	 */
	public static MyHeroLevel LoadPlayerData(UUID playeruuid)
	{
		MyHeroLevel mhl = null;
		try 
		{
			Connection con =ConnectionManager.getConnection("PlayersLevels");
			ResultSet result = con.prepareStatement("SELECT * FROM "+TableName + " WHERE uuid = '" + playeruuid.toString()+"'").executeQuery();
			MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
			
			if(result.next())
			{
				mhl = new MyHeroLevel();
				mhl.setPlayerExp(result.getLong(2));
				api.addPlayerData(mhl, UUID.fromString(result.getString(1)));
			}
			con.close();
		}
		catch (SQLException | IllegalArgumentException e) 
		{
			e.printStackTrace();
		}
		return mhl;
	}
	
	private static void loadAllPlayerData()
	{
		Connection con =ConnectionManager.getConnection("PlayersLevels");
		MyHeroLevelsAPI api = MyHeroLevelsMain.getAPI();
		try 
		{
			ResultSet result = con.prepareStatement("SELECT * FROM "+TableName).executeQuery();
			while(result.next())
			{
				MyHeroLevel mhl = new MyHeroLevel();
				mhl.setPlayerExp(result.getLong(2));
				api.addPlayerData(mhl, UUID.fromString(result.getString(1)));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		
		
	}
	
	private static void dataBaseInicialization()
	{
		
		try 
		{
			Connection con = ConnectionManager.getConnection("PlayersLevels");
			Statement stat = con.createStatement();
			stat.execute("CREATE TABLE IF NOT EXISTS "+TableName+" (uuid varchar(100) NOT NULL, exp BIGINT ,PRIMARY KEY (uuid))");
			con.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

}
