package com.ejemplo.files001;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    EditText textBox;
    File file;
    static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textBox = (EditText) findViewById(R.id.editText);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

    }

    public void onClickSave(View view) {
        if(isExternalStorageWritable()){
            try{
                file = new File(Environment.getExternalStorageDirectory(), textBox.getText().toString());
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(textBox.getText().toString().getBytes());
                fos.close();
                Toast.makeText(this, "Archivo Guardado.", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "No se pudo encontrar memoria externa.", Toast.LENGTH_SHORT).show();
        }
        textBox.setText("");
    }

    public void onClickLoad(View view) {
        if(isExternalStorageReadable()){
            if(file!=null){
                try {
                    File file = new File(this.file.toString());
                    FileInputStream fileInputStream = new FileInputStream(file);

                    InputStreamReader isr = new InputStreamReader(fileInputStream);
                    char[] inputBuffer = new char[READ_BLOCK_SIZE];
                    String s = "";
                    int charRead;
                    while ((charRead = isr.read(inputBuffer)) > 0) {
                        //---convert the chars to a String---
                        String readString =
                                String.copyValueOf(inputBuffer, 0,
                                        charRead);
                        s += readString;
                        inputBuffer = new char[READ_BLOCK_SIZE];
                    }
                    textBox.setText(s);
                    Toast.makeText(getBaseContext(), "Archivo recuperado con exito!",
                            Toast.LENGTH_SHORT).show();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }else {
                Toast.makeText(this, "Archivo no guardado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {}
                return;
            }
        }
    }

}


