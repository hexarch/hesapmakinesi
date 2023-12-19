package com.example.hesapmakinesi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hesapmakinesi.DBHelper;

import java.util.ArrayList;

public class history extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        ArrayList<String> historyList = getHistoryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> getHistoryList() {
        ArrayList<String> list = new ArrayList<>();

        String query = "SELECT * FROM " + DBHelper.TABLE_NAME + " ORDER BY " + DBHelper.COLUMN_ID + " DESC LIMIT 25";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                float tutar = cursor.getFloat(cursor.getColumnIndex(DBHelper.COLUMN_TUTAR));
                float kdvOrani = cursor.getFloat(cursor.getColumnIndex(DBHelper.COLUMN_KDV_ORANI));
                float kdv = cursor.getFloat(cursor.getColumnIndex(DBHelper.COLUMN_KDV));
                boolean kdvDahil = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_KDV_DAHIIL)) == 1;

                String entry = "Tutar: " + tutar + ", KDV Oranı: " + kdvOrani + ", KDV: " + kdv + ", KDV Dahil: " + kdvDahil;
                list.add(entry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}
