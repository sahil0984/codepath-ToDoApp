package com.forCodePath.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();
    }


    private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
			
		});

		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
				  Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
				  i.putExtra("item", todoAdapter.getItem(pos).toString()); 
				  i.putExtra("pos", pos); 
				  startActivityForResult(i, REQUEST_CODE);
			}
			
		});			
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    	     // Extract name value from result extras
    	     String itemName = data.getExtras().getString("item");
    	     int pos = data.getExtras().getInt("pos");
   	     
		     todoItems.set(pos, itemName);
		     todoAdapter.notifyDataSetChanged();
			 writeItems();
    	}
    }
    
    public void onAddedItem(View v) {
    	String itemText = etNewItem.getText().toString();
    	todoAdapter.add(itemText);
    	etNewItem.setText("");
    	writeItems();
	}

    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			todoItems = new ArrayList<String>();
		}
	}
    
    private void writeItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
