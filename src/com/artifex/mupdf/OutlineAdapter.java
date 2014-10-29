/*
  	MuPDF is Copyright 2006-2013 Artifex Software, Inc.
 	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.artifex.mupdf;

import com.fullreader.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OutlineAdapter extends BaseAdapter {
	private final OutlineItem    mItems[];
	private final LayoutInflater mInflater;
	public OutlineAdapter(LayoutInflater inflater, OutlineItem items[]) {
		mInflater = inflater;
		mItems    = items;
	}

	public int getCount() {
		return mItems.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(R.layout.outline_entry, null);
		} else {
			v = convertView;
		}
		int level = mItems[position].level;
		if (level > 8) level = 8;
		String space = "";
		for (int i=0; i<level;i++)
			space += "   ";
		((TextView)v.findViewById(R.id.title)).setText(space+mItems[position].title);
		((TextView)v.findViewById(R.id.page)).setText(String.valueOf(mItems[position].page+1));
		return v;
	}

}
