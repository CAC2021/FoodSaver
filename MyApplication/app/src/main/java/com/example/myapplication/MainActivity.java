package com.example.myapplication;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_log)
                .build();
        final Button camButton = (Button) findViewById(R.id.button_camera);
        imageView = findViewById(R.id.cam_image_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
           startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("data");
        System.out.println(data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            System.out.println("b4");
            try {
                String filename = "photo.bmp";
                File f = new File(getApplicationContext().getCacheDir(), filename);
                f.createNewFile();
                System.out.println("filename"+f);
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                FileOutputStream fos = new FileOutputStream(f);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 0, fos);
                System.out.println(imageBitmap);
//                byte[] bitMapData = bos.toByteArray();
//                System.out.println(bitMapData.length);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(bitMapData, 0,bitMapData.length); //image
//                ImageView image = (ImageView) findViewById(R.id.picview);
//                image.setImageBitmap(decodedByte);
                fos.flush();
                fos.close();
                upload("https://api3-production.up.railway.app/api/image/" ,f);


//                try {
//                    fos.write(bitMapData);
//                    System.out.println("good");
//                }
//                catch(Exception e){
//                    System.out.println("Bad");
//                }

                System.out.println("Worked :)");
            }
            catch (Exception e) {
                System.out.println("Fatal error.");
                System.exit(0);
            }
        }
    }
    public void upload(String url, File file) throws IOException {
        System.out.println("sdf");
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("multipart/form-data")))
                .build();
        System.out.println(formBody);
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();
//        System.out.println(response);
    }
    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }
}