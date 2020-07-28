import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String tableName = "container";

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createTable(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + tableName + "(score integer, time text, first text, second text, third text)";
        try
        {
            db.execSQL(sql);
        }
        catch (SQLException e)
        {
        }
    }

    public void insertName(SQLiteDatabase db, int score, String first, String second, String third, String time)
    {
        db.beginTransaction();
        try
        {
            String sql = "insert into " + tableName + "(score, time, first, second, third)" + " values('" + score + "')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }

}
