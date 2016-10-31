package com.skylake.notetaker;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor>{

    private CursorAdapter cursor_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = {DBOpenHelper.NOTE_TEXT};
        int[] to = {android.R.id.text1};
        cursor_adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, from, to, 0);

        ListView list_view = (ListView) findViewById(R.id.list_view);
        list_view.setAdapter(cursor_adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * function: insertNote
     * This method inserts a new note into the database.
     */
    private void insertNote(String note_text) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, note_text);
        Uri note_uri = getContentResolver().insert(NoteProvider.CONTENT_URI, values);

        Log.d("MainActivity", "Inserted note " + note_uri.getLastPathSegment());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_samples:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllNotes();
                break;
            default:
                // do nothing
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            getContentResolver().delete(NoteProvider.CONTENT_URI,
                                    null, null);
                            restartLoader();

                            Toast.makeText(MainActivity.this, getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(this);
        alert_dialog_builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void insertSampleData() {
        insertNote("Hello World");
        insertNote("Multi-line\nnote");
        insertNote("Very long note with a lot of text that exceeds the width of the screen");

        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, NoteProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor_adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursor_adapter.swapCursor(null);
    }
}