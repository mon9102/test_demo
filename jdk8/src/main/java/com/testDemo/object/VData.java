

/**
 * Copyright (c) 2002 sinosoft  Co. Ltd.
 * All right reserved.
 */
package com.testDemo.object;

import java.util.Date;
import java.util.Vector;

public class VData extends Vector<Object> {
	// 保存传递中需要的数据

	private static final long serialVersionUID = 1L;

	public VData() {
	}

	// 对数据进行打包成字符串
	public String encode() {
		String strReturn = "";
		return strReturn;
	}

	// 根据类的名称，从VData中找出从cStartPos为开始位置的第一个名称和cObjectName相同的对象
	// 输入：cObjectName: 需要查找的第一个对象名
	// cStartPos: 查找的开始位置（包含这个位置）
	// 输出：如果找到，返回找到的对象，如果没有找到，返回null
	public Object getObjectByObjectName(String cObjectName, int cStartPos) {
		int i = 0;
		int iMax = 0;
		String tStr1 = "";
		String tStr2 = "";
		Object tReturn = null;
		if (cStartPos < 0) {
			cStartPos = 0;
		}
		iMax = this.size();
		try {
			for (i = cStartPos; i < iMax; i++) {
				if (this.get(i) == null) {
					continue;
				}
				tStr1 = this.get(i).getClass().getName().toUpperCase();
				tStr2 = cObjectName.toUpperCase();
				if (tStr1.equals(tStr2)
						|| this.getLastWord(tStr1, ".").equals(tStr2)) {
					tReturn = this.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			tReturn = null;
		}
		return tReturn;
	}

	// 根据类的名称，从VData中找出从cStartPos为开始位置的第一个名称和cObjectName相同的对象
	// 输入：cObjectName: 需要查找的第一个对象名
	// cStartPos: 查找的开始位置（包含这个位置）
	// cPos: 查找第几个该对象
	// 输出：如果找到，返回找到的对象，如果没有找到，返回null
	public Object getObjectByObjectName(String cObjectName, int cStartPos,
			int cPos) {
		int i = 0;
		int j = 0;
		int iMax = 0;
		String tStr1 = "";
		String tStr2 = "";
		Object tReturn = null;
		if (cStartPos < 0) {
			cStartPos = 0;
		}
		iMax = this.size();
		try {
			for (i = cStartPos; i < iMax; i++) {
				if (this.get(i) == null) {
					continue;
				}
				tStr1 = this.get(i).getClass().getName().toUpperCase();
				tStr2 = cObjectName.toUpperCase();
				if (tStr1.equals(tStr2)
						|| this.getLastWord(tStr1, ".").equals(tStr2)) {
					j++;
					if (j < cPos) {
						continue;
					}
					tReturn = this.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			tReturn = null;
		}
		return tReturn;
	}

	// 得到字符串中的最后一个以splitChar分割的单词
	public String getLastWord(String cStr, String splitStr) {
		String tReturn;
		int tIndex = -1;
		int tIndexOld = -1;
		tReturn = cStr;
		try {
			while (true) {
				tIndex = tReturn.indexOf(splitStr, tIndex + 1);
				if (tIndex > 0) {
					tIndexOld = tIndex;
				} else {
					break;
				}
			}
			if (tIndexOld > 0) {
				tReturn = cStr.substring(tIndexOld + 1, cStr.length());
			} else {
				tReturn = cStr;
			}
		} catch (Exception ex) {
			tReturn = "";
		}
		return tReturn;
	}

	// 根据类的索引，从VData中找出第cIndex位置的对象
	// 输入：cIndex: 需要得到的对象位置
	// 输出：如果找到，返回找到的对象，如果没有找到，返回null
	public Object getObject(int cIndex) {
		Object tReturn = null;
		if (cIndex < 0) {
			cIndex = 0;
		}
		try {
			tReturn = this.get(cIndex);
		} catch (Exception ex) {
			tReturn = null;
		}
		return tReturn;
	}

	public static void main(String[] args) {
		VData mVData = new VData();
		mVData.add("aaaa");
		mVData.add("bbbb");
		mVData.add(new Date());
		Date a = (Date) mVData.getObjectByObjectName("Date",1);
		System.out.println(a);
	}
}
