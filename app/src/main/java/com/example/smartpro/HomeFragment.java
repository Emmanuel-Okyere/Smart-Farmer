package com.example.smartpro;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartpro.network.ChannelData;
import com.example.smartpro.network.FeedData;
import com.example.smartpro.network.Results;
import com.example.smartpro.network.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    LineChart lineChart,lineChart1;
    Button refresh;
    ArrayList<Entry> x;
    ArrayList<String> y;
    ArrayList<Entry> x1;
    ArrayList<String> y1;
    public static List<FeedData> entry;
    public static List entry_time;
    public static List created_at;
    public static List feild1;
    public static List feild2;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View myview= inflater.inflate(R.layout.fragment_home, container,false);
        lineChart = myview.findViewById(R.id.feild1chart);
        lineChart1= myview.findViewById(R.id.feild2chart);
        refresh = myview.findViewById(R.id.refreshBtn);
        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        x1 = new ArrayList<Entry>();
        y1 = new ArrayList<String>();
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDescriptionColor(Color.WHITE);


        lineChart1.setDrawGridBackground(false);
        lineChart1.setTouchEnabled(true);
        lineChart1.setDragEnabled(true);
        lineChart1.setScaleEnabled(true);
        lineChart1.setPinchZoom(true);
        lineChart1.setDescriptionColor(Color.WHITE);
        //First Data
        XAxis xl = lineChart.getXAxis();
        xl.setAvoidFirstLastClipping(false);
        xl.setTextColor(Color.WHITE);

        YAxis rightAxis = lineChart.getAxisLeft();
        rightAxis.setEnabled(true);
        rightAxis.setAxisMinValue(0);
        rightAxis.setTextColor(Color.WHITE);
        YAxis leftAxis = lineChart.getAxisRight();
        leftAxis.setEnabled(false);
        Legend l = lineChart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setForm(Legend.LegendForm.SQUARE);

        //Second data
        XAxis xl1 = lineChart1.getXAxis();
        xl1.setTextColor(Color.WHITE);
        xl1.setAvoidFirstLastClipping(false);
        YAxis rightAxis1 = lineChart1.getAxisLeft();
        rightAxis1.setEnabled(true);
        rightAxis1.setAxisMinValue(0);
        YAxis leftAxis1 = lineChart1.getAxisRight();
        leftAxis1.setEnabled(false);
        rightAxis1.setTextColor(Color.WHITE);
        Legend l1 = lineChart1.getLegend();
        l1.setTextColor(Color.WHITE);
        l1.setForm(Legend.LegendForm.SQUARE);
        getAllData();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllData();
            }
        });
        return myview;
    }

    public void getAllData() {
        Call<Results> call = RetrofitClient.getInstance().getMyApi().getAllData();
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                ArrayList<FeedData> data =  response.body().getFeedDataList();

                for(int i=0; i<data.size();i++){

                    String createdAt= data.get(i).getCreated_at();
                    int feed= data.get(i).getField1()*100/1023;
                    x.add(new Entry(feed,i));
                    y.add(createdAt);
                    System.out.println("Created at: "+data.get(i).getField2());
                    System.out.println("Created at: "+data.get(i).getCreated_at());
                    LineDataSet lineDataSet1= new LineDataSet(x,"Soil moisture from sensor 1 in %");
                    lineDataSet1.setLineWidth(1.5f);
                    lineDataSet1.setCircleRadius(4f);
                    lineDataSet1.setColor(Color.RED);
                    lineDataSet1.setValueTextColor(Color.WHITE);
                    lineDataSet1.setCircleColor(Color.WHITE);

                    LineData data1= new LineData( y,lineDataSet1);
                    lineChart.setData(data1);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                }
                //Getting data fro chart

                //For chart2
                for(int i=0; i<data.size();i++){
                    int feed1= data.get(i).getField2()*100/1023;
                    String createdAt= data.get(i).getCreated_at();
                    x1.add(new Entry(feed1,i));
                    y1.add(createdAt);
                    LineDataSet lineDataSet2= new LineDataSet(x1,"Soil moisture from sensor 2 in %");
                    lineDataSet2.setLineWidth(1.5f);
                    lineDataSet2.setCircleRadius(4f);
                    lineDataSet2.setColor(Color.RED);
                    lineDataSet2.setValueTextColor(Color.WHITE);
                    lineDataSet2.setCircleColor(Color.WHITE);
                    LineData data2= new LineData( y1,lineDataSet2);
                    lineChart1.setData(data2);
                    lineChart1.notifyDataSetChanged();
                    lineChart1.invalidate();
                }
                Toast.makeText(getContext(),"Data fetch successful", Toast.LENGTH_SHORT).show();





                /*// Get the Enumeration object
                //    Enumeration<FeedData> e = Collections.enumeration(data);
                for ( int i=0 ;i < 100 ; i++){
                    entry_time = Collections.singletonList(data.get(i).getEntry_id());

                    System.out.println("Data: " + data.get(i).getEntry_id());

                }
                for(int i=0; i<100;i++){
                    created_at =Collections.singletonList(data.get(i).getCreated_at());
                    System.out.println("Created at: "+data.get(i).getCreated_at());
                }

                for(int i=0; i<100;i++){
                    feild1 =Collections.singletonList(data.get(i).getField1());
                    System.out.println("Feild1: "+data.get(i).getField1());
                }
                for(int i=0; i<100;i++){
                    feild2 =Collections.singletonList(data.get(i).getField2());
                    System.out.println("Feild2: "+data.get(i).getField2());
                }*/


                // Enumerate through the ArrayList elements

                //System.out.println("Data" +  );
//                Iterator iter = data.get(1).iterator();
//                while (iter.hasNext()) {
//                    System.out.println(iter.next());
//                }
            }
            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText(requireActivity(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void getAllResultData() {
//        Call<Results> call = RetrofitClient.getInstance().getMyApi().getResultData("2");
//        call.enqueue(new Callback<Results>() {
//            @Override
//            public void onResponse(Call<Results> call, Response<Results> response) {
//                response.body().getChannelDataList();
//                System.out.println("Data2 "+response.body().getChannelDataList());
//            }
//            @Override
//            public void onFailure(Call<Results> call, Throwable t) {
//                Toast.makeText(requireActivity(), "An error has occurred", Toast.LENGTH_LONG).show();
//            }
//        });
//    }


}
