package com.example.hesapmakinesi;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hesapmakinesi.DBHelper;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private boolean kdvDahil;

    private float tutar;
    private float kdvOrani;
    private float kdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        Button historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, history.class);
                startActivity(intent);
            }
        });

        Button hesaplaBtn = findViewById(R.id.hesaplaBtn);
        hesaplaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tutarInput = findViewById(R.id.tutartxt);
                EditText kdvOraniInput = findViewById(R.id.kdvOranInp);

                tutar = Float.parseFloat(tutarInput.getText().toString());
                kdvOrani = Float.parseFloat(kdvOraniInput.getText().toString());
                kdvDahil = true;  // Uygulama mantığına göre ayarlayın

                kdv = kdvHesaplama(kdvOrani, tutar);

                fiyatGuncelle();
            }
        });
    }

    void fiyatGuncelle() {
        TextView araToplamTxt = findViewById(R.id.araToplamInp);
        TextView kdvTutariInp = findViewById(R.id.KdvTutariInp);
        TextView kdvDahilTutarInp = findViewById(R.id.kdvDahilInp);
        TextView genelToplamInp = findViewById(R.id.genelToplamInp);

        if (kdvDahil) {
            genelToplamInp.setText(String.valueOf(tutar));
            araToplamTxt.setText(String.valueOf(tutar - kdv));
            kdvDahilTutarInp.setText(String.valueOf(tutar));
            kdvTutariInp.setText(String.valueOf(kdv));
        } else {
            genelToplamInp.setText(String.valueOf(tutar + kdv));
            araToplamTxt.setText(String.valueOf(tutar));
            kdvDahilTutarInp.setText(String.valueOf(tutar + kdv));
            kdvTutariInp.setText(String.valueOf(kdv));
        }

        saveToDatabase(tutar, kdvOrani, kdv, kdvDahil);
    }

    private void saveToDatabase(float tutar, float kdvOrani, float kdv, boolean kdvDahil) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TUTAR, tutar);
        values.put(DBHelper.COLUMN_KDV_ORANI, kdvOrani);
        values.put(DBHelper.COLUMN_KDV, kdv);
        values.put(DBHelper.COLUMN_KDV_DAHIIL, kdvDahil ? 1 : 0);

        database.insert(DBHelper.TABLE_NAME, null, values);
    }

    private float kdvHesaplama(float kdvOrani, float fiyat) {
        return (fiyat / 100 * kdvOrani);
    }
}
