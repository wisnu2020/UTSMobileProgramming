package neko.stationery;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import neko.stationery.utils.Product;
import neko.stationery.utils.SqliteHelper;

public class SeeProduct extends AppCompatActivity {
    SqliteHelper sqliteHelper;
    TextView text1, text2, text3, text4, text5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_product);

        sqliteHelper = new SqliteHelper(this);
        customTitleBar();
        initViews();
        setTextViews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void customTitleBar() {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
        this.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
    }

    private void initViews() {
        text1 = (TextView) findViewById(R.id.textView1);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);
        text5 = (TextView) findViewById(R.id.textView5);
    }

    private void setTextViews() {
        String id = getIntent().getStringExtra("id");
        Product product = sqliteHelper.getProductById(id);
        text1.setText(product.getId());
        text2.setText(product.getName());
        text3.setText(product.getCategory());
        text4.setText(product.getSupplier());
        text5.setText(product.getStock());
    }
}