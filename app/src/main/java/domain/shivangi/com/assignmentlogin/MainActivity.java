package domain.shivangi.com.assignmentlogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    //reference specific portion of database
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private Button sign_up,sign_in;
    private EditText email_num, password;
    String number = null,email=null;
    boolean flag = false;
    TextInputLayout passwordWrapper,emailWrapper;
    String email_number,password_text;
    boolean flag1 = false,remem;
    CheckBox remember,terms;
    Session session;
    ImageButton imageButton;
    TextView termsAndCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);

        sign_up = (Button)findViewById(R.id.sign_up);
        sign_in = findViewById(R.id.sign_in);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        emailWrapper = findViewById(R.id.emailWrapper);
        remember = findViewById(R.id.remember_me);
        imageButton = findViewById(R.id.fingerButton);
        terms = findViewById(R.id.terms_and_service);
        termsAndCondition = findViewById(R.id.terms_and_service_textView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FingerScannerDialogActivity.class));
            }
        });

        termsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Put terms and condition activity.", Toast.LENGTH_SHORT).show();
            }
        });

        if(session.isEnabled()){
            terms.setChecked(true);
            remember.setChecked(true);
            if(session.getEmail() == null)
                emailWrapper.getEditText().setText(session.getNumber());
            else if(session.getNumber() == null)
                emailWrapper.getEditText().setText(session.getEmail());
            passwordWrapper.getEditText().setText(session.getPassword());
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("users");

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        EditText pass = passwordWrapper.getEditText();

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                remem = isChecked;
                session.setEnabled(isChecked);
                if(!isChecked) {
                    session.setPassword(null);
                    session.setPassword(null);
                    session.setPassword(null);
                }
            }
        });

        if(session.getFingerAuth())
            pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(session.getFingerAuth()) {
                        startActivity(new Intent(MainActivity.this, FingerScannerDialogActivity.class));
                        session.setFingerAuth(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                if(!terms.isChecked()){
                    Toast.makeText(MainActivity.this, "check terms and condition.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //email_number = email_num.getText().toString();
                email_number = emailWrapper.getEditText().getText().toString();
                if(email_number.isEmpty()){
                    Toast.makeText(MainActivity.this, "Login details can't be empty.", Toast.LENGTH_SHORT).show();
                    emailWrapper.setError("Login details can't be empty.");
                    return;
                }
                else {
                    if(isNumeric(email_number)) {
                        number = email_number;
                        email = null;
                        if(remem) {
                            session.setNumber(number);
                            session.setEmail(null);
                        }
                    }
                    else if(validateEmail(email_number)) {
                        email = email_number;
                        number = null;
                        if(remem) {
                            session.setEmail(email);
                            session.setNumber(null);
                        }
                    }
                    else {
                        emailWrapper.setError("Not a valid email address!");
                        Toast.makeText(MainActivity.this, "Please write a valid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                password_text = passwordWrapper.getEditText().getText().toString();
                if(password_text.isEmpty()){
                    passwordWrapper.setError("Login details can't be empty.");
                    Toast.makeText(MainActivity.this, "Login details can't be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(remem)
                    session.setPassword(password_text);
                emailWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);

                if(!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mMessagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        flag = false;
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            UserInformation post = postSnapshot.getValue(UserInformation.class);
                            if(email == null){
                                if(post.getMobile_num().equals(number))
                                    if(post.getPassword().equals(password_text)){
                                        flag = true;
                                        Toast.makeText(MainActivity.this, "Login Sucessfull", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this,AfterLoginActivity.class));
                                    }
                            }
                            else if(number == null){
                                if(post.getEmail().equals(email))
                                    if(post.getPassword().equals(password_text)){
                                        flag = true;
                                        Toast.makeText(MainActivity.this, "Login Sucessfull", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this,AfterLoginActivity.class));
                                }
                            }
                        }
                        if(flag == false) {
                            Toast.makeText(MainActivity.this, "Incorrect login or password.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Problem retrieving data.", Toast.LENGTH_SHORT).show();
                    }
                });
                session.setFingerAuth(true);
            }
        });
    }

    private boolean isNumeric(String text){
        try {
            Double.parseDouble(text);
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
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
