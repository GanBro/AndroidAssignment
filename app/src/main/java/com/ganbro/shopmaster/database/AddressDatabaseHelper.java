import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "address.db";
    private static final int DATABASE_VERSION = 1;

    public AddressDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADDRESS_TABLE = "CREATE TABLE addresses (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, address TEXT)";
        db.execSQL(CREATE_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS addresses");
        onCreate(db);
    }
}
