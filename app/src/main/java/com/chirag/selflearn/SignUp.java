package com.chirag.selflearn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    LinearLayout linearLayout1,linearLayout2;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    EditText first , second , email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        linearLayout1=findViewById(R.id.name_input_layout);
        linearLayout2=findViewById(R.id.email_input_layout);

        first=findViewById(R.id.first);
        second=findViewById(R.id.last);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("user");

    }


    // when first back button will press.
    public void gobackfirst(View view) {

        finish();
        startActivity(new Intent(SignUp.this,login_activity.class));

    }

    //when next button will be clicked
    public void gonext(View view) {


        if(first.getText().toString().isEmpty()||second.getText().toString().isEmpty()){
            if(first.getText().toString().isEmpty()) first.setError("please enter first name ");
            else second.setError("please enter last name");
        }
        else {
                linearLayout1.setVisibility(View.INVISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);

        }

    }
    private int  checkpassword(String pass) {


        int right=0;
        char[] pass_data = pass.toCharArray();
        if(pass.length()<8) {password.setError("password should be at least of 8 letter"); return right;}
        else{
            int i =0;
            for(;i<pass.length();i++){
                if(pass_data[i]==64||pass_data[i]==42||pass_data[i]==35||pass_data[i]==36){
                    break;
                }
            }
            if(i == pass.length()){
                password.setError("password should have any one special character(#,$,@,$) ");
                return right;
            }
            else{
                i=0;
                for(;i<pass.length();i++){
                    if(pass_data[i]<57&&pass_data[i]>47){
                        right=1;
                        break;
                    }
                }
                if(i==pass.length()){
                    password.setError("at least one digit");
                }
            }
        }
        return  right;

    }

    //when second back button will be clicked
    public void gobacksecond(View view) {

        linearLayout2.setVisibility(View.INVISIBLE);
        linearLayout1.setVisibility(View.VISIBLE);

    }

    //when finish button will be clicked.
    public void finish_click(View view) {



        int check = checkpassword(password.getText().toString());
        if (check == 1) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);


            ProgressBar progressBar = new ProgressBar(SignUp.this);

            builder.setView(progressBar);

            builder.setCancelable(false);
            builder.setTitle("wait for registering");
            final AlertDialog alertDialog= builder.create();
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    alertDialog.dismiss();
                }
            },3000);



            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getActiveNetworkInfo()!=null&&connectivityManager.getActiveNetworkInfo().isConnected()){
                        savedataandcreateuser();
                    }
                    else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setCancelable(false);
                        builder.setTitle("no internet");
                        builder.setPositiveButton("okk", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            },3000);

        }
    }

    //this method will save data to the database and create new user .

    private void savedataandcreateuser() {
        if(savedata())
        {
            createuser();
        }
        else{
            Toast.makeText(getApplicationContext(),"database is not responding try again with another username ",Toast.LENGTH_LONG).show();
        }
    }

    private void createuser() {
        finish();
        startActivity(new Intent(SignUp.this,login_activity.class));
    }

    private boolean savedata() {

        boolean issaved=false;
        String username;
        boolean dot_username=false;
        int index_dot=0;

        //getting data from all edittext.
        String name = first.getText().toString() + second.getText().toString();
        String mail = email.getText().toString();
        String pass = password.getText().toString();

        char[] username_data =mail.toCharArray();

        for(int i=0;i<mail.length();i++){
            if(username_data[i]==46){
                index_dot=i;
                dot_username=true;
                break;
            }
        }
        if(dot_username){
            username = mail.substring(0,index_dot);
            Toast.makeText(getApplicationContext(),username,Toast.LENGTH_LONG).show();
        }
        else{
            username=mail;
        }
        user_data data = new user_data(username,pass,name);

        databaseReference.child(databaseReference.push().getKey()).setValue(data);
        Toast.makeText(getApplicationContext(),"user created",Toast.LENGTH_LONG).show();
        issaved=true;
        return issaved;
    }
}
