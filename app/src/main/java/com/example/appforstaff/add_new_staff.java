package com.example.appforstaff;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class add_new_staff extends AppCompatActivity {
    Intent Mainactiv;
    EditText name, phone , email;
    ImageView img;
    String encodedImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_staff);

        Mainactiv = new Intent(this,MainActivity.class);
        name = findViewById(R.id.name);
        phone =  findViewById(R.id.phone);
        email =  findViewById(R.id.email);
        img = findViewById(R.id.imageView2);

    }

    public void ImageClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    img.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });
    private String encodeImage(Bitmap bitmap) {
        int prevW = 300;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    public void btn_onclick_to_main_act(View view){
        add_staff(name.getText().toString(), phone.getText().toString(), email.getText().toString(), encodedImage);
        back();
    }
    public void back(){startActivity(Mainactiv);}


    public void add_staff(String name, String phone , String email , String encodedImage){
        Retrofit retrofit = new Retrofit.Builder().baseUrl ("https://ngknn.ru:5001/NGKNN/????????????????/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Staff modal = new Staff (name,phone,email,encodedImage);
        Call<Staff> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(@NonNull Call<Staff> call, @NonNull Response<Staff> response) {
                Toast.makeText(add_new_staff.this, "???????????? ??????????????????",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Toast.makeText(add_new_staff.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}