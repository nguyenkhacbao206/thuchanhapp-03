package com.example.thuchanh03;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int productId;
    private TextView tvName, tvPrice, tvQuantity, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        db = new DatabaseHelper(this);
        productId = getIntent().getIntExtra("id", -1);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvDesc = findViewById(R.id.tvDesc);

        Button btnIn = findViewById(R.id.btnImport);
        Button btnSell = findViewById(R.id.btnSell);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnIn.setOnClickListener(v -> {
            Product p = db.getProductById(productId);
            if (p == null) return;
            db.updateQuantity(productId, p.getQuantity() + 1);
            load();
        });
        btnSell.setOnClickListener(v -> {
            Product p = db.getProductById(productId);
            if (p == null) return;
            if (p.getQuantity() <= 0) {
                Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            db.updateQuantity(productId, p.getQuantity() - 1);
            load();
        });
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> {
            db.deleteProduct(productId);
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        });
        load();
    }

    private void load() {
        Product p = db.getProductById(productId);
        if (p == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvName.setText(p.getName() + " (" + p.getCategory() + ")"
                + (p.isActive() ? "" : " - Ngừng kinh doanh"));
        tvPrice.setText("Giá bán: " + MainActivity.money(p.getPrice()));
        String qty = "Số lượng: " + p.getQuantity() + "\nGiá trị tồn kho: "
                + MainActivity.money((long) p.getPrice() * p.getQuantity());
        if (p.isLowStock()) qty += "\nCảnh báo: sắp hết hàng!";
        tvQuantity.setText(qty);
        tvQuantity.setTextColor(p.isLowStock() ? 0xFFD32F2F : 0xFF000000);
        String d = p.getDescription();
        tvDesc.setText("Mô tả: " + (d == null || d.isEmpty() ? "(không có)" : d));
    }
}
