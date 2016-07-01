package com.smsfilter.congybk.smsfilter;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by congybk on 22/06/2016.
 */
public class BlockPhoneFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<BlockPhone> mListBlockPhone = new ArrayList<>();
    private NotificationCompat.Builder mNotificationBuilder;
    private static final int MY_NOTIFICATION_ID = 12345;
    private static final int MY_REQUEST_CODE = 100;
    private View fragmentView;
    private DatabaseHelper mDB;
    private BlockPhoneAdapter mAdapter = null;
    private ActionMode mActionMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_block_list, container, false);
        mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rcv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDB = new DatabaseHelper(getActivity());
        mDB.createDefaultBlockPhoneifNeed();
        List<BlockPhone> listBlockPhones = mDB.getAllBlockPhones();
        mListBlockPhone.addAll(listBlockPhones);
        mAdapter = new BlockPhoneAdapter(getActivity(), mListBlockPhone, new BlockPhoneAdapter.OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                  showPopupMenu(view,position);

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return  fragmentView;
    }
    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuItemClickListener(position));
        popup.show();
    }
    public class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int mPosition;
        public MenuItemClickListener(int position){
            this.mPosition=position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.deEdit:
                    Intent intent = new Intent(getActivity(),AddBlockPhone.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PHONEBLOCK",mListBlockPhone.get(mPosition));
                    intent.putExtra("PACKAGE",bundle);
                    startActivity(intent);
                    break;
                case R.id.inDelete:
                    mDB.deleteBlockPhone(mListBlockPhone.get(mPosition));
                    mListBlockPhone.remove(mPosition);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    }

}
