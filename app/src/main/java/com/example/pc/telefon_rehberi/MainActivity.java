package com.example.pc.telefon_rehberi;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText adGiris, soyadGiris, telGiris, ismeGöreArat;
    TextView bilgiler;
    private String LOG ="Kayıt : ";
    Button kaydet, kisiAra, sil, guncelle;
    private veritabani v1;
    private  SQLiteDatabase db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//onCreate de xml e baglanma işlemi yapılıyor.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v1 = new veritabani(this);
        bilgiler = (TextView) findViewById(R.id.textView);//R.id. ile javaya baglanmış oluyor.
        adGiris = (EditText) findViewById(R.id.editText2);
        soyadGiris = (EditText) findViewById(R.id.editText3);
        telGiris = (EditText) findViewById(R.id.editText4);
        ismeGöreArat = (EditText) findViewById(R.id.editText5);

        kisiAra = (Button) findViewById(R.id.button2);
        kaydet = (Button) findViewById(R.id.button);
        guncelle = (Button) findViewById(R.id.button4);
        sil = (Button)findViewById(R.id.button3);


        kaydet.setOnClickListener(new View.OnClickListener() {//kaydet butonunun click eventi

            @Override
            public void onClick(View v) {//tıklandığında kaydet butonuna gidecek ve orada kayıt işlemi yapacak.view v viev turunden parametre almış bu bizim butonlarımız.
                try {
                    kaydet(adGiris.getText().toString(), soyadGiris.getText().toString(), telGiris.getText().toString());
                } finally {
                    v1.close();
                }
            }
        });


        kisiAra.setOnClickListener(new View.OnClickListener() {//bu  butona tıklandığında bilgileri goster metoduna gidecek
            @Override
            public void onClick(View v) {
                bilgilerigoster(ismeGöreArat.getText().toString()); //bu metodda kişiler tablosundan sutunlar isimli sütunlar cekilecek
            }
        });

    }

    /*private void bilgilerigoster(String s) {
    }*/

    private void kaydet(String ad, String soyad, String tel) {
        //try {
        db1 = v1.getWritableDatabase();
        ContentValues cv1 = new ContentValues();// sınıftır tablodaki sutun adları ve bunlara kaydedecegimiz bilgiler vardır.
        cv1.put("ad", ad);//put ile sutun isimlerine aktarım yapılıyo.
        cv1.put("soyad", soyad);
        cv1.put("tel", tel);
        db1.insertOrThrow("kişiler", null, cv1);//bilgileri tasıyan cv1 nesnesi,veritabanına eklenecek
        db1.close();
        Toast.makeText(getApplicationContext(), "kayıt işlemi tamamlandı", Toast.LENGTH_SHORT).show();
        // } catch (Exception e) {
        // Toast.makeText(getApplicationContext(), "kayıt işleminde hata olustu", Toast.LENGTH_SHORT).show();
    }


    private void bilgilerigoster(String kişiAd) {
        try {
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
            //Adapter verileri arayuze aktarır.
            String[] sutunlar = {"ad", "soyad", "tel"};
            // hangi sütundaki verileri çekeceğimizi gösteren bir dizi şeklinde oluşturduk.
            SQLiteDatabase db1 = v1.getReadableDatabase();
            //okuma yapacağımız için bu sefer getReadableDatabase()

            //asagıda:ada gore arama yapıldı.3.parametre WHERE ifadesi
            Cursor okunanlar = db1.query("kişiler", sutunlar, "ad=?", new String[]{kişiAd}, null, null, null);
            //Cursor, veritabanından çekilen veriler arasında gezinmeyi sağlar.

            String ad = "";
            String soyad = "";
            int tel;

            while (okunanlar.moveToNext()) {//veritabanındaki kayıtlar SATIR SATIR okunacak.
                //Bu döngü içerisinde de ad, soyad,telNo adlı sütunlardan veriler çekilecek ve veriler adlı değişkene atılacaktır.
                // Son olarak da textview içerisine yazılacak.
                ad = okunanlar.getString(okunanlar.getColumnIndex("ad"));
                soyad = okunanlar.getString(okunanlar.getColumnIndex("soyad"));
                tel = okunanlar.getInt(okunanlar.getColumnIndex("tel"));
                arrayAdapter2.add(ad + "\n" + soyad + "\n" + tel);//okunan veriler adaptere alt alta eklendi
            }
            cekilenlerigoster(arrayAdapter2);//adaptare doldurulan veriler ilgili metoda gonderildi

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "bilgiler cekilirken hata olustu", Toast.LENGTH_SHORT).show();

        }
        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sil(adGiris.getText().toString());
            }
        });

        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guncelle(telGiris.getText().toString());
            }
        });
    }


    private void cekilenlerigoster(final ArrayAdapter arrayAdapter2) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("getirilen kayıtlar");
        builderSingle.setNegativeButton("ÇIKIŞ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Name = (String) arrayAdapter2.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                builderInner.setCancelable(false);
                builderInner.setMessage(Name);
                builderInner.setTitle("Kayıtlar");
                builderInner.setPositiveButton("Degistir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                builderInner.setNegativeButton("Sil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();// açılı dialog’u kapatacak.
                            }


                        }

                );
                builderInner.show();

            }
        });
        builderSingle.show();
    }


    private void Guncelle(final String ad) {

        Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
        db1 = v1.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put("ad", ad);
        db1.update("kişiler", cv1, "ad" + "=?", new String[]{ad});

    }

    private void sil(final String adi) {//adı girilen kişiyi silecek. parametremiz ada gore oldugundan

        db1 = v1.getReadableDatabase();
        db1.delete("kişiler", "ad" + "=?", new String[]{adi});//ilk deger tablo adı,2.deger WHERE sorgumuz,3.parametre ?: yeni ad


    }
}











   












