package cop4656.jrdbnntt.com.groupproject1.provider.types;

import java.text.ParseException;

/**
 * For types that can be
 */
abstract class SQLiteSerializedType {
    abstract public String serialize();
    protected abstract void deserialize(String serialization) throws ParseException;

    @Override
    public String toString() {
        return serialize();
    }
}
