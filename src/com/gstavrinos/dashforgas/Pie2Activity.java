package com.gstavrinos.dashforgas;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

public class Pie2Activity extends ActionBarActivity {

	private TextView donutSizeTextView;
	private SeekBar donutSizeSeekBar;

	private PieChart pie;

	private Segment s1;
	private Segment s2;
	private Segment s3;
	private Segment s4;
	private Segment s5;
	private Segment s6;
	private Segment s7;
	private Segment s8;
	private Segment s9;
	private int s1_value, s2_value, s3_value, s4_value, s5_value, s6_value, s7_value, s8_value, s9_value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pie2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.dashforgas);
		// initialize our XYPlot reference:
		pie = (PieChart) findViewById(R.id.mySimplePieChart);
		Intent intent = getIntent();
		s1_value = Integer.parseInt(intent.getStringExtra("a"));
		s2_value = Integer.parseInt(intent.getStringExtra("km"));
		s3_value = Integer.parseInt(intent.getStringExtra("t"));
		s4_value = Integer.parseInt(intent.getStringExtra("k"));
		s6_value = Integer.parseInt(intent.getStringExtra("de"));
		s7_value = Integer.parseInt(intent.getStringExtra("dm"));
		s8_value = Integer.parseInt(intent.getStringExtra("i"));
		s9_value = Integer.parseInt(intent.getStringExtra("p"));

		donutSizeSeekBar = (SeekBar) findViewById(R.id.donutSizeSeekBar);

		donutSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				pie.getRenderer(PieRenderer.class).setDonutSize(seekBar.getProgress()/100f,
						PieRenderer.DonutMode.PERCENT);
				pie.redraw();
				updateDonutText();
			}
		});

		donutSizeTextView = (TextView) findViewById(R.id.donutSizeTextView);
		updateDonutText();

		s1 = new Segment("ATTIKI", s1_value);
		s2 = new Segment("KENTRIKI MAKEDONIA", s2_value);
		s3 = new Segment("THESSALIA", s3_value);
		s4 = new Segment("KRITI", s4_value);
		s5 = new Segment("ANATOLIKI MAKEDONIA KAI THRAKI", s5_value);
		s6 = new Segment("DYTIKI ELLADA", s6_value);
		s7 = new Segment("DYTIKI MAKEDONIA", s7_value);
		s8 = new Segment("IPEIROS", s8_value);
		s9 = new Segment("PELOPONNISOS", s9_value);

		EmbossMaskFilter emf = new EmbossMaskFilter(
				new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

		SegmentFormatter sf1 = new SegmentFormatter();
		sf1.configure(getApplicationContext(), R.xml.pie_segment_formatter1);

		sf1.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf2 = new SegmentFormatter();
		sf2.configure(getApplicationContext(), R.xml.pie_segment_formatter2);

		sf2.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf3 = new SegmentFormatter();
		sf3.configure(getApplicationContext(), R.xml.pie_segment_formatter3);

		sf3.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf4 = new SegmentFormatter();
		sf4.configure(getApplicationContext(), R.xml.pie_segment_formatter4);

		sf4.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf5 = new SegmentFormatter();
		sf5.configure(getApplicationContext(), R.xml.pie_segment_formatter5);

		sf5.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf6 = new SegmentFormatter();
		sf6.configure(getApplicationContext(), R.xml.pie_segment_formatter6);

		sf6.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf7 = new SegmentFormatter();
		sf7.configure(getApplicationContext(), R.xml.pie_segment_formatter7);

		sf7.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf8 = new SegmentFormatter();
		sf8.configure(getApplicationContext(), R.xml.pie_segment_formatter8);

		sf8.getFillPaint().setMaskFilter(emf);

		SegmentFormatter sf9 = new SegmentFormatter();
		sf9.configure(getApplicationContext(), R.xml.pie_segment_formatter9);

		sf9.getFillPaint().setMaskFilter(emf);

		pie.addSeries(s1, sf1);
		pie.addSeries(s2, sf2);
		pie.addSeries(s3, sf3);
		pie.addSeries(s4, sf4);
		//pie.addSeries(s5, sf5);
		pie.addSeries(s6, sf6);
		//pie.addSeries(s7, sf7);
		pie.addSeries(s8, sf8);
		pie.addSeries(s9, sf9);

		pie.getBorderPaint().setColor(Color.TRANSPARENT);
		pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pie2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}


	protected void updateDonutText() {
		donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
	}
}
