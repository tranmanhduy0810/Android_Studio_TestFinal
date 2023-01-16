package com.webmientay.baitaptonghop;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.webmientay.baitaptonghop.models.SQLiteHelper;

import java.util.ArrayList;

import com.webmientay.baitaptonghop.models.Phone;

public class MainActivity extends AppCompatActivity {
    SQLiteHelper dbHelper;
    SQLiteDatabase database;
    ListView listViewPhone;
    ArrayList<Phone> phoneArrayListString;
    ArrayAdapter<String> arrayAdapter;
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        listViewPhone = (ListView) findViewById(R.id.listView);

        dbHelper = new SQLiteHelper(this);
        dbHelper.queryData("Delete from phones");
        dbHelper.queryData("Insert into phones (id, name, price) values " +
                "('ID-1', 'Trần Mạnh Duy', '22'), " +
                "('ID-2', 'Trần Mạnh Nam', '21'), " +
                "('ID-3', 'Hoàng Hữu Nghĩa', '22'), " +
                "('ID-4', 'Lê Bảo Em', '20'), " +
                "('ID-5', 'Lê Nam Anh', '22'), " +
                "('ID-6', 'Lê Em Nam', '33'),  " +
                "('ID-7', 'Nguyễn Thanh Tiến', '22'), " +
                "('ID-8', 'Lâm Hoàng Phúc', '43'), " +
                "('ID-9', 'Trần Ngọc Anh', '34'), " +
                "('ID-10', 'Phúc Nam Trần', '30'), " +
                "('ID-11', 'Trần Bá Nam', '25'), " +
                "('ID-12', 'Ngọc Anh Trần', '50'), " +
                "('ID-13', 'Ông Dũng Anh', '60')");

        database = dbHelper.getReadableDatabase();


        phoneArrayListString = new ArrayList<>();

        listViewPhone.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                openDialogEditUser();
                return false;
            }
        });
        actionGetData();

        database.close();
    }


    private void openDialogEditUser(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        Window window = dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);
        dialog.setCancelable(true);

        EditText editName = dialog.findViewById(R.id.editTextNamePhone);
        EditText editID = dialog.findViewById(R.id.editTextPhoneID);
        EditText editPrice = dialog.findViewById(R.id.editTextPrice);

        Button buttonBack  = dialog.findViewById(R.id.buttonYesEditUser);
        Button buttonDelete = dialog.findViewById(R.id.buttonNoEditUser);

        TextView textViewTitleEditUser = dialog.findViewById(R.id.textViewTitleEditUser);
        TextView textViewNotiEdit = dialog.findViewById(R.id.textViewNotiEdit);

        String name = phoneArrayListString.get(pos).name;
        String id = phoneArrayListString.get(pos).id;
        String age = phoneArrayListString.get(pos).age+"";
        editName.setText(name);
        editID.setText(id);
        editPrice.setText(age);
        textViewTitleEditUser.setText("Sửa thông tin nhân viên ");

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteUser();

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionGetData();
                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Back",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void confirmDeleteUser(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Bạn chắc chắn xoá ?");

        String name = phoneArrayListString.get(pos).name;
        alertDialog.setMessage("Bạn có muốn xóa nhân viên "+name+"?");

        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id = phoneArrayListString.get(pos).id;
                Toast.makeText(MainActivity.this,"Đã xoá nhân viên " +name,Toast.LENGTH_SHORT).show();
                dbHelper.queryData("DELETE FROM phones WHERE id='"+id+"'");
                //dbHelper.queryData("Delete from phones");

            }
        });

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this,"Quay lại",Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog.show();
    }

    private void actionGetData() {
        Cursor data = dbHelper.getData("SELECT * FROM phones");
        phoneArrayListString.clear();

        while (data.moveToNext()){
            String name = data.getString(1);
            String id = data.getString(0);
            int price = data.getInt(2);
            phoneArrayListString.add(new Phone(id, name, price));

        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, phoneArrayListString){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if(position%2==0){
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                return view;
            }
        };
        listViewPhone.setAdapter(arrayAdapter);

    }
}