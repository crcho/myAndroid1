package com.lgcns.dcxandroid;

import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChartAdapter extends BaseAdapter {

    private Context context;
    private Resources res;
    private ArrayList<ChartItem> list;
    private LayoutInflater inflater;

    public ChartAdapter(Context c, ArrayList<ChartItem> list) {
        this.list = list;
        this.context = c;
        this.res = c.getResources();
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        ChartItem item = list.get(position);
        LinearLayout returnView = (LinearLayout)inflater.inflate(R.layout.adapter_chart, null);

        TextView txtRank = (TextView)returnView.findViewById(R.id.adapter_txt_no);
        txtRank.setText(String.valueOf(item.rank));

        ImageView imgAlbum = (ImageView)returnView.findViewById(R.id.adapter_img_album);
        int imgResId = res.getIdentifier(StringUtil.getFileNameWithoutExt(list.get(position).imageFile), "drawable", context.getPackageName());
        imgAlbum.setImageResource(imgResId);

        TextView txtTitle = (TextView)returnView.findViewById(R.id.adapter_txt_title);
        txtTitle.setText(item.title);

        TextView txtArtist = (TextView)returnView.findViewById(R.id.adapter_txt_artist);
        txtArtist.setText(item.singer);

        return returnView;

    }
}
