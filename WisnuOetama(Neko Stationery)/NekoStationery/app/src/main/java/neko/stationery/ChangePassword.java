package neko.stationery;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import neko.stationery.utils.Employee;
import neko.stationery.utils.SqliteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChangePassword extends AppCompatActivity {
    SqliteHelper sqliteHelper;
    EditText editText1, editText2, editText3;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sqliteHelper = new SqliteHelper(this);
        customTitleBar();
        initViews();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmployeePassword();
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
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        fab = (FloatingActionButton) findViewById(R.id.button1);
    }

    private void changeEmployeePassword() {
        if (!isFieldEmpty() && validate()) {
            String password = editText2.getText().toString();

            String id = getIntent().getStringExtra(LoginActivity.TAG_ID);
            sqliteHelper.updateEmployeePassword(new Employee(password), id);

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            DashboardActivity.dashboardActivity.refreshList();
            finish();
        }
    }

    public boolean isFieldEmpty() {
        boolean valid = true;

        String currentPassword = editText1.getText().toString();
        String newPassword = editText2.getText().toString();
        String confirmNewPassword = editText3.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please, fill in all fields!", Toast.LENGTH_SHORT).show();
        } else {
            valid = false;
        }
        return valid;
    }

    public boolean validate() {
        boolean valid = false;

        String currentPassword = editText1.getText().toString();
        String newPassword = editText2.getText().toString();
        String confirmNewPassword = editText3.getText().toString();

        String id = getIntent().getStringExtra(LoginActivity.TAG_ID);
        String password = sqliteHelper.getEmployeePasswordById(id);
        
        if (!currentPassword.equals(password)) {
            Toast.makeText(getApplicationContext(), "Your current password is wrong!", Toast.LENGTH_SHORT).show();
        } else {
            if (newPassword.equals(currentPassword)) {
                Toast.makeText(getApplicationContext(), "Your new password same with current password!", Toast.LENGTH_SHORT).show();
            } else {
                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(getApplicationContext(), "Confirm new password is wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    valid = true;
                }
            }
        }
        return valid;
    }
 }