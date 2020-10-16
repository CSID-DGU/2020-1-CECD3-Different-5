package com.example.whatthe;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FragDash extends Fragment {
    private View view;

    private static String TAG = "phptest";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_TIMESTAMP = "timestamp";
    private static final String TAG_ROUND = "round";
    private static final String TAG_TOTALTIME ="totalTime";
    private static final String TAG_SCORE = "score";
    private static final String TAG_FEEDBACK = "feedback";

    private TextView mTextViewResult;
    String mJsonString;
    ArrayList<HashMap<String, studyData>> mArrayList;
    ArrayList<String> dateArray;

    private LineChart chart;
    ArrayList<Entry> values;
    ArrayList<String> xVals;

    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = Inflater.inflate(R.layout.frag_dashboard, container, false);

        userId = getArguments().getString("userId");

        chart = (LineChart) view.findViewById(R.id.Lchart);//layout의 id

        mTextViewResult = view.findViewById(R.id.textView_main_result);
        FragDash.GetData task = new FragDash.GetData();

        task.execute("http://192.168.0.27/dashquery.php?table="+userId); //IP 주소 변경

        return view;
    }

    private class GetData extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null) {
                mTextViewResult.setText(errorString);
            } else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();
            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    class studyData{
        float dscore;
        String dtotalTime, dfeedback;

        studyData(String t, float s, String f){
            this.dtotalTime = t;
            this.dscore = s;
            this.dfeedback = f;
        }
    }

    //참고 http://blog.daum.net/techtip/12415218
    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                HashMap<String, studyData> hashMap = new HashMap<>();
                hashMap.put(item.getString(TAG_TIMESTAMP),new studyData(item.getString(TAG_TOTALTIME),item.getInt(TAG_SCORE),item.getString(TAG_FEEDBACK)));

                String d = item.getString(TAG_TIMESTAMP);

                dateArray = new ArrayList<>();
                mArrayList = new ArrayList<>();

                dateArray.add(d);
                mArrayList.add(hashMap);
            }

            setCctGraph();

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void setCctGraph() {

        values = new ArrayList<>();
        xVals = new ArrayList<String>();

        for(int i = 0;i<dateArray.size();i++){
            String date = dateArray.get(i);
            studyData s = mArrayList.get(i).get(date);
            values.add(new Entry(s.dscore, i));
            xVals.add(date);
        }

        //values.add(new Entry(Float.parseFloat(item.getString(TAG_SCORE)), i));

        LineDataSet set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.invalidate();
    }

}
