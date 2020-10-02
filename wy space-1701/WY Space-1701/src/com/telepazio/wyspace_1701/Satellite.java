package com.telepazio.wyspace_1701;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.List;

public class Satellite {

	static String findOverlap(int groundStationBandwidth){

		HashMap<Integer, ArrayList<Integer>> mapTimeBand = new HashMap<Integer, ArrayList<Integer>>();
		String st;
		LocalTime startTime;
		LocalTime endTime;
		int satelliteBandwidth;
		BufferedReader br;
		int max = 0, maxtime = 0;
		File file = new File("2458843pass-schedule.txt");
		if(file.length()==0)
		{
			return "Pass schedule file not found or is empty";
		}
		
		try {
				br = new BufferedReader(new FileReader(file));
				
				while ((st = br.readLine()) != null) 
				{
					List<Integer> listTimeBand = new ArrayList<Integer>();
					long difference = 0;
					String[] satelliteArray = st.split(",");
					if(satelliteArray.length!=4)
					{
						return "Invalid pass schedule file structure";
					}
			
					satelliteBandwidth = Integer.parseInt(satelliteArray[1]);
					if(satelliteBandwidth>groundStationBandwidth)
					{
						continue;
					}
					
					startTime = LocalTime.parse(satelliteArray[2]);
					endTime = LocalTime.parse(satelliteArray[3]);
					
					// Assuming when start time =00:00 and end time= 00:00 satellite is available
					// for downlink 24hrs
					if (endTime.compareTo(startTime) == 0) 
					{
						difference = 24 * 60;
					}	 
					else 
					{
						if (endTime.getHour() == 0)
						{
							difference = (24 * 60) - (startTime.getHour() * 60 + startTime.getMinute());
						} 
						else 
						{
							difference = Duration.between(startTime, endTime).toMinutes();
						}
					}
					
					int numOfIntervals = (int) difference / 30;
					int startHour = startTime.getHour();
					startHour = startHour * 2 + 1;
					int min = startTime.getMinute();
			
					for (int i = 0; i < numOfIntervals; i++) 
					{
				
						if (min > 0) 
						{
							min = 0;
							startHour = startHour + 1;
						}
				
						if (startHour == 48) 
						{
							if (mapTimeBand.get(0) != null) 
							{
								listTimeBand = mapTimeBand.get(0);
								if (listTimeBand.get(1) + satelliteBandwidth > groundStationBandwidth) 
								{
									continue;
								} 
								else 
								{
									mapTimeBand.put(0, new ArrayList<Integer>(Arrays.asList(listTimeBand.get(0) + 1, listTimeBand.get(1) + satelliteBandwidth)));
								}
							}
							else 
							{
								mapTimeBand.put(0, new ArrayList<Integer>(Arrays.asList(1, satelliteBandwidth)));
							}

						} 
						else 
						{
							if (mapTimeBand.get(startHour) != null) 
							{
								listTimeBand = mapTimeBand.get(startHour);
								if (listTimeBand.get(1) + satelliteBandwidth > groundStationBandwidth)
								{
									continue;
								} 
								else 
								{
									mapTimeBand.put(startHour, new ArrayList<Integer>(Arrays.asList(listTimeBand.get(0) + 1, listTimeBand.get(1) + satelliteBandwidth)));
								}
						
							} 
							else 
							{
								mapTimeBand.put(startHour, new ArrayList<Integer>(Arrays.asList(1, satelliteBandwidth)));
							}

						}
						startHour = startHour + 1;
					}
				}
		
	
		for (Entry<Integer, ArrayList<Integer>> entry : mapTimeBand.entrySet()) 
		{
			if (max < entry.getValue().get(0))
			{
				max = entry.getValue().get(0);
				maxtime = entry.getKey();
			}
			
		}
		 
		if(mapTimeBand.isEmpty())
		{
			return "Insufficient Bandwidth: No satellite can down link data";
		}

		if (maxtime == 0) 
		{
			return String.valueOf(
					"Start Time: " + (48 - 1) / 2 + ":" + "30" + ", End Time: " + "0" + (maxtime) / 2 + ":" + "00");
		}

		if (((float) (maxtime - 1) / 2) % 1 == 0) 
		{
			if ((maxtime - 1) / 2 < 10)
				return String.valueOf("Start Time: " + "0" + (maxtime - 1) / 2 + ":" + "00" + ", End Time: " + "0"
						+ (maxtime) / 2 + ":" + "30");
			else
				return String.valueOf(
						"Start Time: " + (maxtime - 1) / 2 + ":" + "00" + ", End Time: " + (maxtime) / 2 + ":" + "30");
		} 
		else 
		{
			if ((maxtime - 1) / 2 < 10)
				return String.valueOf(
						"Start Time: " + "0" + (maxtime - 1) / 2 + ":" + "30" + ", End Time: " + (maxtime) / 2 + ":00");
			else
				return String.valueOf(
						"Start Time: " + (maxtime - 1) / 2 + ":" + "30" + ", End Time: " + (maxtime) / 2 + ":00");
		}
		
		} catch (FileNotFoundException e) {
			return "Pass schedule file not found";
		}
		catch(IOException iex)
		{
			return "Can not read pass schedule file";
		}
		catch(DateTimeParseException ex )
		{
			return "Invalid time structure in pass schedule file";
		}
		catch(NumberFormatException nex)
		{
			return "Invalid bandwidth in pass schedule file";
		}
	}

	public static void main(String[] args) {
		System.out.println(findOverlap(100));
	}
}
