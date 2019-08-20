package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaSession2;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button sendbt;
    private EditText editdt;
    public String msg;

    private Button sendbt_profile;
    private EditText editdt_profile;
    private CheckBox Cb_1;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendbt = (Button) findViewById(R.id.button2);
        editdt = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listviewmsg);

        sendbt_profile = (Button) findViewById(R.id.button3);
        editdt_profile = (EditText) findViewById(R.id.editText2);
        Cb_1 = (CheckBox) findViewById(R.id.checkBox);

        initDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);



        sendbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 버튼 누르면 수행 할 명령
                msg = editdt.getText().toString();
                databaseReference.child("message").push().setValue(msg);
            }
        });

        sendbt_profile.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                String tokenID = FirebaseInstanceId.getInstance().getToken();

                mReference = mDatabase.getReference("UserProfile");
                Boolean ms1 = Cb_1.isChecked();

                if (!TextUtils.isEmpty(tokenID)) {
                    senddata Senddata = new senddata();
                    Senddata.firebaseKey = tokenID;
                    Senddata.userName = editdt_profile.getText().toString();
                    Senddata.message1 = ms1.toString();

                    mReference.child(tokenID).setValue(Senddata);
                }

            }
        });

        mReference = mDatabase.getReference("message"); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot messageData : dataSnapshot.getChildren()){
                    String test = FirebaseInstanceId.getInstance().getToken();
                    Log.d("Token Value", test);

                    String msg2 = messageData.getValue().toString();
                    Array.add(msg2);
                    adapter.add(msg2);
                }


                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

}

