package box.gift.ytime;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Scanner;

import android.content.Context;
import android.util.Pair;


public class ScheduleEngine {
	private Context ctx;
	private InputStream stream;
	
	public ScheduleEngine(Context ctx)
	{
		this.ctx = ctx;
	}
	
	public NotificationFormater.NotificationParts getNotification()
	{
		return NotificationFormater.format(currentClass(), nextClass(), currentTime());
	}
	
	private Class currentClass()
	{
		stream = ctx.getResources().openRawResource(R.raw.schedule);
		Scanner schedule = new Scanner(stream);
		long nextClassTime = -100;
		String nextClassDesc = "";
		int nextClassNumber = 0;
		int day = currentDay();
		String firstLine = schedule.nextLine(); //Skip description line
		Scanner desc = new Scanner(firstLine);
		desc.useDelimiter(",");
		desc.next(); //Skip first cell
		while (schedule.hasNextLine())
		{
			String line = schedule.nextLine();
			if (Integer.parseInt(Character.toString(line.charAt(0))) == day)
			{
				//We found the correct day...
				Scanner inner = new Scanner(line);
				inner.useDelimiter(",");
				inner.next(); //Skip day number
				
				long time = currentTime();
				while (inner.hasNext())
				{
					nextClassNumber++;
					long nextTime = Long.parseLong(inner.next());
					if (time < nextTime)
					{
						nextClassTime = nextTime;
						inner.close();
						break;
					}
				}
			}
			
			while (nextClassNumber > 1)
			{
				nextClassNumber--;
				nextClassDesc = desc.next();
			}

		}
		desc.close();
		
		return new Class(0, nextClassDesc);
	}
	
	private Class nextClass()
	{
		stream = ctx.getResources().openRawResource(R.raw.schedule);
		Scanner schedule = new Scanner(stream);
		long nextClassTime = -100;
		String nextClassDesc = "";
		int nextClassNumber = 0;
		int day = currentDay();
		String firstLine = schedule.nextLine(); //Skip description line
		Scanner desc = new Scanner(firstLine);
		desc.useDelimiter(",");
		desc.next(); //Skip first cell
		while (schedule.hasNextLine())
		{
			String line = schedule.nextLine();
			if (Integer.parseInt(Character.toString(line.charAt(0))) == day)
			{
				//We found the correct day...
				Scanner inner = new Scanner(line);
				inner.useDelimiter(",");
				inner.next(); //Skip day number
				
				long time = currentTime();
				while (inner.hasNext())
				{
					nextClassNumber++;
					long nextTime = Long.parseLong(inner.next());
					if (time < nextTime)
					{
						nextClassTime = nextTime;
						inner.close();
						break;
					}
				}
			}
			
			while (nextClassNumber > 0)
			{
				nextClassNumber--;
				nextClassDesc = desc.next();
			}

		}
		
		desc.close();
		
		return new Class(nextClassTime, nextClassDesc);
	}
	
	private long currentTime()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long millis = (System.currentTimeMillis() - c.getTimeInMillis());
		return millis;
	}
	
	private int currentDay()
	{
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK); 

		switch (day) {
		    case Calendar.SUNDAY:
		        return 1;

		    case Calendar.MONDAY:
		        return 2;

		    case Calendar.TUESDAY:
		        return 3;
		        
		    case Calendar.WEDNESDAY:
		        return 4;
		        
		    case Calendar.THURSDAY:
		        return 5;
		        
		    case Calendar.FRIDAY:
		        return 6;
		}
		
		return 100; //Impossible
	}
}
