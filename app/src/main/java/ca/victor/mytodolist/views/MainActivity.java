package ca.victor.mytodolist.views;

import android.content.Context;
import android.content.DialogInterface;
//import android.graphics.Color;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ca.victor.mytodolist.R;
import ca.victor.mytodolist.dao.DatabaseAdapter;
import ca.victor.mytodolist.models.Tasks;

public class MainActivity extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;

    private ListView mListView;
    private Tasks mTask;

    private TextView mTextStatus;

    private FloatingActionButton addButton;
    private FloatingActionButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseAdapter = new DatabaseAdapter(MainActivity.this);

        mTextStatus = findViewById(R.id.list_status);
        mListView = findViewById(R.id.list);
        addButton = findViewById(R.id.addButton);
        clearButton = findViewById(R.id.clearButton);
        refreshList();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDialog();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearDialog();
            }
        });

        mListView.setLongClickable(true);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Tasks tasks = (Tasks) mListView.getItemAtPosition(position);
                deleteOne(tasks.getId());
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tasks tasks = (Tasks) mListView.getItemAtPosition(position);
                modifyOne(tasks.getId());
            }
        });
    }

    private void deleteOne(long id) {
        final long position = id;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.deleteOne_title);
        alert.setMessage(R.string.deleteOne_message);

        alert.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteOnePos(position);
                refreshList();
            }
        });

        alert.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void clearDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.delete_list);
        alert.setMessage(R.string.deleteAll_message);

        alert.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteAll();
                refreshList();
            }
        });

        alert.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void addDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.addOne_title);
        alert.setMessage(R.string.addOne_message);

        final EditText name = new EditText (this);
        name.setHint(R.string.addOne_name);

        final EditText text = new EditText(this);
        text.setHint(R.string.addOne_task);

        final CheckBox importantCheck = new CheckBox(this);
        importantCheck.setText(R.string.addOne_important);

        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);

        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(importantCheck, layoutParams);

        alert.setView(layout);

        alert.setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String important;
                if(importantCheck.isChecked()) {
                    important = "y";
                }
                else {
                    important = "n";
                }
                if(name.length() > 0 || text.length() > 0) {
                    mTask = new Tasks( name.getText().toString(), text.getText().toString(), important);
                    long taskId = AddItem(mTask);
                    mTask.setId(taskId);
                    refreshList();
                }
            }
        });

        alert.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void modifyOne(final long position) {

        mTask = databaseAdapter.getById(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.modifyOne_title);
        alert.setMessage(R.string.modifyOne_message);

        final EditText name = new EditText (this);
        name.setText(mTask.getName());

        final EditText text = new EditText(this);
        text.setText(mTask.getDescription());

        // Checkbox
        final CheckBox importantCheck = new CheckBox(this);
        importantCheck.setText(R.string.addOne_important);

        if(mTask.getImportant().equals("y")) {
            importantCheck.setChecked(true);
        }

        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(70, 0, 70, 0);
        layout.addView(name, layoutParams);
        layout.addView(text, layoutParams);
        layout.addView(importantCheck, layoutParams);
        alert.setView(layout);
        alert.setPositiveButton(R.string.app_modify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String important;
                if(importantCheck.isChecked()) {
                    important = "y";
                }
                else {
                    important = "n";
                }
                if(name.length() > 0 || text.length() > 0) {
                    mTask = new Tasks(position, name.getText().toString(), text.getText().toString(), important);
                    ModifyItem(mTask.getId(), mTask);
                    refreshList();
                }
            }
        });

        alert.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    private void refreshList() {
        List<Tasks> tasksList = databaseAdapter.getData();
        RowAdapter adapter = new RowAdapter(MainActivity.this, tasksList);
        mListView.setAdapter(adapter);

        if(tasksList.size() > 0 ) {
            mTextStatus.setText(R.string.app_listNoEmpty);
            clearButton.setEnabled(true);
        }
        else {
            mTextStatus.setText(R.string.app_listEmpty);
            clearButton.setEnabled(false);
        }
    }

    private void ModifyItem(long position, Tasks tasks) {
        databaseAdapter.modifyById(position,tasks);
    }

    private long AddItem(Tasks task ) {
        return databaseAdapter.addData(task);
    }

    private void deleteOnePos(long id) {
        databaseAdapter.deleteById(id);
    }

    private void deleteAll() {
        databaseAdapter.allDelete();
    }

}
