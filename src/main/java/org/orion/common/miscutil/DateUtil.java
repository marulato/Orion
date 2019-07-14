package org.orion.common.miscutil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String FULL_STD_FORMAT 				= "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String STD_FORMAT 					= "yyyy-MM-dd HH:mm:ss";
    public static final String TODAY_FORMAT 				= "yyyy-MM-dd";
    public static final String TIME_24H 					= "HH:mm:ss";
    public static final String TIME_12H 					= "hh:mm:ss";
    public static final String TIME_24H_MILLIS 				= "HH:mm:ss:SSS";
    public static final String TIME_12H_MILLIS 				= "hh:mm:ss:SSS";
    public static final String SLASH_TODAY_FORMAT			= "yyyy/MM/dd";
    public static final String MD_FORMAT 					= "MM/dd";
    public static final String UPSIDE_DOWN_TODAY_FORMAT 	= "dd/MM/yyyy";
    public static final long ONE_DAY_MILLIS					= 24 * 60 * 60 * 1000;

    public static Date parseDate(String dateStr) {
        Date date = null;
        if (!StringUtil.isEmpty(dateStr)) {
            try {
                SimpleDateFormat dateFormater = new SimpleDateFormat(FULL_STD_FORMAT);
                date = dateFormater.parse(dateStr);
            } catch (ParseException e0) {
                try {
                    SimpleDateFormat dateFormater = new SimpleDateFormat(STD_FORMAT);
                    date = dateFormater.parse(dateStr);
                } catch (ParseException e1) {
                    try {
                        SimpleDateFormat dateFormater = new SimpleDateFormat(TODAY_FORMAT);
                        date = dateFormater.parse(dateStr);
                    } catch (ParseException e2) {
                        try {
                            SimpleDateFormat dateFormater = new SimpleDateFormat(TIME_24H);
                            date = dateFormater.parse(dateStr);
                        } catch (ParseException e3) {
                            try {
                                SimpleDateFormat dateFormater = new SimpleDateFormat(TIME_12H);
                                date = dateFormater.parse(dateStr);
                            } catch (ParseException e4) {
                                try {
                                    SimpleDateFormat dateFormater = new SimpleDateFormat(SLASH_TODAY_FORMAT);
                                    date = dateFormater.parse(dateStr);
                                } catch (ParseException e5) {
                                    try {
                                        SimpleDateFormat dateFormater = new SimpleDateFormat(MD_FORMAT);
                                        date = dateFormater.parse(dateStr);
                                    } catch (ParseException e6) {
                                        try {
                                            SimpleDateFormat dateFormater = new SimpleDateFormat(UPSIDE_DOWN_TODAY_FORMAT);
                                            date = dateFormater.parse(dateStr);
                                        } catch (ParseException e7) {
                                            try {
                                                SimpleDateFormat dateFormater = new SimpleDateFormat(TIME_24H_MILLIS);
                                                date = dateFormater.parse(dateStr);
                                            } catch (ParseException e8) {
                                                try {
                                                    SimpleDateFormat dateFormater = new SimpleDateFormat(TIME_12H_MILLIS);
                                                    date = dateFormater.parse(dateStr);
                                                } catch (ParseException e9) {
                                                    date = null;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return date;
    }

    public static Date now() {
        return new Date();
    }

    public static Date today() {
        Date date = null;
        try {
            SimpleDateFormat dateFormater = new SimpleDateFormat(TODAY_FORMAT);
            date = dateFormater.parse(dateFormater.format(now()));
        } catch (ParseException e0) {
            try {
                SimpleDateFormat dateFormater = new SimpleDateFormat(SLASH_TODAY_FORMAT);
                date = dateFormater.parse(dateFormater.format(now()));
            } catch (ParseException e1) {
                try {
                    SimpleDateFormat dateFormater = new SimpleDateFormat(UPSIDE_DOWN_TODAY_FORMAT);
                    date = dateFormater.parse(dateFormater.format(now()));
                } catch (ParseException e2) {
                    date = now();
                }
            }
        }
        return date;
    }

    public static Calendar getCalendar(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int getYear(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addYear(Date date,int year) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static Date addMonth(Date date,int month) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static Date addWeek(Date date,int week) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.WEEK_OF_YEAR, week);
        return calendar.getTime();
    }

    public static Date addDay(Date date, int day) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return calendar.getTime();
    }

    public static Timestamp getTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    public static Date getDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp.getTime());
    }

    public static Date getDate(long millis) {
        return new Date(millis);
    }

    public static Date getDate(String dateStr, String format) {
        if (StringUtil.isEmpty(format) || StringUtil.isEmpty(dateStr)) {
            return null;
        }
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            date = formater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    public static int getDaysBetween(Date from, Date to) {
        if (from != null && to != null) {
            return (int) (millisBetween(from, to) / ONE_DAY_MILLIS);
        }
        return -1;
    }

    public static long millisBetween(Date from, Date to) {
        if (from == null || to == null)
            return -1;
        long fromMillis = from.getTime();
        long toMillis = to.getTime();
        long temp = toMillis - fromMillis;
        long between = temp >= 0 ? temp : -temp;
        return between;
    }

    public static String getLogDate(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_STD_FORMAT);
        return dateFormat.format(date);
    }

    public static String getStandardDate(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(STD_FORMAT);
        return dateFormat.format(date);
    }

    public static String getWebDate(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(SLASH_TODAY_FORMAT);
        return dateFormat.format(date);
    }

    public static int getAge(Date birthday) {
        if (birthday != null) {
            int birthYear = getYear(birthday);
            int birthMon = getMonth(birthday);
            int birthDay = getDayOfMonth(birthday);
            int currentYear = getYear(now());
            int currentMon = getMonth(now());
            int currentDay = getDayOfMonth(now());

            int passed = currentYear - birthYear;
            if (currentMon < birthMon)
                return passed - 1;
            else if (currentMon > birthMon)
                return passed;
            else {
                if (currentDay < birthDay)
                    return passed - 1;
                else
                    return passed;
            }
        }
        return -1;
    }

    public static boolean same(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.getTime() == date2.getTime();
        }
        return false;
    }

    public static boolean isBetween(Date date, Date from, Date to) {
        if (date != null && from != null && to != null) {
            if (same(date, from) || same(date, to))
                return true;
            if (date.after(from) && date.before(to))
                return true;
        }
        return false;
    }

    public static String getConstellation(Date date) {
        if (date != null) {
            int month = getMonth(date) + 1;
            int day = getDayOfMonth(date);
            String constellation = null;
            if (month == 1 && day >= 20 || month == 2 && day <= 18) {
                constellation = "AQUA";
            }
            if (month == 2 && day >= 19 || month == 3 && day <= 20) {
                constellation = "PIS";
            }
            if (month == 3 && day >= 21 || month == 4 && day <= 19) {
                constellation = "ARI";
            }
            if (month == 4 && day >= 20 || month == 5 && day <= 20) {
                constellation = "TAU";
            }
            if (month == 5 && day >= 21 || month == 6 && day <= 21) {
                constellation = "GEM";
            }
            if (month == 6 && day >= 22 || month == 7 && day <= 22) {
                constellation = "CAN";
            }
            if (month == 7 && day >= 23 || month == 8 && day <= 22) {
                constellation = "LEO";
            }
            if (month == 8 && day >= 23 || month == 9 && day <= 22) {
                constellation = "VIR";
            }
            if (month == 9 && day >= 23 || month == 10 && day <= 23) {
                constellation = "LIB";
            }
            if (month == 10 && day >= 24 || month == 11 && day <= 22) {
                constellation = "SCO";
            }
            if (month == 11 && day >= 23 || month == 12 && day <= 21) {
                constellation = "SAG";
            }
            if (month == 12 && day >= 22 || month == 1 && day <= 19) {
                constellation = "CAP";
            }
            return constellation;
        }
        return null;
    }

}
