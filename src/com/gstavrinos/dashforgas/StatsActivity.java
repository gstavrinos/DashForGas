package com.gstavrinos.dashforgas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.google.android.gms.maps.model.LatLng;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class StatsActivity extends ActionBarActivity {

	private XYPlot plot;
	LatLng Athens;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xy_plot);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.dashforgas);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		Athens = new LatLng(37.983913, 23.729362);
		
		createPlot();
	}

	private void createPlot(){		
		//Domain = x
		//Range = y
		final XYSeries series1 = new SimpleXYSeries(new ArrayList<Integer>(MainActivity.distance_amount.keySet()),new ArrayList<Integer>(MainActivity.distance_amount.values()),"s");
		final LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_plf);
        plot.addSeries(series1, series1Format);
        plot.setRangeBottomMin(0);
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 10);
        plot.setRangeValueFormat(new DecimalFormat("#"));
        plot.setDomainValueFormat(new DecimalFormat("#"));
        plot.getGraphWidget().setDomainLabelOrientation(0);
        plot.getGraphWidget().setRangeLabelOrientation(0);
        plot.getLegendWidget().setVisible(false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
