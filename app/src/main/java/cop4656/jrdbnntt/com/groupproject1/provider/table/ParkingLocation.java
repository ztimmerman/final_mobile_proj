package cop4656.jrdbnntt.com.groupproject1.provider.table;

public class ParkingLocation extends DatabaseTable {
    public static final String TABLE_NAME = "parking_location";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";

    public Integer id;
    public String name;
    public String location;

    public ParkingLocation() {}

    @Override
    public String getCreateSql() {
        return "CREATE TABLE " + TABLE_NAME + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME  + " TEXT,"
                + COLUMN_LOCATION + " TEXT);";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}