

/**
 * Copyright (c) 2002 sinosoft  Co. Ltd.
 * All right reserved.
 */
package com.testDemo.object;

import java.lang.reflect.Field;

public class CError implements java.io.Serializable{
	public static final String TYPE_NONEERR = "0"; // 没有错误
	public static final String TYPE_FORBID = "1"; // 禁止
	public static final String TYPE_ALLOW = "2"; // 允许
	public static final String TYPE_NEEDSELECT = "3"; // 需要判断
	public static final String TYPE_UNKNOW = "4"; // 兼容传统
	public static final String SYSTEM = "10"; // 操作系统
	public static final String COMMUNICATION = "11"; // 通讯
	public static final String SAFETY = "12"; // 安全
	public static final String BL_RISK = "2101"; // 应用逻辑-险种相关的错误
	public static final String BL_FINANCE = "2102"; // 应用逻辑-财务相关错误
	public static final String BL_TB = "2103"; // 应用逻辑-投保相关的错误
	public static final String BL_BQ = "2104"; // 应用逻辑-保全相关的错误
	public static final String BL_CASE = "2105"; // 应用逻辑-案件相关的错误
	public static final String BL_LIVEGET = "2106"; // 应用逻辑-生存领取相关的错误
	public static final String BL_AGENT = "2107"; // 应用逻辑-业务员相关的错误
	public static final String BL_BANK = "2108"; // 应用逻辑-银行相关的错误
	public static final String BL_UNKNOW = "2109"; // 应用逻辑-其它错误
	public static final String DB_OPERATE = "22"; // 数据库操作
	public static final String UNKNOW = "23"; // 意外

	public static final String TYPE_NONE = "0"; // 无错误
	public static final String WS_SERVER_CALL = "02010001"; // 电子商务服务器端调用错误
	public static final String WS_TRANS_SOAP = "02010002"; // 电子商务soap传输错误

	/** 错误严重级别类型 */
	public String errorType = TYPE_FORBID;

	/** 错误编码 */
	public String errorNo = UNKNOW;

	/** 模块名称 */
	public String moduleName;

	/** 函数名称 */
	public String functionName;

	/** 错误内容 */
	public String errorMessage;

	public CError() {
	}

	/**
	 * 获得错误信息
	 * 
	 * @param errString
	 *            String
	 */
	public CError(String errString) {
		errorMessage = errString;
	}

	/**
	 * 设置错误信息集合
	 * 
	 * @param errString
	 *            String
	 * @param cModuleName
	 *            String
	 * @param cFunctionName
	 *            String
	 */
	public CError(String errString, String cModuleName, String cFunctionName) {
		errorMessage = errString;
		moduleName = cModuleName;
		functionName = cFunctionName;
	}

	/**
	 * 建立错误信息对象，并将该错误放置到传入的对象的错误队列对象中
	 * 
	 * @param o
	 *            希望容纳该错误的对象
	 * @param errMessage
	 *            错误信息
	 */
	public static void buildErr(Object o, String errMessage) {
		buildErr(o, "", errMessage, TYPE_FORBID, UNKNOW);
	}

	/**
	 * 建立错误信息对象，并将该错误放置到传入的对象的错误队列对象中
	 * 
	 * @param o
	 *            Object
	 * @param errMessage
	 *            String
	 * @param errType
	 *            String
	 * @param errNo
	 *            String
	 */
	public static void buildErr(Object o, String errMessage, String errType,
			String errNo) {
		buildErr(o, "", errMessage.trim(), errType, errNo);
	}

	/**
	 * 建立错误信息对象，并将该错误放置到传入的对象的错误队列对象中
	 * 
	 * @param o
	 *            希望容纳该错误的对象
	 * @param functionName
	 *            出错函数名称
	 * @param errMessage
	 *            错误信息
	 * @param errType
	 *            错误严重级别类型
	 * @param errNo
	 *            错误编码
	 */
	public static void buildErr(Object o, String functionName,
			String errMessage, String errType, String errNo) {
		try {
			CError tError = new CError();
			tError.moduleName = o.getClass().getName();
			tError.functionName = functionName;
			tError.errorMessage = String.valueOf(errMessage).trim();
			tError.errorType = errType;
			tError.errorNo = errNo;

			Class c = o.getClass();
			Field f = c.getField("mErrors");
			((CErrors) f.get(o)).addOneError(tError);

			System.out.println("\n在（" + tError.moduleName + "）中抛出如下错误："
					+ errMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 建立错误信息对象，并将该错误放置到传入的对象的错误队列对象中，同时转储其它出入的对象列表
	 * 
	 * @param o
	 *            Object
	 * @param errMessage
	 *            String
	 * @param e
	 *            CErrors
	 */
	public static void buildErr(Object o, String errMessage, CErrors e) {
		buildErr(o, "", errMessage.trim(), e, TYPE_FORBID, UNKNOW);
	}

	/**
	 * 建立错误信息对象，并将该错误放置到传入的对象的错误队列对象中，同时转储其它出入的对象列表
	 * 
	 * @param o
	 *            Object
	 * @param errMessage
	 *            String
	 * @param e
	 *            CErrors
	 * @param errType
	 *            String
	 * @param errNo
	 *            String
	 */
	public static void buildErr(Object o, String errMessage, CErrors e,
			String errType, String errNo) {
		buildErr(o, "", errMessage, e, errType, errNo);
	}

	/**
	 * 建立错误信息对象，并将该错误放置到传入的对象的错误队列对象中，同时转储其它出入的对象列表
	 * 
	 * @param o
	 *            Object
	 * @param functionName
	 *            String
	 * @param errMessage
	 *            String
	 * @param e
	 *            CErrors
	 * @param errType
	 *            String
	 * @param errNo
	 *            String
	 */
	public static void buildErr(Object o, String functionName,
			String errMessage, CErrors e, String errType, String errNo) {
		try {
			Class c = o.getClass();
			Field f = c.getField("mErrors");
			((CErrors) f.get(o)).copyAllErrors(e);

			buildErr(o, functionName, errMessage, errType, errNo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 调试函数
	 * 
	 * @param arg
	 *            String[]
	 */
	public static void main(String[] arg) {
		CError e = new CError();
	}
}
