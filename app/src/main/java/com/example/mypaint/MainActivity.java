package com.example.mypaint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ColorDialog.ColorDialogListner, StrokeWidthDialog.StrokeWidthDialogListner, BackgroundColorDialog.BackgroundColorDialogListner {

    public static PaintView paintView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        paintView = findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.emboss:
                paintView.emboss();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
            case R.id.share:
                if(paintView.getBitmap() != null) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File imgDir = new File(root + "/MyPaint");
                    imgDir.mkdir();
                    Random gen = new Random();
                    int num = 100000;
                    num = gen.nextInt(num);
                    String fname = "IMG_" + num + ".jpg";
                    File img = new File(imgDir, fname);
                    if (img.exists())
                        img.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(img);
                        paintView.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        Toast.makeText(MainActivity.this, "Image saved successfully. FilePath:" + img.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(img.getAbsolutePath()));
                        intent.setType("image/*");
                        startActivity(Intent.createChooser(intent, "Share Image"));
                    } catch (Exception e) {
                        Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                return true;
            case R.id.save:
                if(paintView.getBitmap() != null) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File imgDir = new File(root + "/MyPaint");
                    imgDir.mkdir();
                    Random gen = new Random();
                    int num = 100000;
                    num = gen.nextInt(num);
                    String fname = "IMG_" + num + ".jpg";
                    File img = new File(imgDir, fname);
                    if (img.exists())
                        img.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(img);
                        paintView.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        Toast.makeText(MainActivity.this, "Image saved successfully. FilePath:" + img.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                return true;
            case R.id.color:
                ColorDialog colorDialog = new ColorDialog();
                colorDialog.show(getSupportFragmentManager(),"colorDialog");
                return true;
            case R.id.eraser:
                    paintView.setCurrColorColor(paintView.getBackgroundColor());
                return true;
            case R.id.strokeWidth:
                StrokeWidthDialog strokeWidthDialog = new StrokeWidthDialog();
                strokeWidthDialog.show(getSupportFragmentManager(), "strokeWidthDialog");
                return true;
            case R.id.backgroundColor:
                BackgroundColorDialog backgroundColorDialog = new BackgroundColorDialog();
                backgroundColorDialog.show(getSupportFragmentManager(), "backgroundColorDialog");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeColor(int chosenColor) {
        paintView.setCurrColorColor(chosenColor);
    }

    @Override
    public void changeStrokeWidth(float chosenStrokeWidth) {
        paintView.setStrokeWidth(chosenStrokeWidth);
    }

    @Override
    public void changeBackgroundColor(int chosenColor) {
        paintView.setbackgroundColor(chosenColor);
    }

    public void getPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            getPermission();
        }
    }

}
