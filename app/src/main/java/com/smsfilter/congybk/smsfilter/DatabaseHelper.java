package com.smsfilter.congybk.smsfilter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congybk on 22/06/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smsfilter";
    private static final String TABLE_NAME = "BlockPhone";
    private static final int DATABASE_VERSION = 1;

    private static final String COLUMN_BLOCKPHONE_ID = "BlockPhone_Id";
    private static final String COLUMN_BLOCKPHONE_NAME ="BlockPhone_Name";
    private static final String COLUMN_BLOCKPHONE_PHONE = "BlockPhone_Phone";
    private static final String COLUMN_BLOCKPHONE_REASON = "BlockPhone_Reason";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQL","onCreate");
        String sql = "CREATE TABLE "+TABLE_NAME+" (" +
                COLUMN_BLOCKPHONE_ID +" INTEGER PRIMARY KEY,"+
                COLUMN_BLOCKPHONE_NAME+" TEXT,"+
                COLUMN_BLOCKPHONE_PHONE+" TEXT,"+
                COLUMN_BLOCKPHONE_REASON+" TEXT) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addBlockPhone(BlockPhone blockPhone){
        Log.d("SQL","addPhone");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BLOCKPHONE_NAME,blockPhone.getName());
        values.put(COLUMN_BLOCKPHONE_PHONE,blockPhone.getNumber());
        values.put(COLUMN_BLOCKPHONE_REASON,blockPhone.getReason());
        db.insert(TABLE_NAME,null,values);

        db.close();
    }
    public List<BlockPhone> getAllBlockPhones(){
        List<BlockPhone> blockPhones = new ArrayList<>();
        String sql = "SELECT * FROM "+ TABLE_NAME+" ORDER BY "+COLUMN_BLOCKPHONE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                BlockPhone blockPhone = new BlockPhone();
                blockPhone.setId(Integer.parseInt(cursor.getString(0)));
                blockPhone.setName(cursor.getString(1));
                blockPhone.setNumber(cursor.getString(2));
                blockPhone.setReason(cursor.getString(3));
                blockPhones.add(blockPhone);
            }while(cursor.moveToNext());
        }

        return blockPhones;
    }
    public BlockPhone getBlockPhone(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{COLUMN_BLOCKPHONE_ID,
                COLUMN_BLOCKPHONE_NAME,COLUMN_BLOCKPHONE_PHONE,
                COLUMN_BLOCKPHONE_REASON},COLUMN_BLOCKPHONE_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null
                );
        if(cursor!=null){
            cursor.moveToFirst();
        }
        BlockPhone blockPhone = new BlockPhone(id,cursor.getString(1),cursor.getString(2),cursor.getString(3));
        return blockPhone;
    }
    public int updateBlockPhone(BlockPhone blockPhone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BLOCKPHONE_NAME,blockPhone.getName());
        values.put(COLUMN_BLOCKPHONE_PHONE,blockPhone.getNumber());
        values.put(COLUMN_BLOCKPHONE_REASON,blockPhone.getReason());
        return db.update(TABLE_NAME,values,
                COLUMN_BLOCKPHONE_ID+"=?",
                new String[]{String.valueOf(blockPhone.getId())});
    }
    public void deleteBlockPhone(BlockPhone blockPhone){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_BLOCKPHONE_ID+"=?",new String[]{String.valueOf(blockPhone.getId())});
        db.close();
    }
    public int getCount(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(sql,null);
        int notesCount = cursor.getCount();
        cursor.close();
        return notesCount;
    }
    public void createDefaultBlockPhoneifNeed(){
        Log.d("SQL","Default");
        int count = getCount();
        if(count==0){
            BlockPhone blockPhone1 = new BlockPhone("Nguyễn Công Y","01649077628","Quấy rối giất ngủ");
            BlockPhone blockPhone2 = new BlockPhone("Lê Văn Tèo","01649077628","Gét là chặn");
            addBlockPhone(blockPhone1);
            addBlockPhone(blockPhone2);
        }
    }
    public boolean checkBlockPhoneByNumber(String number){
//        SQLiteDatabase db = getReadableDatabase();
//        String sql  = "SELECT * FROM "+TABLE_NAME + " WHERE "+COLUMN_BLOCKPHONE_PHONE+"="+number;
//        Cursor cursor = db.rawQuery(sql,null);
        return true;
    }
}
