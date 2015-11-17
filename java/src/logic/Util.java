package logic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	
	public static boolean valiDate(String date) {
		if (date.length() != 10)
			return false;
		try {
			Date thisDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
			java.util.Calendar c = java.util.Calendar.getInstance();
			c.set(java.util.Calendar.HOUR_OF_DAY, 0);
			c.set(java.util.Calendar.MINUTE, 0);
			c.set(java.util.Calendar.SECOND, 0);
			c.set(java.util.Calendar.MILLISECOND, 0);
			Date today = c.getTime();
			if (thisDate.before(today))
				return false;
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}