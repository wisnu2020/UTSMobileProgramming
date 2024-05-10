package neko.stationery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import neko.stationery.utils.Employee;
import neko.stationery.utils.SqliteHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText editTextName, editTextEmail, editTextPassword;
    TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutPassword;
    AppCompatButton buttonRegister;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitleBar();
        setContentView(R.layout.activity_register);

        sqliteHelper = new SqliteHelper(this);
        initTextViewLogin();
        initViews();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void hideTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
    }

    private void initTextViewLogin() {
        TextView textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        editTextName = (TextInputEditText) findViewById(R.id.editTextName);
        editTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
    }

    private void register() {
        if (validate()) {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!sqliteHelper.isEmailExists(email)) {
                sqliteHelper.addEmployee(new Employee(null, name, email, password));
                Toast.makeText(getApplicationContext(), "Successful Register", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(buttonRegister, "User already exist with same email", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public boolean validate() {
        boolean valid = false;

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (name.isEmpty()) {
            valid = false;
            textInputLayoutName.setError("Please enter valid name!");
        } else {
            valid = true;
            textInputLayoutName.setError(null);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        if (password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter valid password!");
        } else {
            valid = true;
            textInputLayoutPassword.setError(null);
        }

        return valid;
    }
}