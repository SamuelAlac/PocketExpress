package com.alac.pocketexpress;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alac.pocketexpress.entity.Staff;
import com.alac.pocketexpress.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    String username, password;
    String loginDate;
    Button btnLogin;
    TextView btnSignUpPage;
    DatabaseHelper db;
    boolean isVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUpPage = findViewById(R.id.btnSignupPage);


        btnLogin.setOnClickListener(v ->{
            username = txtUsername.getText().toString();
            password = txtPassword.getText().toString();
            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            loginDate = formatter.format(date);

            Staff staff = new Staff(username, password, loginDate);
            db = new DatabaseHelper(LoginActivity.this);

            //For Account Verification
            isVerified = checkAccount();
            if(isVerified){
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                long result = db.updateLoginDate(staff);
                main.putExtra("staffID", staff.getStaffID());
                startActivity(main);
                finish();
            }else{
                Toast.makeText(this, "ACCOUNT NOT FOUND", Toast.LENGTH_SHORT).show();
                //db.createStaff(staff); For registration just cuz
            }

        });

        btnSignUpPage.setOnClickListener(v ->{
            Intent signup = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(signup);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean checkAccount(){
        db = new DatabaseHelper(LoginActivity.this);
        username = txtUsername.getText().toString();
        password = txtPassword.getText().toString();
        Staff staff = new Staff(username, password);
        Cursor cursor = db.readStaffAccount(staff);

        if (cursor.getCount() > 0) {
            Toast.makeText(this, "LOGGING IN", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db = new DatabaseHelper(LoginActivity.this);
        db.deleteNullOrder();
    }
}