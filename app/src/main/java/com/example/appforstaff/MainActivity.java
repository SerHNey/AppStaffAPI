package com.example.appforstaff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private AdaptStaff sAdapter;
    private List<Staff> staffList = new ArrayList<>();

    ListView list;
    Intent add_new_staff;
    Intent item_gridd;
    AdaptStaff adapter;
    Spinner sort;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView ivProducts = findViewById(R.id.gridviewlist);//Находим лист в который будем класть наши объекты
        sAdapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList); //Создаем объект нашего адаптера
        ivProducts.setAdapter(sAdapter); //Cвязывает подготовленный список с адаптером

        new GetProducts().execute(); //Подключение к нашей API в отдельном потоке


        String[] sort_name = { "ФИО", "Почте", "Телефону"};
        sort = findViewById(R.id.sort);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sort_name);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(spinner_adapter);

        list = findViewById(R.id.gridviewlist);
        item_gridd = new Intent(this,item_grid.class);
        add_new_staff = new Intent(this, com.example.appforstaff.add_new_staff.class);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 );


        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Collections.sort(staffList, new Comparator<Staff>() {
                            @Override
                            public int compare(Staff staff, Staff t1) {
                                return staff.name.compareTo(t1.name);
                            }
                        });
                        adapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
                        list.setAdapter(adapter);
                        break;
                    case 1:
                        Collections.sort(staffList, new Comparator<Staff>() {
                            @Override
                            public int compare(Staff staff, Staff t1) {
                                return staff.email.compareTo(t1.email);
                            }
                        });
                        adapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
                        list.setAdapter(adapter);
                        break;
                    case 2:
                        Collections.sort(staffList, new Comparator<Staff>() {
                            @Override
                            public int compare(Staff staff, Staff t1) {
                                return staff.phone.compareTo(t1.phone);
                            }
                        });
                        adapter = new AdaptStaff(MainActivity.this, (ArrayList<Staff>) staffList);
                        list.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Staff item = (Staff) list.getItemAtPosition(i);

                item_gridd.putExtra("name", item.name);
                item_gridd.putExtra("id",item.id);
                item_gridd.putExtra("email",item.phone);
                item_gridd.putExtra("phone",item.email);
                if(item.imageB != null) {
                    item_gridd.putExtra("image",encodeImage(item.imageB));
                }
                startActivity(item_gridd);
            }
        }
        );

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        MenuItem menuItem2 = menu.findItem(R.id.action_add);
        SearchView  searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void onclick_view_add_staff(View view){startActivity(add_new_staff);}
    public void UpdateList(View view){new GetProducts().execute();}


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



    private class GetProducts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/НаумовСА/api/Staffs");//Строка подключения к нашей API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //вызываем нашу API

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);//кладет строковое значение в потоке
                }
                return result.toString();

            } catch (Exception exception) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONArray tempArray = new JSONArray(s);//преоброзование строки в json массив
                for (int i = 0;i<tempArray.length();i++)
                {

                    JSONObject productJson = tempArray.getJSONObject(i);//Преобразование json объекта в нашу структуру
                    Staff tempProduct = new Staff(

                            productJson.getString("name"),
                            productJson.getString("phone"),
                            productJson.getString("email"),

                            productJson.getString("id"),

                            getImageBitmap(productJson.getString("image")));
                    staffList.add(tempProduct);
                    sAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored) {


            }
        }
        private Bitmap getImageBitmap(String encodedImg) {
            if (encodedImg != null) {
                byte[] bytes = new byte[0];
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    bytes = Base64.getDecoder().decode(encodedImg);
                }
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            return BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher_foreground);
        }
    }
}