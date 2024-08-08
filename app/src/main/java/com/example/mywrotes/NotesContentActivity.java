package com.example.mywrotes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesContentActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView SiteTitleTextView;
    String title, content, NoteID;
    TextView deleteNoteBtn;
    boolean isEditMode = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        titleEditText = findViewById(R.id.notes_title);
        contentEditText = findViewById(R.id.notes_content);
        saveNoteBtn = findViewById(R.id.new_note_done);
        SiteTitleTextView = findViewById(R.id.site_title);
        deleteNoteBtn = findViewById(R.id.delete_note_text_view_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        NoteID = getIntent().getStringExtra("NoteID");

        if (NoteID!=null && !NoteID.isEmpty()){
            isEditMode =true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if (isEditMode){
            SiteTitleTextView.setText("Wrote bearbeiten");
            deleteNoteBtn.setVisibility(View.VISIBLE);
        }


        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        deleteNoteBtn.setOnClickListener((v)-> deleteNoteFromFB());
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString().trim(); // um überflüssige Leerzeichen zu entfernen
        String noteContent = contentEditText.getText().toString().trim();

        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Titel eingeben"); // wenn kein Titel vorhanden ist
            return;
        }

        if (noteContent == null || noteContent.isEmpty()) {
            contentEditText.setError("Inhalt eingeben"); //wenn kein Inhalt vorhanden ist
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }


    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (isEditMode){
            // notiz aktualisieren
            documentReference = tool.getCollectionReferenceForNotes().document(NoteID);
        } else {
            // neue notiz erstellen
            documentReference = tool.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // notiz hinzugefügt
                    tool.showToast(NotesContentActivity.this,"Notiz wurde gespeichert");
                    finish();
                }else {
                    tool.showToast(NotesContentActivity.this,"Error! Notiz wurde nicht gespeichert");
                }
            }
        });
    }

    void deleteNoteFromFB(){
        DocumentReference documentReference;
            documentReference = tool.getCollectionReferenceForNotes().document(NoteID);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // notiz gelöscht
                    tool.showToast(NotesContentActivity.this,"Notiz wurde gelöscht");
                    finish();
                }else {
                    tool.showToast(NotesContentActivity.this,"Error! Notiz konnte nicht gelöscht werden");
                }
            }
        });
    }

}