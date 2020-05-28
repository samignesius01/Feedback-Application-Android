package com.example.feedback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    EditText fb1,fb2,fb3;
    Button btn_submit;
    Intent intent;
    FirebaseAuth auth;
    DatabaseReference reference;
    Users user;
    FirebaseUser fuser;
    FirebaseDatabase database;

    Spinner semSpinner,subSpinner;
    ArrayList<String> arrayList_parent;
    ArrayAdapter<String> arrayAdapter_parent;

    ArrayList<String> arrayList_semester1,arrayList_semester2,arrayList_semester3,arrayList_semester4,arrayList_semester5;
    ArrayAdapter<String> arrayAdapter_child;

    //Toolbar toolbar;
    @Override
    public void setSupportActionBar(@Nullable androidx.appcompat.widget.Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        fb1=findViewById(R.id.feedbackone);
        fb2=findViewById(R.id.feedbacktwo);
        fb3=findViewById(R.id.feedbackthree);
        btn_submit=findViewById(R.id.feedbacksubmit);
        user=new Users();
        reference= FirebaseDatabase.getInstance().getReference().child("Feed");
//            toolbar=findViewById(R.id.toolbar);

        reference=database.getInstance().getReference().child("Spinner");


        intent=getIntent();
        final String userid=intent.getStringExtra("userid");
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String msg1=fb1.getText().toString();
              String msg2=fb2.getText().toString();
              String msg3=fb3.getText().toString();
              if(!msg1.equals("") || !msg2.equals("") || !msg3.equals(""))
              {
                 sendMessage(fuser.getUid(),userid,msg1,msg2,msg3);
              }
              else
              {
                 Toast.makeText(FeedbackActivity.this,"Feedback not submited",Toast.LENGTH_SHORT).show();
              }
              fb1.setText("");
              fb2.setText("");
              fb3.setText("");
            }
        });


        semSpinner=findViewById(R.id.sem);
        subSpinner=findViewById(R.id.sub);

        arrayList_parent=new ArrayList<>();
        arrayList_parent.add("Semester 1");
        arrayList_parent.add("Semester 2");
        arrayList_parent.add("Semester 3");
        arrayList_parent.add("Semester 4");
        arrayList_parent.add("Semester 5");


        arrayAdapter_parent=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_parent);

        semSpinner.setAdapter(arrayAdapter_parent);


        //child spinner starts

        arrayList_semester1=new ArrayList<>();
        arrayList_semester1.add("C");
        arrayList_semester1.add("Data structures");
        arrayList_semester1.add("MFCS");


        arrayList_semester2=new ArrayList<>();
        arrayList_semester2.add("C++");
        arrayList_semester2.add("Advanced Data structure");
        arrayList_semester2.add("Processor");

        arrayList_semester3=new ArrayList<>();
        arrayList_semester3.add("Java");
        arrayList_semester3.add("HTML");

        arrayList_semester4=new ArrayList<>();
        arrayList_semester4.add("Artificial Intelligence");
        arrayList_semester4.add("PCD");
        arrayList_semester4.add("Enterprise computing");
        arrayList_semester4.add("Android");
        arrayList_semester4.add("Computer network");


        arrayList_semester5=new ArrayList<>();
        arrayList_semester5.add("Compiler Design");




        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_semester1);
                }
                if(position==1)
                {
                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_semester2);
                }
                if(position==2)
                {
                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_semester3);
                }
                if(position==3)
                {
                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_semester4);
                }
                if(position==4)
                {
                    arrayAdapter_child=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_semester5);
                }

                subSpinner.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //child spinner ends

    }

    private void sendMessage(String uid, final String userid, String msg1,String msg2,String msg3) {


         DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
         HashMap<String,Object> hashMap=new HashMap<>();
         hashMap.put("What is good in the course:",msg1);
         hashMap.put("What can be improved or changed",msg2);
         hashMap.put("Other comments",msg3);
         hashMap.put("userid",uid);
         hashMap.put("semester",semSpinner.getSelectedItem().toString());
         hashMap.put("subject",subSpinner.getSelectedItem().toString());

         reference.child("Feed").push().setValue(hashMap);


         //reference=FirebaseDatabase.getInstance().getReference("Feed");

         final DatabaseReference Fref=FirebaseDatabase.getInstance().getReference("Feed")
                 .child(fuser.getUid());
                 //.child(userid);

         Fref.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(!dataSnapshot.exists())
                 {
                     Fref.child("userid").setValue(userid);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FeedbackActivity.this,MainActivity.class));
                finish();
                return true;
        }

        return false;

    }
}
