package com.almutlaq.realstate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.almutlaq.realstate.R;
import com.almutlaq.realstate.dto.CalendrdatelistModel;
import com.almutlaq.realstate.fragment.selfserviceuserFragments.SelfServiceHomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class CalendarGridCellAdapter extends BaseAdapter implements OnClickListener {
	private Context _context;
	private List<String> list;
	private static final int DAY_OFFSET = 1;
	private  String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private  String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private  String[] monthsNumeric = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
	int check=0;
	private  int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private int daysInMonth;
	private int currentDayOfMonth;
	private int currentWeekDay;
	private TextView gridcell;
	private int currentMonth;
	private int year,month,crnt_month,crnt_yr;
	private TextView num_events_per_day;
	private HashMap<String, Integer> eventsPerMonthMap;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MMM-dd");
	private ArrayList<CalendrdatelistModel> dt_list;
	SelfServiceHomeFragment selfServiceHomeFragment;
//	String user;
	public CalendarGridCellAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<CalendrdatelistModel> dt_list,SelfServiceHomeFragment selfServiceHomeFragment)
	{
		super();
		this._context = context;
		this.list = new ArrayList<String>();
		this.month=month;
		this.year=year;
		this.dt_list=dt_list;
	this.selfServiceHomeFragment = selfServiceHomeFragment;
//		this.user=user;
		Calendar calendar = Calendar.getInstance();
		setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
		setCurrentMonth(calendar.get(Calendar.MONTH)+1);
		setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
		crnt_month=calendar.get(Calendar.MONTH)+1;
		crnt_yr=calendar.get(Calendar.YEAR);
		// Print Month
		printMonth(month, year);

		// Find Number of Events
		eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
	}
	
	private void printMonth(int mm, int yy)
	{
		int trailingSpaces = 0;
		int leadSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;

		int currentMonth = mm - 1;
		daysInMonth = getNumberOfDaysOfMonth(currentMonth);

		// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
		GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
		//Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

		if (currentMonth == 11)
		{
			prevMonth = currentMonth - 1;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 0;
			prevYear = yy;
			nextYear = yy + 1;
		}
		else if (currentMonth == 0)
		{
			prevMonth = 11;
			prevYear = yy - 1;
			nextYear = yy;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 1;
		}
		else
		{
			prevMonth = currentMonth - 1;
			nextMonth = currentMonth + 1;
			nextYear = yy;
			prevYear = yy;
			daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
		}

		int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		trailingSpaces = currentWeekDay;

		if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
		{
			++daysInMonth;
		}
		for (int i = 0; i < trailingSpaces; i++)
		{
			list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
		}

		for (int i = 1; i <= daysInMonth; i++)
		{
			if (i == getCurrentDayOfMonth())
			{
				list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
			}
			else
			{
				
				list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
		
			}
		}

		// Leading Month days
		for (int i = 0; i < list.size() % 7; i++)
		{
			//		Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
			String newDay = "";
			list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
			//list.add(newDay + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
		}
	}
	
	public int getCurrentDayOfMonth()
	{
		return currentDayOfMonth;
	}
	
	private HashMap findNumberOfEventsPerMonth(int year, int month)
	{
		HashMap map = new HashMap<String, Integer>();
		return map;
	}
	
	private String getMonthAsString(int i)
	{
		return months[i];
	}

	@SuppressWarnings("unused")
	private String getWeekDayAsString(int i)
	{
		return weekdays[i];
	}

	private int getNumberOfDaysOfMonth(int i)
	{
		return daysOfMonth[i];
	}
	
	private void setCurrentDayOfMonth(int currentDayOfMonth)
	{
		this.currentDayOfMonth = currentDayOfMonth;
	}
	
	private void setCurrentMonth(int currentMonth){
		System.out.println("current month "+currentMonth+" "+month);
		this.currentMonth=currentMonth;
	}
	
	public void setCurrentWeekDay(int currentWeekDay)
	{
		this.currentWeekDay = currentWeekDay;
	}
	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		check=0;
		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
		}

		// Get a reference to the Day gridcell
		gridcell =  row.findViewById(R.id.calendar_day_gridcell);
        num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
       // if (_context instanceof LeaveCalenderActivity)
			gridcell.setOnClickListener(this);

		// ACCOUNT FOR SPACING

		//	Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
		String[] day_color = list.get(position).split("-");
		String theday = day_color[0];

		if(theday.equalsIgnoreCase("1")){
			theday = "01";
		}else if(theday.equalsIgnoreCase("2")){
			theday = "02";
		}else if (theday.equalsIgnoreCase("3")){
			theday = "03";
		}else if(theday.equalsIgnoreCase("4")){
			theday = "04";
		}else if (theday.equalsIgnoreCase("5")){
			theday = "05";
		}else if(theday.equalsIgnoreCase("6")){
			theday = "06";
		}else if (theday.equalsIgnoreCase("7")){
			theday = "07";
		}else if (theday.equalsIgnoreCase("8")){
			theday = "08";
		}else if (theday.equalsIgnoreCase("9")){
			theday = "09";
		}

		String themonth = day_color[2];
		String theyear = day_color[3];
		if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
		{
			if (eventsPerMonthMap.containsKey(theday))
			{
//				num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
				Integer numEvents = eventsPerMonthMap.get(theday);
//				num_events_per_day.setText(numEvents.toString());
			}
		}

		// Set the Day GridCell
		gridcell.setText(theday);
		// gridcell.setTag(theday + "-" + themonth + "-" + theyear);



		if(themonth.equalsIgnoreCase(months[0])){
			themonth = monthsNumeric[0];
		}
		else if(themonth.equalsIgnoreCase(months[1])){
			themonth = monthsNumeric[1];
		}
		else if(themonth.equalsIgnoreCase(months[2])){
			themonth = monthsNumeric[2];
		}
		else if(themonth.equalsIgnoreCase(months[3])){
			themonth = monthsNumeric[3];
		}
		else if(themonth.equalsIgnoreCase(months[4])){
			themonth = monthsNumeric[4];
		}

		else if(themonth.equalsIgnoreCase(months[5])){
			themonth = monthsNumeric[5];
		}
		else if(themonth.equalsIgnoreCase(months[6])){
			themonth = monthsNumeric[6];
		}
		else if(themonth.equalsIgnoreCase(months[7])){
			themonth = monthsNumeric[7];
		}
		else if(themonth.equalsIgnoreCase(months[8])){
			themonth = monthsNumeric[8];
		}
		else if(themonth.equalsIgnoreCase(months[9])){
			themonth = monthsNumeric[9];
		}
		else if(themonth.equalsIgnoreCase(months[10])){
			themonth = monthsNumeric[10];
		}

		else if(themonth.equalsIgnoreCase(months[11])){
			themonth = monthsNumeric[11];
		}
		else if(themonth.equalsIgnoreCase(months[12])){
			themonth = monthsNumeric[12];
		}

		String dateText  = theyear+"-"+ themonth + "-"+theday ;
		gridcell.setTag(position);

		if (day_color[1].equals("GREY"))
		{
			gridcell.setTextColor(Color.LTGRAY);
			//gridcell.setTextColor(ContextCompat.getColor(_context, R.color.new_login_button));
			gridcell.setEnabled(false);
			check=1;
		}
		if (day_color[1].equals("WHITE"))
		{
			gridcell.setTextColor(Color.BLACK);
			gridcell.setEnabled(true);
			num_events_per_day.setBackgroundColor(ContextCompat.getColor(_context, R.color.white));
		}
		if (day_color[1].equals("BLUE"))
		{
			System.out.println(themonth+ crnt_month+""+ theyear+crnt_yr+"");
			gridcell.setTextColor(Color.BLACK);
			if (Integer.parseInt(themonth)==crnt_month && Integer.parseInt(theyear)==crnt_yr ) {
				gridcell.setEnabled(true);
				gridcell.setBackgroundResource(R.drawable.current_date);
				gridcell.setTextColor(Color.WHITE);
			}else {
				gridcell.setTextColor(Color.BLACK);
			}
		}
		
		for(int i = 0 ; dt_list!=null && i < dt_list.size() ; i++){
			if(dateText.equalsIgnoreCase(dt_list.get(i).getSelecteddate()) && check==0){
				//gridcell.setTextColor(Color.BLACK);
			//	gridcell.setBackgroundResource(R.drawable.selected_date);
                num_events_per_day.setBackgroundColor(ContextCompat.getColor(_context, R.color.new_login_button));
				/*if (day_color[1].equals("BLUE")){
					gridcell.setTextColor(Color.WHITE);
					gridcell.setBackgroundResource(R.drawable.current_date);
				}*/

			}
		}
		return row;
	}

	@Override
	public void onClick(View v) {

	Log.e("clicked","clicked calendarbutton");

		int position=(Integer) v.getTag();
		String str= list.get(position);
		List<String> crnt_dt_List = new ArrayList<String>(Arrays.asList(list.get(position).split("-")));
		System.out.println("position "+position+" "+str);

		int mnth=0;
		for (int i=0; i< months.length; i++)
			if (crnt_dt_List.get(2).trim().equalsIgnoreCase(months[i])) {
				mnth = i+1;
				break;
			}


//		Dialog loader_dialog = CommonUtility.custom_loader((Activity) _context, "");
//		loader_dialog.show();
//		Object[] values={user.getUserToken(),mnth+"/"+crnt_dt_List.get(0)+"/"+crnt_dt_List.get(3)};
//		new GetJobListCalendarApiManager(((Activity)_context),  values, "", false,loader_dialog);
		String newdate = "";
		String dt = crnt_dt_List.get(3) +"-"+ mnth +"-"+ crnt_dt_List.get(0);
		Log.e("clicked_cal_date",dt);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date selecteddate = sdf.parse(dt);
			 newdate =  sdf.format(selecteddate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
       selfServiceHomeFragment.getCSdatedetailsList(newdate);

	}


}
