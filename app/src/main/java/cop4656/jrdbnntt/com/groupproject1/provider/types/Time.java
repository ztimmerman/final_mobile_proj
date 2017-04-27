package cop4656.jrdbnntt.com.groupproject1.provider.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Stored in database as "hh:mma" and does not include timezone data
 */
public class Time extends SQLiteSerializedType {
    private static final String TIME_FORMAT =  "h:mm a";

    private Calendar calendar;

    public Time(String serializedString) throws ParseException {
        deserialize(serializedString);
    }

    @Override
    public String serialize() {
        return getDateFormat().format(calendar.getTime());
    }


    @Override
    protected void deserialize(String serialization) throws ParseException {
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(getDateFormat().parse(serialization));
    }

    public Calendar getCalendar() {
        return calendar;
    }


    private SimpleDateFormat getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat;
    }
}
