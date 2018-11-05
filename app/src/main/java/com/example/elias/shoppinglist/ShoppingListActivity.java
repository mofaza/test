package com.example.elias.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private static final String FILENAME = "shopping_list.txt";
    private static final int MAX_BYTES = 8000;

    private ArrayList<ShoppingItem> itemList;
    private ShoppingListAdapter adapter;
    private EditText edit_item;
    private ListView list;

    private void writeItemList(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < itemList.size(); i++){
                ShoppingItem it = itemList.get(i);
                String line = String.format("%s;%b\n", it.getText(), it.isChecked());
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("elias", "writeItemList: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("elias", "writeItemList: IOException");
            Toast.makeText(this, R.string.cannot_write, Toast.LENGTH_SHORT).show();
        }
    }

    private void readItemList(){
        itemList = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte [] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            String content = new String(buffer, 0, nread);
            String[] lines = content.split("\n");
            for (String line : lines) {
                String[] parts = line.split(";");
                itemList.add(new ShoppingItem(parts[0], parts[1].equals(true)));
            }

        }

        catch (FileNotFoundException e) {
            Log.e("elias", "readItemList: FileNotFoundException");
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.e("elias", "readItemList: IOException");
            Toast.makeText(this, R.string.cannot_read, Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        writeItemList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        list = findViewById(R.id.list);
        Button btn_add = findViewById(R.id.btn_add);
        edit_item = findViewById(R.id.edit_item);

        itemList = new ArrayList<>();
        itemList.add(new ShoppingItem("Patatas"));
        itemList.add(new ShoppingItem("Bravas"));
        itemList.add(new ShoppingItem("Toilet Paper"));
        itemList.add(new ShoppingItem("Bananas"));


        adapter = new ShoppingListAdapter(this, android.R.layout.simple_list_item_1, itemList);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addItem();
                return true;
            }
        });

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemList.get(position).toggleChecked();
                adapter.notifyDataSetChanged();


            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                maybeRemoveItem(position);
                return true;
            }
        });

    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(String.format("Are you sure you want to remove '%s'?", itemList.get(pos).getText()));
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();

    }

    private void addItem() {
        String item_text = edit_item.getText().toString();
        if (!item_text.isEmpty() && !itemList.contains(item_text)){
            itemList.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText(""); }

            list.smoothScrollToPosition(itemList.size()-1);
    }



}
