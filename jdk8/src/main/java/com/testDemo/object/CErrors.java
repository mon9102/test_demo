

/**
 * Copyright (c) 2002 sinosoft  Co. Ltd.
 * All right reserved.
 */
package com.testDemo.object;

import java.util.Locale;
import java.util.Vector;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CErrors implements Cloneable, java.io.Serializable{
	private Vector vErrors = new Vector();
	private int errorCount = 0;

	private String flag = "";
	private String content = "";
	private String result = "";

	/**
	 * 克隆CErrors对象 2005-04-15 朱向峰添加
	 * 
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		CErrors cloned = (CErrors) super.clone();
		// clone the mutable fields of this class
		return cloned;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String f) {
		flag = f;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String c) {
		content = c;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String c) {
		result = c;
	}

	public CErrors() {
	}

	/**
	 * 向错误容器类中增加一个错误，错误个数加1
	 * 
	 * @param cError
	 *            CError
	 */
	public void addOneError(CError cError) {
		this.vErrors.add(cError);
		this.errorCount++;
	}

	/**
	 * 向错误容器类中增加一个错误，错误个数加1
	 * 
	 * @param cErrorString
	 *            String
	 */
	public void addOneError(String cErrorString) {
		CError tError = new CError(cErrorString.trim());
		this.vErrors.add(tError);
		this.errorCount++;
	}

	/**
	 * 移出最后的错误
	 */
	public void removeLastError() {
		if (errorCount > 0) {
			this.vErrors.removeElementAt(errorCount - 1);
			this.errorCount--;
		}
	}

	/**
	 * 移出指定的错误信息
	 * 
	 * @param pos
	 *            int
	 */
	public void removeError(int pos) {
		if ((errorCount > 0) && (pos < errorCount)) {
			this.vErrors.removeElementAt(pos);
			this.errorCount--;
		}
	}

	/**
	 * 将错误容器内的错误信息清空，计数重置为0
	 */
	public void clearErrors() {
		this.vErrors.clear();
		this.errorCount = 0;
	}

	/**
	 * 得到容器中的错误的个数
	 * 
	 * @return int
	 */
	public int getErrorCount() {
		return this.errorCount;
	}
    
    private String replaceMsg(String msg){
    	/*
         * kevin 2002-10-15
         * 保证错误信息的最后一个字符不是回车。否则，这样的信息会造成javascript的错误。
         * 替换一些特殊的字符。否则，会造成javascript的错误。
         */
        String strMsg = msg;
        strMsg = strMsg.replace((char) (10), ' ');
        strMsg = strMsg.replace('"', ' ');
        strMsg = strMsg.replace('\'', ' ');
        return strMsg;
    }

	/**
	 * 得到容器中指定位置的错误对象
	 * 
	 * @param indexError
	 *            int
	 * @return CError
	 */
	public CError getError(int indexError) {
		CError tError;
		tError = (CError) vErrors.get(indexError);
		return tError;
	}
    
    public CError getError(int indexError, ResourceBundle rb){
        CError tError = (CError) vErrors.get(indexError);
        tError.errorMessage = replaceMsg(i18nStr(tError.errorMessage, rb));
        return tError;
    }
	public static String i18nStr(String str, ResourceBundle locale){
		return str;
	}

	/**
	 * 得到最早一个错误的错误信息,如果没有错误则返回空字符串""
	 * 
	 * @return String
	 */
	public String getFirstError() {
		try {
			CError tError = (CError) this.vErrors.get(0);
			return replaceMsg(tError.errorMessage);
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getFirstError(ResourceBundle rb){
		try {
			return getError(0, rb).errorMessage;
		} catch (Exception e) {
			return "";
		}
    }


	/**
	 * 得到最后一个错误的错误信息,如果没有错误则返回空字符串""
	 * 
	 * @return String
	 */
	public String getLastError() {
		if (errorCount < 1) {
			return "";
		}
		try {
			CError tError = (CError) this.vErrors.get(errorCount - 1);
			return replaceMsg(tError.errorMessage);
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getLastError(ResourceBundle rb){
		try {
			return getError(errorCount - 1, rb).errorMessage;
		} catch (Exception e) {
			return "";
		}
    }

	/**
	 * 判断产生的错误是否严重到需要处理
	 * 
	 * @return boolean
	 */
	public boolean needDealError() {
		try {
			if (this.getErrorCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	/**
	 * 复制所有的错误信息到本类中
	 * 
	 * @param cSourceErrors
	 *            CErrors
	 */
	public void copyAllErrors(CErrors cSourceErrors) {
		int i = 0;
		int iMax = 0;
		iMax = cSourceErrors.getErrorCount();

		if (this.equals(cSourceErrors)) {
			System.out.println("为同一个错误对象,不进行复制");
			return;
		}

		for (i = 0; i < iMax; i++) {
			this.addOneError(cSourceErrors.getError(i));
		}
	}

	/**
	 * 获取错误严重级别
	 * 
	 * @return String
	 */
	public String getErrType() {
		int forbitNum = 0;
		int needSelectNum = 0;
		int allowNum = 0;
		int unknowNum = 0;

		for (int i = 0; i < vErrors.size(); i++) {
			CError tError = (CError) this.vErrors.get(i);

			if (tError.errorType.equals(CError.TYPE_FORBID)) {
				forbitNum++;
			} else if (tError.errorType.equals(CError.TYPE_NEEDSELECT)) {
				needSelectNum++;
			} else if (tError.errorType.equals(CError.TYPE_ALLOW)) {
				allowNum++;
			} else {
				unknowNum++;
			}
		}

		if (forbitNum > 0) {
			return CError.TYPE_FORBID;
		} else if (needSelectNum > 0) {
			return CError.TYPE_NEEDSELECT;
		} else if (allowNum > 0) {
			return CError.TYPE_ALLOW;
		} else if (unknowNum > 0) {
			return CError.TYPE_UNKNOW;
		} else {
			return CError.TYPE_NONEERR;
		}
	}

	/**
	 * 获取错误内容,并分类,以提供界面显示
	 * 
	 * @return String
	 */
	public String getErrContent() {
		content = "（一共发生 " + vErrors.size() + " 个错误）\n";
		String forbitContent = "";
		String needSelectContent = "";
		String allowContent = "";
		String unknowContent = "";

		for (int i = 0; i < vErrors.size(); i++) {
			CError tError = (CError) this.vErrors.get(i);

			if (tError.errorType.equals(CError.TYPE_FORBID)) {
				forbitContent = forbitContent + "  " + "（错误编码："
						+ tError.errorNo + "）" + tError.errorMessage + "\n";
			} else if (tError.errorType.equals(CError.TYPE_NEEDSELECT)) {
				needSelectContent = needSelectContent + "  " + "（错误编码："
						+ tError.errorNo + "）" + tError.errorMessage + "\n";
			} else if (tError.errorType.equals(CError.TYPE_ALLOW)) {
				allowContent = allowContent + "  " + "（错误编码：" + tError.errorNo
						+ "）" + tError.errorMessage + "\n";
			} else {
				unknowContent = unknowContent + "  " + "（错误编码：意外错误）"
						+ tError.errorMessage + "\n";
			}
		}

		if (!forbitContent.equals("")) {
			content = content + "严重错误如下:\n" + forbitContent;
		}

		if (!needSelectContent.equals("")) {
			content = content + "需要选择的错误如下:\n" + needSelectContent;
		}

		if (!allowContent.equals("")) {
			content = content + "允许出现的错误如下:\n" + allowContent;
		}

		if (!unknowContent.equals("")) {
			content = content + "意外错误如下:\n" + unknowContent;
		}

		// content = content + forbitContent + needSelectContent + allowContent;
		// content = changForJavaScript(content);
		// content = changForHTML(content);
		return content;
	}

	/**
	 * 校验错误
	 * 
	 * @param cerrors
	 *            CErrors
	 */
	public void checkErrors(CErrors cerrors) {
		if (cerrors.getErrType().equals(CError.TYPE_NONE)) {
			content = "操作成功";
			flag = "Success";
		} else {
			String ErrorContent = cerrors.getErrContent();

			if (cerrors.getErrType().equals(CError.TYPE_ALLOW)) {
				content = "保存成功，但是：" + changForHTML(ErrorContent);
				flag = "Success";
			} else {
				content = "保存失败，原因是：" + changForHTML(ErrorContent);
				flag = "Fail";
			}
		}
	}

	/**
	 * 校验错误
	 * 
	 * @param v
	 *            VData
	 */
	public void checkWSErrors(VData v) {
		String type = (String) v.get(0);

		if (type.equals(CError.TYPE_NONE)) {
			content = "操作成功";
			flag = "Success";

			if (v.size() > 1) {
				result = (String) v.get(1);
			}
		} else {
			if (type.equals(CError.TYPE_ALLOW)) {
				content = "保存成功，但是：" + changForHTML((String) v.get(1));
				flag = "Success";
			} else {
				content = "保存失败，原因是：" + changForHTML((String) v.get(1));
				flag = "Fail";
			}
		}
	}
	/**
	 * 转换JavaScript解析不了的特殊字符
	 *
	 * @param s String
	 * @return String
	 */
	public static String changForHTML(String s) {
		char[] arr = s.toCharArray();
		s = "";

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == '"' || arr[i] == '\'') {
				s = s + "\\";
			}

			if (arr[i] == '\n') {
				s = s + "<br>";
				continue;
			}
			if (arr[i] == '\r'){
				continue;
			}


			s = s + arr[i];
		}

		return s;
	}
	/**
	 * 调试函数
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		ResourceBundle rb = ResourceBundle.getBundle("i18n.LocalStrings", Locale.SIMPLIFIED_CHINESE);
    	CErrors cs = new CErrors();
    	cs.addOneError("#ldcode_sex_1##ldcode_sex_0#");
    	cs.addOneError("");
    	cs.addOneError("");
    	System.out.println(cs.getFirstError(rb));
	}
}