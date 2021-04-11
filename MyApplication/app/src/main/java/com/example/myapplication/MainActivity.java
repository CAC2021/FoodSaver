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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
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

import okhttp3.Call;
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
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.get("multipart/form-data; boundary=324ff93ks0kr021");

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

    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("data");
        System.out.println(data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            try {
                /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] imageBytes = bos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/
                //create a file to write bitmap data
                String filename = "photo.bmp";
                File f = new File(getApplicationContext().getCacheDir(), filename);
                f.createNewFile();

                Bitmap bitmap = imageBitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitMapData = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitMapData);
                fos.flush();
                fos.close();
                post("https://api3-production.up.railway.app/api/image/", f);
//                System.out.println(response);
                System.out.println("Worked :)");
            }
            catch (Exception e) {
                System.out.println("Fatal error.");
                System.exit(0);
            }
        }
    }

    public void post(String url, File f) throws IOException {

        RequestBody body = new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("file", "file.png",
                RequestBody.create(MediaType.parse("multipart/form-data"),
                  f))
              .build();

            Request request = new Request.Builder()
              .url(url)
              .addHeader("Content-Type", "application/json")
              .post(body)
              .build();

            Call call = client.newCall(request);
            Response response = call.execute();
        
            System.out.println(response);

//        RequestBody body = RequestBody.create(f, JSON);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        }
    }
}