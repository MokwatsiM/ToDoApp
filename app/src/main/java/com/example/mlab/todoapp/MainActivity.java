package com.example.mlab.todoapp;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.MediumTest;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mlab.todoapp.Adapter.ListItemAdapter;
import com.example.mlab.todoapp.model.ToDo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    List<ToDo> toDoList;
    FirebaseFirestore db;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;

    public MaterialEditText title, description;

    public boolean isUpdate = false; //flag to check is update of add neww
    public String idUpdate = "";

    ListItemAdapter adapter;
    SpotsDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        dialog = new SpotsDialog(this);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        toDoList = new ArrayList<>();

        fab = findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpdate) {
//                    if (TextUtils.isEmpty(title.getText().toString())) {
//                        title.setError("Title cannot be Empty");
//                        title.setErrorColor(R.color.colorAccent);
//                        return;
//                    }
//
//                    if (TextUtils.isEmpty(description.getText().toString())) {
//                        description.setError("Title cannot be Empty");
//                        title.setErrorColor(R.color.colorAccent);
//                        return;
//                    }

                    setData(title.getText().toString(), description.getText().toString());
                } else {
                    updateData(title.getText().toString(), description.getText().toString());
                    isUpdate = !isUpdate; //reset flag
                }
            }
        });


        recyclerView = findViewById(R.id.listToDo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadData();//load data from firestore


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("DELETE"))
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        db.collection("ToDoList").document(toDoList.get(index).getId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }

    private void updateData(String title, String description) {
        db.collection("ToDoList").document(idUpdate)
                .update("title", title, "description", description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "updated!", Toast.LENGTH_LONG).show();

                    }
                });

        //Real Time Update refresh data
        db.collection("ToDoList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void setData(String text, String descriptionText) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> toDo = new HashMap<>();
        toDo.put("id", id);
        toDo.put("title", text);
        toDo.put("description", descriptionText);


        db.collection("ToDoList").document(id).set(toDo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadData();
            }
        });
    }

    private void loadData() {
        dialog.show();
        if (toDoList.size() > 0)
            toDoList.clear();
        db.collection("ToDoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            ToDo toDo = new ToDo(doc.getString("id"), doc.getString("title"), doc.getString("description"));
                            toDoList.add(toDo);


                        }

                        adapter = new ListItemAdapter(MainActivity.this, toDoList);
                        recyclerView.setAdapter(adapter);
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
