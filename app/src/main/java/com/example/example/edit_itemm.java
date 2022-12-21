package com.example.example;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class edit_itemm extends AppCompatActivity {

    String encodedImage;
    EditText title;
    EditText cost;
    EditText stockAvailability;
    EditText availabilityInTheStore;
    EditText description;
    EditText rewiews;
    ImageView imagesq;
    ImageButton imageButtonadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itemm);

        title = findViewById(R.id.txt_title);
        cost = findViewById(R.id.txt_cost);
        stockAvailability = findViewById(R.id.txt_stock);
        availabilityInTheStore = findViewById(R.id.txt_store);
        description = findViewById(R.id.txt_description);
        rewiews = findViewById(R.id.txt_rewiews);
        imagesq = findViewById(R.id.imageGa);
        imageButtonadd = findViewById(R.id.btn_addnew_game);
        imageButtonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (name.getText().toString().isEmpty() && phone.getText().toString().isEmpty() && email.getText().toString().isEmpty()) {
                //    Toast.makeText(add_new_staff.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                //    return;
                //}
                add_staff(title.toString(),cost.toString() ,stockAvailability.toString(),availabilityInTheStore.toString(),description.toString(),rewiews.toString(), encodedImage);
            }
        });
        //открытие
        imagesq.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);        });


    }

    //отдельный метод для открытия
    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imagesq.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });

    //Из строки в изображение
    private Bitmap getImgBitmap(String encodedImg) {
        if (encodedImg != null) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    //Изображение в строку
    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
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
    public void add_staff(String title, String cost, String stockAvailability, String availabilityInTheStore, String description, String rewiews, String image){

        Retrofit retrofit = new Retrofit.Builder().baseUrl ("https://ngknn.ru:5001/NGKNN/зеленцовдр/Api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitApi retrofitAPI = retrofit.create(RetrofitApi.class);
        Game modal = new Game (3,title,1,1,1,description,rewiews,encodedImage);
        Call<Game> call = retrofitAPI.postData(modal);
        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(@NonNull Call<Game> call, @NonNull Response<Game> response) {
                Toast.makeText(edit_itemm.this, "Данные добавлены",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                Toast.makeText(edit_itemm.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}