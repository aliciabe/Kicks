package com.kicks;

interface ChronometerServiceInterface{
	int getNumberOfKicks();
	long getStartTime();
	void setKicks(int kicks);
	void setStartTime(long startTime);
	void incrementKicks();
	void setIsFirst(boolean isF);	
	boolean getIsFirst();
	void enableNotification();
} 