package com.ifs21028.projectdigitalent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.ifs21028.projectdigitalent.databinding.ActivityEditBinding;

import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.Inflater;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(EditActivity.this, DetailActivity.class);
            startActivity(intent);
            finish();
        });

        String nimSiswa = getIntent().getStringExtra("nim_siswa");

        Cursor cursor = databaseHelper.getDetailByNim(nimSiswa);
        cursor.moveToFirst();

        String nama = cursor.getString(1);
        String kelamin = cursor.getString(2);
        String nim = cursor.getString(0);
        String prodi = cursor.getString(3);
        String kelas = cursor.getString(4);
        String lahir = cursor.getString(5);
        String alamat = cursor.getString(6);

        binding.etNama.setText(nama);
        binding.etNim.setText(nim);
        binding.etProdi.setText(prodi);
        binding.etKelas.setText(kelas);
        binding.etLahir.setText(lahir);
        binding.etAlamat.setText(alamat);

        String nimLama = nim;

        if(kelamin.equals("laki-laki"))
            binding.rdLk.setChecked(true);
        else
            binding.rdPr.setChecked(true);

        binding.btnEdit.setOnClickListener(v -> {
            String editNama = binding.etNama.getText().toString();
            final AtomicReference<String> editKelamin = new AtomicReference<>("");
            String editNim = binding.etNim.getText().toString();
            String editProdi = binding.etProdi.getText().toString();
            String editKelas = binding.etKelas.getText().toString();
            String editLahir = binding.etLahir.getText().toString();
            String editAlamat = binding.etAlamat.getText().toString();

            // mengecek nilai radio button (kelamin)
            if(binding.rdLk.isChecked()) {
                editKelamin.set("laki-laki");
            } else {
                editKelamin.set("perempuan");
            }

            if(editNama.equals("")||editKelamin.get().equals("")||editProdi.equals("")|editKelas.equals("")||editLahir.equals("")||editAlamat.equals(""))
                Toast.makeText(EditActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            else {
                Boolean edit = databaseHelper.editBio(editNama, editKelamin.get(), editNim, editProdi, editKelas, editLahir, editAlamat);
                if (edit == true) {
                    Toast.makeText(EditActivity.this, "Berhasil mengubah data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditActivity.this, ViewActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "Gagal mengubah data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}