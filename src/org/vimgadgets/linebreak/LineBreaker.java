/*
Copyright (C) 2008-2012 Wu Yongwei <wuyongwei at gmail dot com>
Copyright (C) 2012 Tom Hacohen <tom dot hacohen at samsung dot com>

This software is provided 'as-is', without any express or implied
warranty.  In no event will the author be held liable for any damages
arising from the use of this software.

Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software.  If you use this software
   in a product, an acknowledgement in the product documentation would
   be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not
   be misrepresented as being the original software.
3. This notice may not be removed or altered from any source
   distribution.
*/

package org.vimgadgets.linebreak;

public final class LineBreaker {
	static {
		System.loadLibrary("LineBreak-v2");
		init();
	}

	public static final char MUSTBREAK = 0;
	public static final char ALLOWBREAK = 1;
	public static final char NOBREAK = 2;
	public static final char INSIDEACHAR = 3;

	private static native void init();
	private static native void setLineBreaksForCharArray(char[] data, int offset, int length, String lang, byte[] breaks);
	private static native void setLineBreaksForString(String data, String lang, byte[] breaks);

	private final String myLanguage;

	public LineBreaker(String lang) {
		myLanguage = lang;
	}

	public void setLineBreaks(char[] data, int offset, int length, byte[] breaks) {
		setLineBreaksForCharArray(data, offset, length, myLanguage, breaks);
	}

	public void setLineBreaks(String data, byte[] breaks) {
		setLineBreaksForString(data, myLanguage, breaks);
	}
}