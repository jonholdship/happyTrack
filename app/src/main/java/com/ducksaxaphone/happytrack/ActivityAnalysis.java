package com.ducksaxaphone.happytrack;

import android.os.Bundle;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ActivityAnalysis extends BaseActivity {
    @BindView(R.id.WeekChart) ColumnChartView weekChart;
    @BindView(R.id.HappyList) TextView happyList;
    @BindView(R.id.SadList) TextView sadList;
    private AnalysisDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_analysis);

        dataSource= new AnalysisDataSource(this);
        dataSource.open();
        ButterKnife.bind(this);
        createWeekChart();
        getHashtags();
        super.onCreate(savedInstanceState);
    }

    private void createWeekChart(){
        List<Float> dayRatings = dataSource.getWeekSummary();
        List<SubcolumnValue> dayValues;
        List<AxisValue> barLabels = new ArrayList<>();
        final String[] weekDays = new String[] { "Sun", "Mon", "Tues", "Wed", "Thurs" , "Fri", "Sat" };

        List<Column> columns = new ArrayList<Column>();


        //for this library, a bar chart is made by creating column objects and adding them to a list
        //each column object is made from a list of subcolumns. We make one subcolumn and add to a column
        //then add that column to list.
        //also add names of week to a list of x-axis labels at this point
        for (int i=0; i < 7;i++){
            //create a subcolumn.
            dayValues  = new ArrayList<SubcolumnValue>();
            SubcolumnValue dayValue = new SubcolumnValue(dayRatings.get(i), getResources().getColor(R.color.colorPrimary));
            dayValue.setLabel(dayRatings.get(i).toString());
            dayValues.add(dayValue);

            AxisValue axisValue = new AxisValue(i);
            axisValue.setLabel(weekDays[i]);
            barLabels.add(axisValue);
            Column column = new Column(dayValues);
            column.setHasLabels(true);
            columns.add(column);

        }

        ColumnChartData weekData = new ColumnChartData(columns);

        //BarDataSet dataSet = new BarDataSet(weekData, "Label"); // add entries to dataset
        //dataSet.setColor(getResources().getColor(R.color.colorPrimaryLight));
      //  dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark)); // styling, ...

      //  data.setBarWidth(0.9f); // set custom bar width

        //set Y Axis up
        Axis axisY = new Axis().setHasLines(true);
        weekData.setAxisYLeft(axisY);

        //set X Axis up
        Axis axisX = new Axis().setHasLines(false);
        axisX.setValues(barLabels);
        weekData.setAxisXBottom(axisX);


        weekChart.setColumnChartData(weekData);

        // formatWeekChart();
        weekChart.invalidate(); // refresh
    }

    private void getHashtags(){
        List<Hashtag> topList = new ArrayList<>();
        String listString = "";
        dataSource= new AnalysisDataSource(this);
        dataSource.open();
        topList=dataSource.getHashtagsSorted(true);

        System.out.println("good Days");
        for (int i=0; i< topList.size();i++){
            listString+=String.valueOf(i+1)+". "+topList.get(i).tag+"\n";
            System.out.println(topList.get(i).score);
        }
        happyList.setText(listString);

        System.out.println("bad days");
        topList=dataSource.getHashtagsSorted(false);
        listString="";
        for (int i=0; i< topList.size();i++){
            listString+=String.valueOf(i+1)+". "+topList.get(i).tag+"\n";
            System.out.println(topList.get(i).score);
        }
        sadList.setText(listString);

    }
}
