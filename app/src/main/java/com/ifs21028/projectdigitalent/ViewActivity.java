package com.ifs21028.projectdigitalent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.database.Cursor;

import com.google.android.material.appbar.MaterialToolbar;
import com.ifs21028.projectdigitalent.databinding.ActivityViewBinding;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private ListView lvItem;
    ActivityViewBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        databaseHelper = new DatabaseHelper(this);
        ArrayList<String> nimSiswa = getAllNimMahasiswa();
        ArrayList<String> namaSiswa = new ArrayList<>();

        for(String nim : nimSiswa) {
            String nama = databaseHelper.getNameByNim(nim);
            namaSiswa.add(nama);
        }

        lvItem = binding.listView;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1, namaSiswa);

        lvItem.setAdapter(adapter);

        lvItem.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(ViewActivity.this, "Memilih: " + namaSiswa.get(position), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ViewActivity.this, DetailActivity.class);
            intent.putExtra("nim_siswa", nimSiswa.get(position));
            startActivity(intent);
        });
    }

    private ArrayList<String> getAllNimMahasiswa() {
        ArrayList<String> namaMahasiswa = new ArrayList<>();
        Cursor cursor = databaseHelper.getBio();

        if (cursor != null && cursor.moveToFirst()) {
            int indexNama = cursor.getColumnIndex("nim");
            do {
                String nama = cursor.getString(indexNama);
                namaMahasiswa.add(nama);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        return namaMahasiswa;
    }
}
