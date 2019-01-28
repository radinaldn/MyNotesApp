package id.topapp.radinaldn.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import id.topapp.radinaldn.mynotesapp.R;
import id.topapp.radinaldn.mynotesapp.activity.FormAddUpdateActivity;
import id.topapp.radinaldn.mynotesapp.listener.CustomOnItemClickListener;
import id.topapp.radinaldn.mynotesapp.model.Note;

import static id.topapp.radinaldn.mynotesapp.db.DatabaseContract.CONTENT_URI;

/**
 * Created by radinaldn on 26/01/19.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewholder> {

    private Cursor listNotes;
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public Cursor getListNotes() {
        return listNotes;
    }

    public void setListNotes(Cursor listNotes) {
        this.listNotes = listNotes;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewholder holder, int position) {

        final Note note = getItem(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvDate.setText(note.getDate());
        holder.tvDescription.setText(note.getDescription());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallBack() {
                    @Override
                    public void OnItemClicked(View view, int position) {
                        Intent intent = new Intent(activity, FormAddUpdateActivity.class);

                        Uri uri = Uri.parse(CONTENT_URI+"/"+note.getId());
                        intent.setData(uri);

                        activity.startActivityForResult(intent,
                                FormAddUpdateActivity.REQUEST_UPDATE);
                    }
                }));
    }

    private Note getItem(int position) {
        if (!listNotes.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Note(listNotes);
    }

    @Override
    public int getItemCount() {
        if (listNotes == null) return 0;
        return listNotes.getCount();
    }

    public class NoteViewholder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvDate;
        CardView cvNote;

        public NoteViewholder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            cvNote = itemView.findViewById(R.id.cvItemNote);
        }
    }

    @NonNull
    @Override
    public NoteViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent,
                false);

        return new NoteViewholder(view);
    }
}
