package com.nishatoma.firebasedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{


    //Include the real time database by creating a DataBaseReference object!
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mFirebaseBtn;
        final EditText mEditText;
        final EditText mEditTextEmail;

        mEditText = findViewById(R.id.editTextName);
        mEditTextEmail = findViewById(R.id.editTextEmail);
        mFirebaseBtn = findViewById(R.id.firebase_btn);
        //This line gets a reference to the root directory of our database
        //On the Firebase server
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //1)Create a child for the root directory reference
                //2)Assign a value to that child
                String name = mEditText.getText().toString().trim();
                String email = mEditTextEmail.getText().toString().trim();
                if (!name.isEmpty() && !email.isEmpty() && email.contains("@"))
                {


                    HashMap<String, String> dataMap = new HashMap<>();

                    dataMap.put("Name", name);
                    dataMap.put("Email", email);
                    mEditText.setText("");
                    mEditTextEmail.setText("");
                    mEditText.requestFocus();
                    closeKeyboard();
                    mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "Sign Up: Success", Toast.LENGTH_SHORT).show();

                            } else
                            {
                                displayErrorMessage(task.getException());
                            }
                        }
                    });


                } else
                {
                    String message = "Email/Username are invalid.";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void displayErrorMessage(Exception e)
    {
        String msg;
        if (e.getMessage().contains("java"))
        {
            msg = e.getMessage().substring(e.getMessage().indexOf(" "));
        } else
        {
            msg = e.getMessage();
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void closeKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
