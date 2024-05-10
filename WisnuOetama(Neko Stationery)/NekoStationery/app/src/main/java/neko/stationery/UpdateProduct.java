package neko.stationery;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import neko.stationery.utils.Product;
import neko.stationery.utils.SqliteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateProduct extends AppCompatActivity {
    SqliteHelper sqliteHelper;
    EditText text1, text3, text4;
    FloatingActionButton fab;
    String[] category = {"Choose Category", "Pen", "Pencil", "Highlighter", "Loose Leaf", "Ruler", "Notes"};
    Spinner text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        sqliteHelper = new SqliteHelper(this);
        customTitleBar();
        initViews();
        addSpinnerCategory();
        setEditTexts();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
    }

    private void initViews() {
        text1 = (EditText) findViewById(R.id.editText2);
        text2 = (Spinner) findViewById(R.id.editText3);
        text3 = (EditText) findViewById(R.id.editText4);
        text4 = (EditText) findViewById(R.id.editText5);
        fab = (FloatingActionButton) findViewById(R.id.button1);
    }

    private void addSpinnerCategory() {
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, category) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view;
                if (position == 0) {
                    TextView textView = new TextView(getContext());
                    textView.setVisibility(View.GONE);
                    textView.setHeight(0);
                    view = textView;
                } else {
                    view = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(false);
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text2.setAdapter(arrayAdapter);
    }

    private void setEditTexts() {
        String id = getIntent().getStringExtra("id");
        Product product = sqliteHelper.getProductById(id);
        text1.setText(product.getName());
        text3.setText(product.getSupplier());
        text4.setText(product.getStock());
        setSpinnerText(product);
    }

    private void setSpinnerText(Product product) {
        for (int i = 0; i < text2.getAdapter().getCount(); i++) {
            if (text2.getAdapter().getItem(i).toString().contains(product.getCategory())) {
                text2.setSelection(i);
            }
        }
    }

    private void updateProduct() {
       if (validate()) {
           String name = text1.getText().toString();
           String category = text2.getSelectedItem().toString();
           String supplier = text3.getText().toString();
           String stock = text4.getText().toString();

           String id = getIntent().getStringExtra("id");
           sqliteHelper.updateProduct(new Product(name, category, supplier, stock), id);

           Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
           DashboardActivity.dashboardActivity.refreshList();
           finish();;
       }
    }

    private boolean validate() {
        boolean valid = false;

        String name = text1.getText().toString();
        String category = text2.getSelectedItem().toString();
        String supplier = text3.getText().toString();
        String stock = text4.getText().toString();

        if (name.isEmpty() || category.isEmpty() || supplier.isEmpty() || stock.isEmpty()) {
            valid = false;
            Toast.makeText(getApplicationContext(), "Please, fill in all fields!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }

        if (category.equals("Choose Category")) {
            valid = false;
            Toast.makeText(getApplicationContext(), "Please, choose category!", Toast.LENGTH_SHORT).show();
        } else {
            valid = true;
        }
        return valid;
    }
}