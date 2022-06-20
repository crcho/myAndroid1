package com.lgcns.dcxandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String url = "http://10.0.2.2:3300/v1/chart/";
    private final String CHART_TYPE_DOMESTIC = "domestic";
    private final String CHART_TYPE_OVERSEAS = "overseas";

    private ArrayList<ChartItem> list;
    private TextView dateTitle, menu1, menu2;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        dateTitle = (TextView)findViewById(R.id.main_txt_date);
        dateTitle.setText(StringUtil.getCurrentDateTime());

        listView = (ListView)findViewById(R.id.main_list);
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("id", list.get(position).id);
            startActivity(intent);

        });

        menu1 = (TextView)findViewById(R.id.main_txt_menu_1);
        menu2 = (TextView)findViewById(R.id.main_txt_menu_2);

        View.OnClickListener menuClickListener = view -> {
            if (view.getId() == R.id.main_txt_menu_1) {
                menu1.setTextColor(Color.RED);
                menu2.setTextColor(Color.BLACK);
                new GetChartTask().execute(CHART_TYPE_DOMESTIC);

            } else {
                menu1.setTextColor(Color.BLACK);
                menu2.setTextColor(Color.RED);
                new GetChartTask().execute(CHART_TYPE_OVERSEAS);

            }

        };

        menu1.setOnClickListener(menuClickListener);
        menu2.setOnClickListener(menuClickListener);


        // AsyncTask 가동한다. (가변형 매개변수 ...)
        new GetChartTask().execute(CHART_TYPE_DOMESTIC);

    }

    private class GetChartTask extends AsyncTask<String, Void, ArrayList<ChartItem>> {

        @Override
        protected ArrayList<ChartItem> doInBackground(String... chartType) {
            ArrayList<ChartItem> resultList = new ArrayList<>();
            HttpURLConnection conn = null;
            BufferedReader br = null;
            InputStreamReader isr = null;

            try {
                URL url = new URL(MainActivity.this.url + chartType[0]);
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


                    // JSON 배열 객체를 생성한다 -> 스트링 배열로 넘어온것이므로
                    JSONObject mainJSON = new JSONObject(sb.toString());
                    JSONArray mainJSONArray = mainJSON.getJSONArray("chartList");

                    for (int inx = 0; inx < mainJSONArray.length(); inx++) {
                        JSONObject obj = mainJSONArray.getJSONObject(inx);
                        ChartItem item = new ChartItem();
                        item.id = obj.getInt("id");
                        item.rank = obj.getInt("rank");
                        item.title = obj.getString("title");
                        item.singer = obj.getString("singer");
                        item.imageFile = obj.getString("imageUrl"); //예제 문제에서는 imageFile이라고 되어 있으나, 예제 JSON은 imageUrl로 되어있음.
                        resultList.add(item);

                    } //json parse end

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

            // doinBackground 매서드가 종료되면서 메인쓰레드가 호출하는 onPostExecute 매서드로 결과값을 넘긴다
            return resultList;
        }


        // doinBackground 매서드의 수행이 완료되면 메인 쓰레드가 호출하는 메서드
        // doinBackground 매서드가 반환하는 값을 매개변수로 받는다
        @Override
        protected void onPostExecute(ArrayList<ChartItem> chartItems) {
            MainActivity.this.list = chartItems;
            ChartAdapter adapter = new ChartAdapter(MainActivity.this, chartItems);
            listView.setAdapter(adapter);

        }

    }

}