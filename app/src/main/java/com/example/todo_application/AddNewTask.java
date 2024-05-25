package com.example.todo_application;

import static android.companion.CompanionDeviceManager.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    private EditText editTask;
    private Button save;
    private Context context;
    private FirebaseFirestore firestore;
    private String id = "";
    private ImageButton speech_btn;
    private static final int RECOGNIZER_CODE = 1;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    private ActivityResultLauncher<Intent> speechRecognizerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the launcher
        speechRecognizerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ArrayList<String> taskText = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (taskText != null && !taskText.isEmpty()) {
                            editTask.setText(taskText.get(0));
                        }
                    }
                }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTask = view.findViewById(R.id.task_edit);
        save = view.findViewById(R.id.save_btn);
        speech_btn = view.findViewById(R.id.speech_mic_btn);
        String userid = UUID.randomUUID().toString();

        // Speech to text configuration
        speech_btn.setOnClickListener(v -> startSpeechRecognizer());

        // Firestore Connection
        firestore = FirebaseFirestore.getInstance();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");

            editTask.setText(task);

            if (task.length() > 0) {
                save.setEnabled(false);
                save.setBackgroundColor(Color.BLUE);
            }
        }

        editTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    save.setEnabled(false);
                    save.setBackgroundColor(Color.RED);
                } else {
                    save.setEnabled(true);
                    save.setBackgroundColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        boolean finalisUpdate = isUpdate;

        save.setOnClickListener(v -> {
            String task = editTask.getText().toString();

            if (finalisUpdate) {
                firestore.collection("task").document(id).update("task", task);
                Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show();
            } else {
                if (task.isEmpty()) {
                    Toast.makeText(context, "Empty task not Allowed !!", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("userid", userid);
                    taskMap.put("task", task);
                    taskMap.put("status", 0);
                    taskMap.put("time", FieldValue.serverTimestamp());

                    firestore.collection("task").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
            dismiss();
        });
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Now Speak");
        speechRecognizerLauncher.launch(intent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
