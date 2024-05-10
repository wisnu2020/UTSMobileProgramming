package neko.stationery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import neko.stationery.utils.Employee;
import neko.stationery.utils.SqliteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Account extends AppCompatActivity {
    protected Cursor cursor;
    SqliteHelper sqliteHelper;
    TextView textView1;
    EditText editText1, editText2;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sqliteHelper = new SqliteHelper(this);
        customTitleBar();
        initViews();
        refreshAccount();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
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
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
        this.getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
    }

    private void initViews() {
        textView1 = (TextView) findViewById(R.id.textView1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        fab = (FloatingActionButton) findViewById(R.id.button1);
    }

    private void refreshAccount() {
        String id = getIntent().getStringExtra(LoginActivity.TAG_ID);
        Employee employee = sqliteHelper.getEmployeeById(id);
        textView1.setText(employee.getId());
        editText1.setText(employee.getName());
        editText2.setText(employee.getEmail());
    }

    private void updateEmployee() {
        if (validate()) {
            String name = editText1.getText().toString();
            String email = editText2.getText().toString();

            try {
                if (!isEmailExist(email)) {
                    String id = getIntent().getStringExtra(LoginActivity.TAG_ID);
                    sqliteHelper.updateEmployee(new Employee(name, email), id);

                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    DashboardActivity.dashboardActivity.refreshList();
                    finish();;
                }
            } catch (SQLiteException e) {
                Toast.makeText(getApplicationContext(), "User already exist with same email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validate() {
        boolean valid = false;

        String name = editText1.getText().toString();
        String email = editText2.getText().toString();

        if (name.isEmpty()) {
            valid = false;
            editText1.setError("Please enter valid name!");
        } else {
            valid = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            editText2.setError("Please enter valid email!");
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean isEmailExist(String email) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT * FROM " + SqliteHelper.TABLE_EMPLOYEES
                        + " WHERE " + SqliteHelper.KEY_EMPLOYEE_EMAIL + " = '" + email
                        + "' AND NOT " + SqliteHelper.KEY_EMPLOYEE_EMAIL + " = '" + email + "'", null);
        return cursor != null && cursor.moveToFirst() && cursor.getCount() > 0;
    }
}
