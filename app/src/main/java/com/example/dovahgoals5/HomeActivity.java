package com.example.dovahgoals5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dovahgoals5.Model.TaskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class HomeActivity extends AppCompatActivity {

    RecyclerView taskRv;
    ArrayList<TaskModel> dataList=new ArrayList<>();
    TaskListAdapter taskListAdapter;
    FirebaseFirestore db;
    String TAG="Homepage query docs";
    TextView userNameTv;
    String maxLevelString;

    TextView levelTextView;
    CircleImageView userImageIv;
    SearchView searchView;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();


        db=FirebaseFirestore.getInstance();
        taskRv=findViewById(R.id.taskListRv);
        userNameTv=findViewById(R.id.userNameTv);
        userImageIv=findViewById(R.id.userProfileIv);
        searchView=findViewById(R.id.searchview);
        levelTextView=findViewById(R.id.levelTextView);
        ProgressBar xpProgressBar = findViewById(R.id.xpProgressBar);


        userImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });


        userNameTv.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImageIv);

        Log.d("Level", FirebaseAuth.getInstance().getCurrentUser().getUid());




        taskListAdapter=new TaskListAdapter(dataList);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        taskRv.setLayoutManager(layoutManager);
        taskRv.setAdapter(taskListAdapter);


        findViewById(R.id.addTaskFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AddTaskActivity.class));
            }
        });





        db.collection("tasks")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                TaskModel taskModel= document.toObject(TaskModel.class);
                                taskModel.setTaskId(document.getId());

                                dataList.add(taskModel);
                                taskListAdapter.notifyDataSetChanged();




                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("users")
                .document("userID")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("level")) {
                        String level = documentSnapshot.getString("level");
                        levelTextView.setText("Level: " + level);

                        // Parse level from the TextView after fetching the data
                        int levelInt = Integer.parseInt(level);  // Parse level as integer
                        int maxLevel;

                        // Determine maxLevel based on level
                        if (levelInt >= 1 && levelInt <= 10) {
                            maxLevel = 100;
                        } else if (levelInt >= 11 && levelInt <= 25) {
                            maxLevel = 250;
                        } else if (levelInt >= 26 && levelInt <= 50) {
                            maxLevel = 500;
                        } else if (levelInt >= 51 && levelInt <= 80) {
                            maxLevel = 1000;
                        } else if (levelInt >= 81) {
                            maxLevel = 2000;
                        } else {
                            maxLevel = 100; // Default fallback value
                        }

                        // Convert maxLevel to String if needed
                        maxLevelString = String.valueOf(maxLevel);
                        FirebaseFirestore.getInstance().collection("users")
                                .document("userID") // Use the correct userID here
                                .update("maxLevelString", String.valueOf(maxLevelString))
                                .addOnSuccessListener(aVoid -> {
                                  //  Toast.makeText(maxLevelString.getContext(), "Max Level Updated Successfully!", Toast.LENGTH_SHORT).show();

                                });

                        // Log the maxLevelString
                        Log.d("MaxLevelLog_Update", "The value of maxLevelString is: " + maxLevelString);

                    } else {
                        levelTextView.setText("Level: Not Found");
                    }
                })
                .addOnFailureListener(e -> {
                    levelTextView.setText("Level: Error");
                    Log.e("MaxLevelLog", "Error fetching level data", e);
                });





        db.collection("users")
                .document("userID")  // Replace with the actual user ID
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("xp")) {
                        // Retrieve XP as a String
                        String xpString = documentSnapshot.getString("xp");

                        if (xpString != null) {
                            try {
                                // Convert the String XP value to an integer
                                int xp = Integer.parseInt(xpString);

                                // Set the max XP (you can adjust this as needed)
                                int maxXp = 10000;

                                // Calculate the progress as a fraction of max XP
                                int progress = (int) ((double) xp / maxXp * 10000);

                                // Set the progress of the ProgressBar dynamically
                                xpProgressBar.setProgress(progress);
                            } catch (NumberFormatException e) {
                                // Handle invalid number format in case the XP is not a valid integer
                                Log.e("XP Parsing Error", "Invalid XP format", e);
                                xpProgressBar.setProgress(0);  // Set progress to 0 if there's an error
                            }
                        } else {
                            // In case the "xp" field is null
                            xpProgressBar.setProgress(0);
                        }
                    } else {
                        // If XP doesn't exist in Firestore
                        xpProgressBar.setProgress(0);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to fetch data from Firestore
                    Log.e("FirestoreError", "Error fetching XP", e);
                    xpProgressBar.setProgress(0);  // Set progress to 0 if an error occurs
                });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("search"," "+s);
                taskListAdapter.clearAllItems();
                db.collection("tasks")
                        .orderBy("taskName")
                        .startAt(s).
                        endAt(s+'\uf8ff')

                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {


                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());


                                        TaskModel taskModel= document.toObject(TaskModel.class);
                                        taskModel.setTaskId(document.getId());

                                        dataList.add(taskModel);
                                        taskListAdapter.notifyDataSetChanged();





                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {



                return false;
            }
        });

        findViewById(R.id.tavernFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,TavernActivity.class));
            }
        });

        findViewById(R.id.achievementsFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AchievementsActivity.class));
            }
        });




    }
}