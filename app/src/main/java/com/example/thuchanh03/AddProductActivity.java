package com.example.thuchanh03;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {
    private EditText edtName, edtPrice, edtQuantity, edtDesc;
    private Spinner spCategory;
    private Switch swActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtDesc = findViewById(R.id.edtDesc);
        spCategory = findViewById(R.id.spCategory);
        swActive = findViewById(R.id.swActive);
        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"}));
        Button btn = findViewById(R.id.btnSave);
        btn.setOnClickListener(v -> save());
    }

    private void save() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String qtyStr = edtQuantity.getText().toString().trim();
        if (name.isEmpty()) {
            edtName.setError("Tên sản phẩm không được để trống");
            return;
        }
        int price, qty;
        try {
            price = Integer.parseInt(priceStr);
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            edtPrice.setError("Giá bán phải là số lớn hơn 0");
            return;
        }
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            edtQuantity.setError("Số lượng phải là số nguyên không âm");
            return;
        }
        Product p = new Product(name, price, qty, spCategory.getSelectedItem().toString(),
                swActive.isChecked(), edtDesc.getText().toString().trim());
        long id = new DatabaseHelper(this).insertProduct(p);
        if (id > 0) {
            Toast.makeText(this, "Đã lưu sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
