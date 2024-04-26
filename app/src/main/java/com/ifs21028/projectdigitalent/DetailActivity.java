package com.ifs21028.projectdigitalent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ifs21028.projectdigitalent.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, ViewActivity.class);
            startActivity(intent);
            finish();
        });

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();

        String nimSiswa = intent.getStringExtra("nim_siswa");

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

        if(kelamin.equals("laki-laki"))
            binding.rdLk.setChecked(true);
        else
            binding.rdPr.setChecked(true);

        binding.btnEdit.setOnClickListener(v -> {
            Intent newIntent = new Intent(DetailActivity.this, EditActivity.class);
            newIntent.putExtra("nim_siswa", nimSiswa);
            startActivity(newIntent);
        });

        binding.btnHapus.setOnClickListener(v -> {
            Boolean isDeleted = databaseHelper.deleteBio(nim);
            if (isDeleted) {
                Toast.makeText(this, "Data biodata berhasil dihapus", Toast.LENGTH_SHORT).show();
                Intent delIntent = new Intent(DetailActivity.this, ViewActivity.class);
                startActivity(delIntent);
                finish();
            } else {
                Toast.makeText(this, "Gagal menghapus data biodata", Toast.LENGTH_SHORT).show();
            }
        });
    }
}