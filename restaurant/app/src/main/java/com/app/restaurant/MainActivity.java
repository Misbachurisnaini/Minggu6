package com.app.restaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.praktikum_mod.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText nama,alamat,review;
    RadioGroup jenis1,jenis2;
    SeekBar rating;
    TextView pointrating;
    CheckBox toilet, musholla, playground;
    Button submit,load;
    int rate=1;
    String jenisrestaurant;
    AlertDialog.Builder builder;
    String NAMA_FILE = "DATARESTO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        jenis1 = findViewById(R.id.jenis1);
        jenis2 = findViewById(R.id.jenis2);
        toilet = findViewById(R.id.toilet);
        musholla = findViewById(R.id.musholla);
        playground = findViewById(R.id.playground);
        review = findViewById(R.id.review);
        rating = findViewById(R.id.rating);
        pointrating = findViewById(R.id.pointrating);
        submit = findViewById(R.id.submit);
        load = findViewById(R.id.load);
        jenis1.setOnCheckedChangeListener(rgListener1);
        jenis2.setOnCheckedChangeListener(rgListener2);
        builder = new AlertDialog.Builder(this);
        rating.setMax(5);
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rate=i+1;
                pointrating.setText(String.valueOf(rate));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });

        submit.setOnClickListener(view -> submitData());
        load.setOnClickListener(view -> loadData());
    }

    public void getJenis(int i){
        RadioButton temp = findViewById(i);
        jenisrestaurant = temp.getText().toString();
    }

    public void submitData(){
        if(!nama.getText().toString().isEmpty()&&
            !alamat.getText().toString().isEmpty()&&
            !(jenisrestaurant==null)&&
            !review.getText().toString().isEmpty()){
            String fasilitas="";
            if(toilet.isChecked()){
                fasilitas+="Toilet ";
            }
            if(musholla.isChecked()){
                fasilitas+="Musholla ";
            }
            if(playground.isChecked()){
                fasilitas+="Playground ";
            }
            String data = "Nama : "+nama.getText().toString()+"\n"
                    +"Alamat : "+alamat.getText().toString()+"\n"
                    +"Jenis : "+jenisrestaurant+"\n"
                    +"Fasilitas : "+(fasilitas==""?"-":fasilitas)+"\n"
                    +"Review : "+review.getText().toString()+"\n"
                    +"Rating : "+rate;
            FileOutputStream fos = null;

            try {
                fos=openFileOutput(NAMA_FILE,MODE_PRIVATE);
                fos.write(data.getBytes());
                File fileDir = new File(getFilesDir(),NAMA_FILE);
                Toast.makeText(this, "Data Saved at "+fileDir, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            Toast.makeText(this, "Semua Field Harus Diisi", Toast.LENGTH_LONG).show();
        }
    }
    public void loadData(){
        try {
            FileInputStream fin = openFileInput(NAMA_FILE);
            int c;
            String temp = "";
            while ((c=fin.read())!=-1){
                temp+=Character.toString((char) c);
            }
            builder.setTitle("Data Restaurant")
                    .setMessage(temp)
                    .setPositiveButton("OK", (dialog, which) -> new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public RadioGroup.OnCheckedChangeListener rgListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            jenis2.setOnCheckedChangeListener(null);
            jenis2.clearCheck();
            jenis2.setOnCheckedChangeListener(rgListener2);
            getJenis(i);
        }
    };

    public RadioGroup.OnCheckedChangeListener rgListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            jenis1.setOnCheckedChangeListener(null);
            jenis1.clearCheck();
            jenis1.setOnCheckedChangeListener(rgListener1);
            getJenis(i);
        }
    };
}