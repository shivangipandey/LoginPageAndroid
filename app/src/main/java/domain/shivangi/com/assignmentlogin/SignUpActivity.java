package domain.shivangi.com.assignmentlogin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout email,user_name,password,confirm_password,mobile_num,full_name,dob,address;
    private Button signUp;

    private FirebaseDatabase mFirebaseDatabase;
    //reference specific portion of database
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users");

        signUp = findViewById(R.id.signUp);
        email = findViewById(R.id.email);
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        mobile_num = findViewById(R.id.mobile_number);
        full_name = findViewById(R.id.full_name);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);

        confirm_password.setEnabled(false);

        EditText pass = password.getEditText();

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirm_password.setEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = email.getEditText().getText().toString();
                if(email_text.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email can't be empty.", Toast.LENGTH_SHORT).show();
                    email.setError("Email can't be empty.");
                    return;
                }
                else if(!validateEmail(email_text)) {
                    Toast.makeText(SignUpActivity.this, "Write valid email address", Toast.LENGTH_SHORT).show();
                    email.setError("Write valid email address");
                    return;
                }

                String user_name_text = user_name.getEditText().getText().toString();
                if(user_name_text.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "User Name can't be empty.", Toast.LENGTH_SHORT).show();
                    user_name.setError("User Name can't be empty.");
                    return;
                }
                String password_text = password.getEditText().getText().toString();
                if(password_text.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "password can't be empty.", Toast.LENGTH_SHORT).show();
                    password.setError("password can't be empty.");
                    return;
                }
                String confirm_pass = confirm_password.getEditText().getText().toString();
                if(confirm_pass.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "confirm password can't be empty.", Toast.LENGTH_SHORT).show();
                    confirm_password.setError("confirm password can't be empty.");
                    return;
                }
                else if(!password_text.equals(confirm_pass)){
                    Toast.makeText(SignUpActivity.this, "passwor doesn't match.", Toast.LENGTH_SHORT).show();
                    confirm_password.setError("password doesn't match.");
                    return;
                }
                String mob_num = mobile_num.getEditText().getText().toString();
                if(mob_num.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "mob_num can't be empty.", Toast.LENGTH_SHORT).show();
                    mobile_num.setError("mob_num can't be empty.");
                    return;
                }
                String full_nameText = full_name.getEditText().getText().toString();
                if(full_nameText.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "full name can't be empty.", Toast.LENGTH_SHORT).show();
                    full_name.setError("full name can't be empty.");
                    return;
                }
                String dob_text = dob.getEditText().getText().toString();
                if(dob_text.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Date of birth can't be empty.", Toast.LENGTH_SHORT).show();
                    dob.setError("Date of birth can't be empty.");
                    return;
                }
                String address_text = address.getEditText().getText().toString();

                email.setErrorEnabled(false);user_name.setErrorEnabled(false);
                password.setErrorEnabled(false);confirm_password.setErrorEnabled(false);
                mobile_num.setErrorEnabled(false);full_name.setErrorEnabled(false);
                dob.setErrorEnabled(false);

                UserInformation users = new UserInformation(email_text,user_name_text,password_text,mob_num,full_nameText,dob_text,address_text);
                mMessagesDatabaseReference.push().setValue(users);

                Toast.makeText(SignUpActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();

                if(!isNetworkAvailable()) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SignUpActivity.this, AfterLoginActivity.class));
                    finish();
                }
            }
        });
    }
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
