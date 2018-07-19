package MyHero_Levels.API;

import java.util.HashMap;
import java.util.UUID;

import MyHero_Levels.Core.MyHeroMain_Levels;
import cn.nukkit.Player;

public class MyHeroLevelsAPI {

	private HashMap<UUID,MyHeroLevel> PlayersData = new HashMap<UUID, MyHeroLevel>();
	
	
	
	
	private int MaxLevel;
	private String Equation;
	
	private long[] LevelsTable;
	public long[] getLevelsTable() {
		return LevelsTable;
	}

	public void setLevelsTable(long[] levelsTable) {
		LevelsTable = levelsTable;
	}

	/**
	 * @param playername
	 * @return return null if player player dont exist 
	 */
	public MyHeroLevel getMyHeroLevel(String playername)
	{
		Player p = MyHeroMain_Levels.getMainClass().getServer().getPlayer(playername);
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
		else if(level >= 1)
			return LevelsTable[level-1];
		else
			return 0;
	}
	/**
	 * @param level
	 * @return level from given exp
	 */
	public int getLevelFromExp(long exp)
	{
		for(int i = 1; i < getMaxLevel(); i++) 
			if(exp < getExpFromLevel(i)) {return i;}
		return 1;
	}
	
	
	
	private void generateEquation()
	{
		long[] exptable = new long[MaxLevel];
		/*ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");*/
		
		try 
		{
			for(int i = 0; i < MaxLevel;i++)
			{
				//LangManager.Log(Equation.replaceAll("%level%", i+""));
				//exptable[i] = ((Integer )engine.eval(Equation.replaceAll("%level%", i+""))).longValue();
				exptable[i] = (long) eval(Equation.replaceAll("%level%", i+""));
				//LangManager.Log(eval(Equation.replaceAll("%level%", i+""))+"");
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		LevelsTable = exptable;
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
	public static double eval(final String str) {
	    return new Object() {
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }

	        double parseFactor() {
	            if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus

	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                x = parseFactor();
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }

	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

	            return x;
	        }
	    }.parse();
	}
}
