package id.topapp.radinaldn.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Created by radinaldn on 26/01/19.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewholder> {

    private LinkedList<Note> listNotes;
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(LinkedList<Note> listNotes) {
        this.listNotes = listNotes;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewholder holder, int position) {
        holder.tvTitle.setText(getListNotes().get(position).getTitle());
        holder.tvDate.setText(getListNotes().get(position).getDate());
        holder.tvDescription.setText(getListNotes().get(position).getDescription());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallBack() {
                    @Override
                    public void OnItemClicked(View view, int position) {
                        Intent intent = new Intent(activity, FormAddUpdateActivity.class);
                        intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION, position);
                        intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE, getListNotes().get(position));
                        activity.startActivityForResult(intent,
                                FormAddUpdateActivity.REQUEST_UPDATE);
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return getListNotes().size();
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
