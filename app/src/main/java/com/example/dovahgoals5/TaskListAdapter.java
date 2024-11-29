package com.example.dovahgoals5;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dovahgoals5.Model.TaskModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private ArrayList<TaskModel> taskDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final

        TextView taskNameTv,taskStatusTv;

        LinearLayout containerLl;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            taskNameTv = (TextView) view.findViewById(R.id.taskNameTv);
            taskStatusTv = (TextView) view.findViewById(R.id.taskStatusTv);
            containerLl=(LinearLayout) view.findViewById(R.id.containerLL);
        }


    }


    public TaskListAdapter(ArrayList<TaskModel> taskDataset) {
        this.taskDataset = taskDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.taskNameTv.setText(taskDataset.get(position).getTaskName());
        viewHolder.taskStatusTv.setText(taskDataset.get(position).getTaskStatus());

        String status=taskDataset.get(position).getTaskStatus();

        if(status.toLowerCase().equals("pending"))
        {
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#FFFF00"));

        } else if(status.toLowerCase().equals("completed"))
        {
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#00FF00"));
        }else{

            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        viewHolder.containerLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu=new PopupMenu(view.getContext(),viewHolder.containerLl );
                popupMenu.inflate(R.menu.taskmenu);
                popupMenu.show();


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId()==R.id.deleteMenu)
                        {


                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataset.get(position).getTaskId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(view.getContext(), "Item deleted",Toast.LENGTH_SHORT).show();
                                            viewHolder.containerLl.setVisibility(View.GONE);

                                        }
                                    });



                        }

                        if(menuItem.getItemId()==R.id.markCompleteMenu) {


                            TaskModel completedTask = taskDataset.get(position);
                            completedTask.setTaskStatus("completed");

                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataset.get(position).getTaskId())
                                    .set(completedTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "Task Item Marked As Completed", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#00FF00"));
                            viewHolder.taskStatusTv.setText("COMPLETED");

                                        }
                        if (menuItem.getItemId() == R.id.markCompleteMenu) {
                            FirebaseFirestore.getInstance().collection("users").document("userID").get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String currentXpString = documentSnapshot.getString("xp");
                                            String levelString = documentSnapshot.getString("level");  // Get the current level
                                            String maxLevelString = documentSnapshot.getString("maxLevelString");  // Get the maxLevel value from Firestore

                                            if (currentXpString != null && levelString != null && maxLevelString != null) {
                                                try {
                                                    int currentXp = Integer.parseInt(currentXpString); // Parse the current XP to an integer
                                                    int xpAmount = 10;
                                                    int newXp = currentXp + xpAmount;  // Increment XP by the specified amount (10 XP)
                                                    int maxLevel = Integer.parseInt(maxLevelString);  // Parse the maxLevel from the Firestore

                                                    // Check if the new XP exceeds or equals the maxLevel
                                                    if (newXp >= maxLevel) {
                                                        // Level up logic: Reset XP and increase level
                                                        newXp = 0; // Reset XP to 0
                                                        int level = Integer.parseInt(levelString);  // Parse current level
                                                        level++;  // Increase level

                                                        // Update Firestore with new XP and level
                                                        FirebaseFirestore.getInstance().collection("users")
                                                                .document("userID")
                                                                .update("xp", String.valueOf(newXp), "level", String.valueOf(level))
                                                                .addOnSuccessListener(aVoid -> {
                                                                    // Optionally, you can add a log or a notification about level up
                                                                });
                                                    } else {
                                                        // Just update the XP without level up
                                                        FirebaseFirestore.getInstance().collection("users")
                                                                .document("userID")
                                                                .update("xp", String.valueOf(newXp))
                                                                .addOnSuccessListener(aVoid -> {
                                                                    // Optionally, you can add a log or a notification that XP was updated
                                                                });
                                                    }

                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                        }






                        return false;
                    }
                });





                return false;
            }
        });





    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskDataset.size();
    }

    public void clearAllItems(){
        taskDataset.clear();
        notifyDataSetChanged();

    }
}
