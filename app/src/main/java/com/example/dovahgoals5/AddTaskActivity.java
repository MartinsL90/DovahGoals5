package com.example.dovahgoals5;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dovahgoals5.Model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddTaskActivity extends AppCompatActivity {

    EditText etTaskInput;
    Button saveBtn;
    FirebaseFirestore db;
    String TAG="DovahGoals";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtn=findViewById(R.id.taskSaveBtn);
        etTaskInput=findViewById(R.id.inputTaskName);

        db = FirebaseFirestore.getInstance();

        saveBtn.setOnClickListener(view -> {

            String taskName=etTaskInput.getText().toString().trim();
            {
                findViewById(R.id.progress).setVisibility(View.VISIBLE);
                TaskModel taskModel=new TaskModel("",taskName,"PENDING", FirebaseAuth.getInstance().getUid());
                db.collection("tasks").add(taskModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                findViewById(R.id.successLayout).setVisibility(View.VISIBLE);
                                findViewById(R.id.progress).setVisibility(View.GONE);
                                findViewById(R.id.addTaskLayout).setVisibility(View.GONE);



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        });






    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}