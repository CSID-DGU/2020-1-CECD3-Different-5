package com.example.whatthe;

import android.app.ProgressDialog;
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

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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


public class Frag_Second extends Fragment {
    private View view;

    private static String TAG = "phptest";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_TIMESTAMP = "timestamp";
    private static final String TAG_EMOTION = "emotion";
    private static final String TAG_BLINK ="blink";
    private static final String TAG_GAZE = "gaze";
    private static final String TAG_SLOPE = "slope";
    private static final String TAG_RESULT ="result";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;

    HorizontalBarChart cctchart, emochart;
    int UnCct = 0xFFC1E6FF, Cct = 0xFF508BE0;
    int[] color;
    float[] always;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater Inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = Inflater.inflate(R.layout.second_study, container, false);
/*
        mTextViewResult = view.findViewById(R.id.textView_main_result);
        mlistView = view.findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();
        GetData task = new GetData();
        task.execute("http://192.168.113.14/getjson.php"); //IP 주소 변경

        cctchart = (HorizontalBarChart) view.findViewById(R.id.concentrationChart);

        cctchart.setDescription(null);
        cctchart.getAxisRight().setDrawLabels(false);
        cctchart.getXAxis().setDrawLabels(false);
        cctchart.getLegend().setEnabled(false);
*/

        return view;
    }

    private class GetData extends AsyncTask<String, Void, String>{
        String errorString = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            mTextViewResult.setText(result);
            Log.d(TAG, "response - "+result);

            if(result == null){
                mTextViewResult.setText(errorString);
            }else{
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = params[0];

            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();
            }catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    //참고 http://blog.daum.net/techtip/12415218
    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int jsonLength = jsonArray.length();

            color = new int[jsonLength];
            always = new float[jsonLength];
            for(int i = 0;i<jsonLength;i++){
                always[i] = 5f;
            }

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String emotion = item.getString(TAG_EMOTION);
                String blink = item.getString(TAG_BLINK);
                String gaze = item.getString(TAG_GAZE);

                if(item.getString(TAG_RESULT).contains("0")) color[i] = UnCct;
                else color[i] = Cct;

                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_EMOTION, emotion);
                hashMap.put(TAG_BLINK, blink);
                hashMap.put(TAG_GAZE, gaze);

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getActivity().getApplicationContext(), mArrayList, R.layout.item_list,
                    new String[]{TAG_EMOTION,TAG_BLINK, TAG_GAZE},
                    new int[]{R.id.textView_list_id, R.id.textView_list_name, R.id.textView_list_address}
            );

            setCctGraph();

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    private void setCctGraph(){
        ArrayList<BarEntry> cct = new ArrayList<BarEntry>();
        cct.add(new BarEntry(always,0));

        ArrayList year = new ArrayList();
        year.add("2008");

        BarDataSet bardataset = new BarDataSet(cct, "");
        cctchart.animateX(5000);
        BarData data = new BarData(year, bardataset);
        data.setDrawValues(false);
        bardataset.setColors(color);
        cctchart.setData(data);
    }
}
