package cop4656.jrdbnntt.com.groupproject1.provider.table;


/**
 * TODO
 */
public abstract class DatabaseTable {
    public static final String COLUMN_ID = "_id";

    public Long id;

    public abstract String getTableName();
    public abstract String getCreateSql();


    public void setId(Long id) {
        this.id = id;
    }

}
