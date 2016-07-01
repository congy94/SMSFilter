package com.smsfilter.congybk.smsfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBlockPhone extends AppCompatActivity {
    private EditText mEdtName;
    private EditText mEdtNumber;
    private EditText mEdtReason;
    private Button btnAdd;
    private BlockPhone mBlockPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_block_phone);
        mEdtName = (EditText)findViewById(R.id.edtName);
        mEdtNumber = (EditText)findViewById(R.id.edtNumber);
        mEdtReason = (EditText)findViewById(R.id.edtReason);
        btnAdd = (Button)findViewById(R.id.btnAdd);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("PACKAGE");
        if(bundle!=null) {
            mBlockPhone = (BlockPhone) bundle.getSerializable("PHONEBLOCK");
            mEdtName.setText(mBlockPhone.getName());
            mEdtNumber.setText(mBlockPhone.getNumber());
            mEdtReason.setText(mBlockPhone.getReason());
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBlockPhone==null) {
                    BlockPhone blockPhone = new BlockPhone();
                    blockPhone.setName(mEdtName.getText().toString());
                    blockPhone.setNumber(mEdtNumber.getText().toString());
                    blockPhone.setReason(mEdtReason.getText().toString());
                    if(!blockPhone.getNumber().equals("")){
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.addBlockPhone(blockPhone);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Please, Input Number",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mBlockPhone.setName(mEdtName.getText().toString());
                    mBlockPhone.setNumber(mEdtNumber.getText().toString());
                    mBlockPhone.setReason(mEdtReason.getText().toString());

                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.updateBlockPhone(mBlockPhone);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }
}
