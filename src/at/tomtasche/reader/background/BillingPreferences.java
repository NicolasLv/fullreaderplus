/*
	OpenOffice Document Reader is Android's first native ODF Viewer!
    Copyright (C) 2010  Thomas Taschauer - tomtasche@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package at.tomtasche.reader.background;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BillingPreferences {

	private SharedPreferences sharedPreferences;

	public BillingPreferences(Context context) {
		sharedPreferences = context.getSharedPreferences(
				"modifyMeIfYouWantToRemoveAdsIllegally", Context.MODE_PRIVATE);
	}

	public long getLastQueryTime() {
		return sharedPreferences.getLong("time", 0);
	}

	public void setLastQueryTime(long time) {
		Editor editor = sharedPreferences.edit();
		editor.putLong("time", time);
		editor.commit();
	}

	public boolean hasPurchased() {
		return sharedPreferences.getBoolean("purchased", false);
	}

	public void setPurchased(boolean purchased) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("purchased", purchased);
		editor.commit();
	}
}