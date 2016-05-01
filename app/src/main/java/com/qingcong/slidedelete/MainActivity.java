package com.qingcong.slidedelete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private SlideDeleteListView listView;

    private List<String> list = new ArrayList<String>();

    private MainAdapter m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (SlideDeleteListView) findViewById(R.id.listview);
        m = new MainAdapter();
        listView.setAdapter(m);
        list.add("刘德华");
        list.add("张学友");
        list.add("黎明");
        list.add("郭富城");
        list.add("整除");
        list.add("垃圾");
        list.add("历经阿卡");
        list.add("哈哈");
        list.add("我是我是");
        list.add("国荣");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("content", list.get(position));
                startActivity(intent);
            }
        });
        listView.setOnDeleteListener(new SlideDeleteListView.onDeleteListener() {
            @Override
            public void onDelete(int p) {
                list.remove(p);
                m.notifyDataSetChanged();
            }
        });
    }

    class MainAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_list_item, null);
            }
            ((TextView)convertView.findViewById(R.id.content_id)).setText("" + list.get(position));
            return convertView;
        }
    }


}
