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
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class Graph extends Activity {
	public int xMax = 30;
	public int yMax = 120;
	private DBHandler db;
	public final String FIRST = "first_try";
	public final String SECOND = "second_try";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Get info from the DB
        db = new DBHandler(this);
        db.open();
        Cursor c1 = db.fetchAllRecords(FIRST);
        Cursor c2 = db.fetchAllRecords(SECOND);
        System.out.println("Number of Records: "+c1.getCount());
        

        

        String[] titles = getResources().getStringArray(R.array.series);
        List<Date[]> x = new ArrayList<Date[]>();
        List<double[]> values = new ArrayList<double[]>();
        int count = 20;
        int length = titles.length;
        DateFormat df = DateFormat.getDateInstance();
        Date day;
        int time;
        
        //X-axis limit variables
        long xMin = 0;
        long xMax=0;
       
        //Add First Try values
          Date [] x1Values = new Date[c1.getCount()];
          double[] y1Values = new double[c1.getCount()];
          
          
          for(int i=0; i<c1.getCount(); i++){
        	  c1.moveToPosition(i);
        	   try {
				//day = new Date(df.parse(c1.getString(1)).getYear(),df.parse(c1.getString(1)).getMonth(), df.parse(c1.getString(1)).getDay());
        		   day = df.parse(c1.getString(1));
        	   time = c1.getInt(2);
        	   
        	   System.out.println("PAIR: "+day+","+time);
        	   x1Values[i] = day;
        	   y1Values[i] = time;
   			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }

          //Set the X-axis values if x1values exist
          if(c1.getCount()>0){
        	  xMin = x1Values[0].getTime();
        	  xMax = x1Values[x1Values.length-1].getTime();
          }
          
          x.add(x1Values);
          values.add(y1Values);
        

          
          //Add Second Try Values
          Date [] x2Values = new Date[c2.getCount()];
          double[] y2Values = new double[c2.getCount()];
          
          for(int i=0; i<c2.getCount(); i++){
        	  c2.moveToPosition(i);
        	   try {
				day = df.parse(c2.getString(1));

        	   time = c2.getInt(2);
        	   
        	   System.out.println("PAIR: "+day+","+time);
        	   x2Values[i] = day;
        	   y2Values[i] = time;
   			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
          
          //Check X-axis values of x2Values against current settings
          if(c2.getCount()>0){
        	  if(x2Values[0].getTime()<xMin){
        		  xMin = x2Values[0].getTime();
        	  }
        	  if(x2Values[x2Values.length-1].getTime()>xMax){
        		  xMax=x2Values[x2Values.length-1].getTime();
        	  }
          }
        	  
          
          x.add(x2Values);
          values.add(y2Values);
          

          
        int[] colors = new int[] { 0XFF006AFF, 0XFFFF4520};
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.TRIANGLE,};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

        

        setChartSettings(renderer, "Kick Times", "Date", "Minutes", xMin, xMax+14440000, 0, yMax, Color.WHITE,
                Color.LTGRAY);
            renderer.setXLabels(10);
            renderer.setYLabels(10);
            length = renderer.getSeriesRendererCount();
            for (int i = 0; i < length; i++) {
              ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
             //This line gets rid of the lines between the points.  0f does not.
              ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setLineWidth(0.00001f);
              ((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setFillBelowLine(true);
              ((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setFillBelowLineColor(0xff8fbeff);

            }

         setContentView(ChartFactory.getTimeChartView(this, buildDataset(titles, x, values), renderer, "ddMMM"));
 
         //Close the DB
         c1.close();
         c2.close();
         db.close();
    }
    
    @Override
    public void onBackPressed() {
    	Intent i = new Intent(Graph.this, Kicks.class);
        startActivity(i);
       return;
    }
    
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(10);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(8f);
       
        renderer.setMargins(new int[] { 20, 30, 15, 10 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
          XYSeriesRenderer r = new XYSeriesRenderer();
          r.setColor(colors[i]);
          r.setPointStyle(styles[i]);
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

    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<Date[]> xValues,
    	      List<double[]> yValues) {
    	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    	    int length = titles.length;
    	    for (int i = 0; i < length; i++) {
    	      TimeSeries series = new TimeSeries(titles[i]);
    	      Date[] xV = xValues.get(i);
    	      double[] yV = yValues.get(i);
    	      int seriesLength = xV.length;
    	      for (int k = 0; k < seriesLength; k++) {
    	        series.add(xV[k], yV[k]);
    	      }
    	      dataset.addSeries(series);
    	    }
    	    return dataset;
    	  }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.graph_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.alarm:
        	goAlarm();
            return true;
        case R.id.notification:
          
            return true;
        case R.id.home:
            goHome();
            return true;             
        case R.id.delete_data:
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void goAlarm(){
    	Intent i = new Intent(Graph.this, Alarm.class);
    	startActivity(i);
    }
    
    public void goHome(){
    	Intent i = new Intent(Graph.this, Kicks.class);
    	startActivity(i);
    }
    

}
