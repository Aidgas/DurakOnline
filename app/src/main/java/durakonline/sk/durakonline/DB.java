package durakonline.sk.durakonline;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by root on 17.07.15.
 */
public class DB
{
    public String DB_APPLICATION   = "base.db";
    public Object _lock_db         = new Object();
    private Context ma        = null;

    public DB(Context _ma)
    {
        ma = _ma;
    }

    public void _db_cteate_tables()
    {
        synchronized(_lock_db) {

            SQLiteDatabase mydb = ma.openOrCreateDatabase(DB_APPLICATION, Context.MODE_PRIVATE, null);

            mydb.execSQL("create table if not exists keyvalue ("+
                    "  id  INTEGER primary key autoincrement" // 0
                    + ", k TEXT"
                    + ", v TEXT"
                    + ")");

            mydb.close();
        }
    }

    public String getKeyValue(String key, String default_value)
    {
        String result = default_value;

        synchronized (_lock_db)
        {
            SQLiteDatabase mydb = ma.openOrCreateDatabase(DB_APPLICATION, Context.MODE_PRIVATE, null);

            Cursor allrows = mydb.rawQuery("SELECT v FROM keyvalue WHERE k =" + DatabaseUtils.sqlEscapeString(key),
                    null);

            if(allrows.getCount() > 0)
            {
                allrows.moveToFirst();
                result = allrows.getString(0);
            }

            allrows.close();
            mydb.close();
        }

        return result;
    }

    public void setKeyValue(String key, String value)
    {
        boolean update = false;

        synchronized (_lock_db)
        {
            SQLiteDatabase mydb = ma.openOrCreateDatabase(DB_APPLICATION, Context.MODE_PRIVATE, null);

            Cursor allrows = mydb.rawQuery("SELECT v FROM keyvalue WHERE k =" + DatabaseUtils.sqlEscapeString(key), null);

            if(allrows.getCount() > 0)
            {
                update = true;
            }

            allrows.close();

            if( update )
            {
                String q = "UPDATE keyvalue" +
                        " SET v = " + DatabaseUtils.sqlEscapeString(value) +
                        " WHERE k = " + DatabaseUtils.sqlEscapeString(key);

                //Log.i("TAG", q);

                mydb.execSQL(q);
            }
            else
            {
                String q = "INSERT INTO keyvalue VALUES("
                        + "NULL,"
                        + DatabaseUtils.sqlEscapeString(key) + ","
                        + DatabaseUtils.sqlEscapeString(value)
                        + ")";

                //Log.i("TAG", q);

                mydb.execSQL(q);
            }

            mydb.close();
        }
    }

}
