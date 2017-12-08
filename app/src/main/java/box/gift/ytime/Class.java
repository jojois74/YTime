package box.gift.ytime;

public class Class {
	long time;
	String desc;
	String formatedTime;
	
	public Class(long time, String desc)
	{
		this.time = time;
		this.desc = desc;
		formatedTime = formatTime(this.time, true);
	}
	
	public String toString()
	{
		return "Time: " + formatedTime + " Description: " + desc;
	}
	
	public static String formatTime(long time, boolean ampm)
	{
		boolean pm = false;
		int hours = (int) (time/3.6e+6);
		if (hours > 12)
		{
			pm = true;
		}
		hours %= 12;
		int mins = (int) ((time / 60000) % 60);
		int secs = (int) ((time / 1000) % 60);
		
		if (ampm)
			return hours + ":" + String.format("%02d", mins) + (pm ? "pm" : "am");
		return hours + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
	}
}
