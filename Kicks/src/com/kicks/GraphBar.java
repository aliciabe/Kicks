package com.kicks;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;


public class GraphBar extends Activity {
	public int xMax = 30;
	public int yMax = 120;
	private DBHandler db;
	public final String FIRST = "first_try";
	public final String SECOND = "second_try";
	public final long DATEDIV = 86400000;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Get info from the DB
        db = new DBHandler(this);
        db.open();
        Cursor c1 = db.fetchAllRecords(FIRST);
        Cursor c2 = db.fetchAllRecords(SECOND);
        System.out.println("Number of Records: "+c1.getCount());
        

        

        String[] titles = getResources().getStringArray(R.array.series);
        List<long[]> x = new ArrayList<long[]>();
        List<double[]> values = new ArrayList<double[]>();
        int count = 20;
        int length = titles.length;
        DateFormat df = DateFormat.getDateInstance();
        Date day;
        int time;
        
        //X-axis limit variables
        long xMin = 0;
        long xMin1;
        long xMax1;
        long xMax=0;
       
        //Add First Try values
          long [] x1Values = new long[c1.getCount()];
          double[] y1Values = new double[c1.getCount()];
          

          try {

			c1.moveToFirst();
			c2.moveToFirst();
			//do min
			xMin = df.parse(c1.getString(1)).getTime();
			xMin1 = df.parse(c2.getString(1)).getTime();
			if(xMin1<xMin){
				xMin = xMin1;
			}
			
			//do max
	          c1.moveToLast();
	          c2.moveToLast();
			
			xMax = df.parse(c1.getString(1)).getTime();
			xMax1 = df.parse(c2.getString(1)).getTime();
			if(xMax1>xMax){
				xMax = xMax1;
			}
			
			xMax = (xMax-xMin)/DATEDIV;
			
			
		} catch (ParseException e1) {
			System.out.println("Could not parse date.");
			e1.printStackTrace();
		}
          
          for(int i=0; i<c1.getCount(); i++){
        	  c1.moveToPosition(i);
        	   try {
				//day = new Date(df.parse(c1.getString(1)).getYear(),df.parse(c1.getString(1)).getMonth(), df.parse(c1.getString(1)).getDay());
        		   day = df.parse(c1.getString(1));
        	   time = c1.getInt(2);
        	   

        	   x1Values[i] = (day.getTime()-xMin)/DATEDIV;
        	   y1Values[i] = time;
        	   System.out.println("PAIR: "+x1Values[i]+","+time);
   			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }

          
          x.add(x1Values);
          values.add(y1Values);
        

          
          //Add Second Try Values
          long [] x2Values = new long[c2.getCount()];
          double[] y2Values = new double[c2.getCount()];
          
          for(int i=0; i<c2.getCount(); i++){
        	  c2.moveToPosition(i);
        	   try {
				day = df.parse(c2.getString(1));

        	   time = c2.getInt(2);
        	   

        	   x2Values[i] = (day.getTime()-xMin)/DATEDIV;        	
        	   y2Values[i] = time;       
        	   
        	   System.out.println("PAIR: "+(day.getTime()-xMin)/DATEDIV+","+time);
   			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }    
        	  
          
          x.add(x2Values);
          values.add(y2Values);
          

          
        int[] colors = new int[] { Color.BLUE, Color.RED};
     //   PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.TRIANGLE,};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors);
        

        setChartSettings(renderer, "Kick Times", "Day", "Minutes", 0, xMax, 0, yMax, Color.WHITE,
                Color.LTGRAY);
            renderer.setXLabels(10);
            renderer.setYLabels(10);
            length = renderer.getSeriesRendererCount();
            renderer.setDisplayChartValues(true);
            renderer.setBarSpacing(1);
            renderer.setBackgroundColor(Color.WHITE);



         setContentView(ChartFactory.getBarChartView(this, buildDataset(titles, x, values), renderer, Type.STACKED));
 
         //Close the DB
         c1.close();
         c2.close();
         db.close();
    }
    
    @Override
    public void onBackPressed() {
    	Intent i = new Intent(GraphBar.this, Kicks.class);
        startActivity(i);
       return;
    }
    
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);

        int length = colors.length;
        for (int i = 0; i < length; i++) {
          SimpleSeriesRenderer r = new SimpleSeriesRenderer();
          r.setColor(colors[i]);
          renderer.addSeriesRenderer(r);
        }
        return renderer;
      }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
    	      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
    	      int labelsColor) {
    	    renderer.setChartTitle(title);
    	    renderer.setXTitle(xTitle);
    	    renderer.setYTitle(yTitle);
    	    renderer.setXAxisMin(xMin);
    	    renderer.setXAxisMax(xMax);
    	    renderer.setYAxisMin(yMin);
    	    renderer.setYAxisMax(yMax);
    	    renderer.setAxesColor(axesColor);
    	    renderer.setLabelsColor(labelsColor);
    	  }

    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
          CategorySeries series = new CategorySeries(titles[i]);
          double[] v = values.get(i);
          int seriesLength = v.length;
          for (int k = 0; k < seriesLength; k++) {
            series.add(v[k]);
          }
          dataset.addSeries(series.toXYSeries());
        }
        return dataset;
      }
    
    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<long[]> xValues,
    	      List<double[]> yValues) {
    	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    	    int length = titles.length;
    	    for (int i = 0; i < length; i++) {
    	      XYSeries series = new XYSeries(titles[i]);
    	      long[] xV = xValues.get(i);
    	      double[] yV = yValues.get(i);
    	      int seriesLength = xV.length;
    	      for (int k = 0; k < seriesLength; k++) {
    	        series.add(xV[k], yV[k]);
    	      }
    	      dataset.addSeries(series);
    	    }
    	    return dataset;
    	  }
}
