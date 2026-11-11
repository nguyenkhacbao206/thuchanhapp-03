package com.example.thuchanh03;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvStats;
    private ListView listView;
    private ProductAdapter adapter;
    private final List<Product> products = new ArrayList<>();

    static String money(long v) {
        return new DecimalFormat("#,###").format(v).replace(',', '.') + " đ";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        tvStats = findViewById(R.id.tvStats);
        listView = findViewById(R.id.listProducts);
        listView.setEmptyView(findViewById(R.id.tvEmpty));
        adapter = new ProductAdapter(this, products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, ProductDetailActivity.class);
            i.putExtra("id", products.get(position).getId());
            startActivity(i);
        });
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        products.clear();
        products.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();
        tvStats.setText("Tổng sản phẩm: " + products.size()
                + "\nTổng giá trị kho: " + money(db.getTotalValue())
                + "\nSắp hết hàng: " + db.countLowStock());
    }

    private static class ProductAdapter extends BaseAdapter {
        private final Context ctx;
        private final List<Product> data;

        ProductAdapter(Context ctx, List<Product> data) {
            this.ctx = ctx;
            this.data = data;
        }

        @Override public int getCount() { return data.size(); }
        @Override public Object getItem(int i) { return data.get(i); }
        @Override public long getItemId(int i) { return data.get(i).getId(); }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.item_product, parent, false);
            }
            Product p = data.get(pos);
            ((TextView) convertView.findViewById(R.id.tvName)).setText(p.getName());
            ((TextView) convertView.findViewById(R.id.tvPrice)).setText(money(p.getPrice()));
            ((TextView) convertView.findViewById(R.id.tvCategory)).setText(p.getCategory());
            TextView q = convertView.findViewById(R.id.tvQuantity);
            q.setText("SL: " + p.getQuantity());
            q.setTextColor(p.isLowStock() ? Color.RED : Color.BLACK);
            convertView.findViewById(R.id.tvLow).setVisibility(p.isLowStock() ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }
}
