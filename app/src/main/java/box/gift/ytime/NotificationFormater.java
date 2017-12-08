package box.gift.ytime;

import android.util.Pair;

public class NotificationFormater {

	public NotificationFormater()
	{
		
	}
	
	public static NotificationParts format(Class current, Class next, long time)
	{
		long timeUntil = (long) Math.floor((next.time - time) / 1000) * 1000; //Remove partial seconds
		String title = Class.formatTime(timeUntil, false) + " until " + next.desc;
		String context = current.desc;
		String body = "";
		return new NotificationParts(title, context, body, timeUntil);
	}

	static class NotificationParts {
		private final String body;
		private final String context;
		private final String title;
		private final long timeUntil;

		public NotificationParts(String title, String context, String body, long timeUntil)
		{
			this.title = title;
			this.context = context;
			this.body = body;
			this.timeUntil = timeUntil;
		}

		public String getTitle() {
			return title;
		}

		public String getContext() {
			return context;
		}

		public String getBody() {
			return body;
		}

		public long getTimeUntil() {
			return timeUntil;
		}
	}
}
