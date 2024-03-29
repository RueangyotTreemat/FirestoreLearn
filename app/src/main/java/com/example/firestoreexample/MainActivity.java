package com.example.firestoreexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firestoreexample.Adapters.FbAdapter;
import com.example.firestoreexample.ModelClasses.ModelClass;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText userNameET, userStatusET;
    RecyclerView objectRecyclerView;

    FirebaseFirestore objectFirebaseFirestore;
    FbAdapter objectFbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            userNameET = findViewById(R.id.userNameET);
            userStatusET = findViewById(R.id.userStatusET);

            objectRecyclerView = findViewById(R.id.RV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            addStatusToRV();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void addStatus(View view){
        try {
            if(!userStatusET.getText().toString().isEmpty()
            && !userStatusET.getText().toString().isEmpty()){

                Map<String,String> objectMap = new HashMap<>();
                objectMap.put("status", userStatusET.getText().toString());

                objectFirebaseFirestore.collection("Status")
                        .document(userNameET.getText().toString())
                        .set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Status is added",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Fails to add status",Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please enter values",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addStatusToRV(){
        try {
            Query objectQuery = objectFirebaseFirestore.collection("Status");
            FirestoreRecyclerOptions<ModelClass> objectOptions
                    = new FirestoreRecyclerOptions.Builder<ModelClass>()
                    .setQuery(objectQuery, ModelClass.class)
                    .build();

            objectFbAdapter = new FbAdapter(objectOptions);
            objectRecyclerView.setAdapter(objectFbAdapter);

            objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (Exception e){
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    protected void onStart() {
        super.onStart();
        objectFbAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectFbAdapter.stopListening();
    }
}
