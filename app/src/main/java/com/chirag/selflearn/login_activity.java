package com.chirag.selflearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {
    protected Button signup;
    protected Button login;
    boolean islogin=false;

    protected EditText username , password;

    protected FirebaseDatabase firebaseDatabase;
    protected DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);



        // initializing databaserefrence object of database.
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("user");



        //initializing all the buttons of layout.
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);


        //initializing all the edittext field .
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);


        // applying listener on sign up
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.cycle_interpolator, android.R.anim.bounce_interpolator);
                startActivity(new Intent(login_activity.this,SignUp.class));
            }
        });

        // applying listener on login.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code on clicking on login button.....

                if(username.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    if(username.getText().toString().isEmpty()){
                        username.setError("please enter valid username or don't leave it blank");
                    }
                    else
                        password.setError("please enter password or don't leave it blank");
                }
                else
                {
                  if(checkusernameandpassword(username.getText().toString(),password.getText().toString())){
                      
                  }
                }
            }
        });
    }

    private boolean checkusernameandpassword(final String username_data, final String password_data) {
        //code to check id and password is valid or not.

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                String username_data_string = dataSnapshot1.child("username").getValue().toString();
                    Toast.makeText(getApplicationContext(),dataSnapshot1.child("username").getValue().toString(),Toast.LENGTH_LONG).show();

                    if(username_data.equals(username_data_string)){
                    islogin=true;
                    break;
                }
                else {
                    islogin=false;
                    continue;
                }
                }
                if(islogin){
                    Toast.makeText(getApplicationContext(),"data found",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"data not found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error",databaseError.getMessage());
            }
        });



        return islogin;
    }

    private void changeactivitytodashboard() {

        //code to change activity to the dashboard layout.

    }

    private void addsharedpref()
    {
        //code to add data to the shared preferences.
    }

    private void senddatatodatabase()
    {
        // code to send the data to the database.
        // this code is in production now so it will managed by team.
    }

    private void loginfromaccount(String username_data, String password_data )
    {
        // code for login.
    }
}
