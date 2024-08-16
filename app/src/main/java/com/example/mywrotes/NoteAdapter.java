package com.example.mywrotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextview.setText(note.Title);
        holder.contentTextview.setText(note.content);
        holder.timestampTextview.setText(tool.timesstampToString(note.timestamp));

        holder.editBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(context, NotesContentActivity.class);
            intent.putExtra("title", note.Title);
            intent.putExtra("content", note.content);
            String NoteID = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("NoteID", NoteID);
            context.startActivity(intent);
        });


        holder.deleteBtn.setOnClickListener((v) -> {
            String NoteID = this.getSnapshots().getSnapshot(position).getId();
            deleteNoteFromFB(NoteID); //
        });
    }


    void deleteNoteFromFB(String NoteID) {
        DocumentReference documentReference = tool.getCollectionReferenceForNotes().document(NoteID);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Notiz gelöscht
                    tool.showToast(context, "Notiz wurde gelöscht"); // **Anpassung:** Benutzerbenachrichtigung
                } else {
                    tool.showToast(context, "Error! Notiz konnte nicht gelöscht werden");
                }
            }
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextview, contentTextview, timestampTextview;
        ImageButton editBtn;

        ImageButton deleteBtn;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextview = itemView.findViewById(R.id.note_title_text_view);
            contentTextview = itemView.findViewById(R.id.note_content_text_view);
            timestampTextview = itemView.findViewById(R.id.note_timestamp_text_view);
            editBtn = itemView.findViewById(R.id.edit_button);
            deleteBtn = itemView.findViewById(R.id.delete_button);
        }
    }
}

