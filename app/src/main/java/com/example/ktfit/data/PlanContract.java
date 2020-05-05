package com.example.ktfit.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class PlanContract {
    public static final String CONTENT_AUTHORITY = "com.example.ktfit";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PLAN = "plan";

    private PlanContract(){}

    public static final class PlanEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAN);
        public final static String TABLE_NAME = "plan";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DAY = "day";
        public final static String COLUMN_MONTH = "month";
        public final static String COLUMN_YEAR = "year";
        public final static String COLUMN_START_TIME_HOUR = "start_time_hour";
        public final static String COLUMN_START_TIME_MINUTE = "start_time_minute";
        public final static String COLUMN_END_TIME_HOUR = "end_time_hour";
        public final static String COLUMN_END_TIME_MINUTE = "end_time_minute";
        public final static String COLUMN_FRIEND = "friend";
        public final static String COLUMN_REPEAT = "repeat";

        public static final int REPEAT_UNKNOWN = 0;
        public static final int REPEAT_DAILY = 1;
        public static final int REPEAT_WEEKLY = 2;
        public static final int REPEAT_MONTHLY = 3;
        public static final int REPEAT_NEVER = 4;
    }
}
