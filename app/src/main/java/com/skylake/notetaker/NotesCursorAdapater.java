package com.skylake.notetaker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NotesCursorAdapater extends CursorAdapter {
    public NotesCursorAdapater(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String note_text = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

        int position_of_line_feed = note_text.indexOf(10);
        if (position_of_line_feed != -1) {
            // found line feed
            note_text = note_text.substring(0, position_of_line_feed) + " ...";
        }

        TextView tv = (TextView) view.findViewById(R.id.text_view_note);
        tv.setText(note_text);

    }
}
