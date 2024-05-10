package neko.stationery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    AppCompatButton buttonLogin;
    public final static String my_shared_preferences = "my_shared_preferences";
    public final static String session_status = "session_status";
    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_PASSWORD = "password";
    SqliteHelper sqliteHelper;
    SharedPreferences sharedPreferences;
    Boolean session = false;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitleBar();
        setContentView(R.layout.activity_login);

        sqliteHelper = new SqliteHelper(this);
        checkSession();
        initCreateAccountTextView();
        initViews();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void hideTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
    }

    private void checkSession() {
        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(session_status, false);
        email = sharedPreferences.getString(TAG_EMAIL, null);
        password = sharedPreferences.getString(TAG_PASSWORD, null);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                editTextEmail.getText().clear();
                editTextPassword.getText().clear();
                textInputLayoutEmail.setError(null);
                textInputLayoutPassword.setError(null);
            }
        });
    }

    private void initViews() {
        editTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        editTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
    }

    private void login(){
        if (validate()) {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            Employee currentEmployee = sqliteHelper.authenticate(new Employee(null, null, email, password));

            if (currentEmployee != null) {
                createSession(currentEmployee);
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Welcome to Neko Stationery", Toast.LENGTH_SHORT).show();
                DashboardActivity.dashboardActivity.refreshList();
                finish();

            } else {
                Snackbar.make(buttonLogin, "Failed to sign in, please try again", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean validate() {
        boolean valid = false;

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email");
            textInputLayoutEmail.requestFocus();
        } else {
            valid = true;
            editTextEmail.setError(null);
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

    private void createSession(Employee employee) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(session_status, true);
        editor.putString(TAG_ID, employee.getId());
        editor.putString(TAG_EMAIL, employee.getEmail());
        editor.putString(TAG_PASSWORD, employee.getPassword());
        editor.apply();
    }

}