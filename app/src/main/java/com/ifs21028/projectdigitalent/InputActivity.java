package com.ifs21028.projectdigitalent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.ifs21028.projectdigitalent.databinding.ActivityInputBinding;

import java.util.concurrent.atomic.AtomicReference;

public class InputActivity extends AppCompatActivity {

    ActivityInputBinding binding;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(InputActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        databaseHelper = new DatabaseHelper(this);

        binding.btnKirim.setOnClickListener(v -> {
            String nama = binding.etNama.getText().toString();
            final AtomicReference<String> kelamin = new AtomicReference<>("");
            String nim = binding.etNim.getText().toString();
            String prodi = binding.etProdi.getText().toString();
            String kelas = binding.etKelas.getText().toString();
            String lahir = binding.etLahir.getText().toString();
            String alamat = binding.etAlamat.getText().toString();

            // mengecek nilai radio button (kelamin)
            if(binding.rdLk.isChecked()) {
                kelamin.set("laki-laki");
            } else {
                kelamin.set("perempuan");
            }

            if(nama.equals("")||kelamin.get().equals("")||nim.equals("")||prodi.equals("")||kelas.equals("")||lahir.equals("")||alamat.equals(""))
                Toast.makeText(InputActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            else {
                Boolean checkNim = databaseHelper.checkNim(nim);
                if (checkNim == false) {
                    Boolean insert = databaseHelper.insertBio(nama, kelamin.get(), nim, prodi, kelas, lahir, alamat);
                    if (insert == true) {
                        Toast.makeText(InputActivity.this, "Berhasil menambahkan data", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(InputActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InputActivity.this, "NIM sudah dipakai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}