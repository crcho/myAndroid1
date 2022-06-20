package com.lgcns.dcxandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private final String url = "http://10.0.2.2:3300/v1/chart/detail/";
    private int selectedId;
    private ImageView imgBack;
    private TextView txtTitle, txtArtist, txtMelodizer, txtLyricist, txtGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        selectedId = intent.getIntExtra("id", 0);

        init();

    }

    private void init() {
        imgBack = (ImageView)findViewById(R.id.detail_img_back);
        imgBack.setOnClickListener(view -> finish());

        txtTitle = (TextView)findViewById(R.id.detail_txt_main_title);
        txtArtist = (TextView)findViewById(R.id.detail_txt_sub_title);
        txtMelodizer = (TextView)findViewById(R.id.detail_tbl_txt_content1);
        txtLyricist = (TextView)findViewById(R.id.detail_tbl_txt_content2);
        txtGenre = (TextView)findViewById(R.id.detail_tbl_txt_content3);

        new GetDetailTask().execute(selectedId);

    }

    private class GetDetailTask extends AsyncTask<Integer, Void, DetailItem> {

        @Override
        protected DetailItem doInBackground(Integer... id) {
            DetailItem resultItem = new DetailItem();
            HttpURLConnection conn = null;
            BufferedReader br = null;
            InputStreamReader isr = null;

            try {
                URL url = new URL(DetailActivity.this.url + id[0]);
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                StringBuilder sb = new StringBuilder();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    isr = new InputStreamReader(conn.getInputStream(), "utf-8");
                    br = new BufferedReader(isr);
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    JSONObject tmpJSON = new JSONObject(sb.toString());
                    JSONObject mainJSON = tmpJSON.getJSONObject("chart");

                    resultItem.id = mainJSON.getInt("id");
                    resultItem.title = mainJSON.getString("title");
                    resultItem.singer = mainJSON.getString("singer");
                    resultItem.melodizer = mainJSON.getString("melodizer");
                    resultItem.lyricist = mainJSON.getString("lyricist");
                    resultItem.genre = mainJSON.getString("genre");

                } //HTTP_OK end

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    if (isr != null) {
                        isr.close();
                    }
                    if (br != null) {
                        br.close();
                    }

                } catch (Exception e) {
                    //do nothing
                }
            }

            return resultItem;
        }

        @Override
        protected void onPostExecute(DetailItem resultItem) {
            txtTitle.setText(resultItem.title);
            txtArtist.setText(resultItem.singer);
            txtMelodizer.setText(resultItem.melodizer);
            txtLyricist.setText(resultItem.lyricist);
            txtGenre.setText(resultItem.genre);

        }

    }

}
