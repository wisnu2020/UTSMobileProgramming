package neko.stationery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import neko.stationery.utils.MyListView;
import neko.stationery.utils.Product;
import neko.stationery.utils.SqliteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    ListView listView;
    SqliteHelper sqliteHelper;
    public static DashboardActivity dashboardActivity;
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;
    MyListView adapter = null;
    ArrayList<Product> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardActivity = this;
        sqliteHelper = new SqliteHelper(this);
        customTitleBar();
        refreshList();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CreateProduct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return addSearchView(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item1) {
            logOut();
        } else if (id == R.id.item2) {
            openAccountActivity();
        } else if (id == R.id.item3) {
            openChangePasswordActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void customTitleBar() {
        this.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
    }

    public void refreshList() {
        getAllProduct();
        addProductAlertBuilder();
        adapter.notifyDataSetChanged();
    }

    private boolean addSearchView(Menu menu) {
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    private void logOut() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSession();
                        openLoginActivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirm Log Out");
        alert.show();
    }

    private void clearSession() {
        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openAccountActivity() {
        Intent intent = new Intent(getApplicationContext(), Account.class);
        intent.putExtra(LoginActivity.TAG_ID, getEmployeeIdPreference());
        startActivity(intent);
    }

    private void openChangePasswordActivity() {
        Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
        intent.putExtra(LoginActivity.TAG_ID, getEmployeeIdPreference());
        startActivity(intent);
    }

    private String getEmployeeIdPreference() {
        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TAG_ID, null);
    }

    public void getAllProduct() {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SqliteHelper.TABLE_PRODUCT, null);

        arrayList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String category = cursor.getString(2);
                String supplier = cursor.getString(3);
                String stock = cursor.getString(4);
                arrayList.add(new Product(id, name, category, supplier, stock));
            } while (cursor.moveToNext());
        }

        adapter = new MyListView(this, arrayList);

        listView = (ListView) findViewById(R.id.listView1);
        listView.setDivider(null);
        listView.setAdapter(adapter);
    }

    public void addProductAlertBuilder() {
        listView.setSelected(true);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selection  = arrayList.get(position);
                CharSequence[] dialogItem = {"See Product", "Update Product", "Delete Product"};

                builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Choose");
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openSeeProductActivity(selection);
                                break;
                            case 1:
                                openUpdateProductActivity(selection);
                                break;
                            case 2:
                                sqliteHelper.deleteProduct(selection);
                                refreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    public void openSeeProductActivity(Product selection) {
        Intent intent = new Intent(getApplicationContext(), SeeProduct.class);
        intent.putExtra("id", selection.getId());
        startActivity(intent);
    }

    public void openUpdateProductActivity(Product selection) {
        Intent intent = new Intent(getApplicationContext(), UpdateProduct.class);
        intent.putExtra("id", selection.getId());
        startActivity(intent);
    }
}
