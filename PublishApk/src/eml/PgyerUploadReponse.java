package eml;

import java.io.Serializable;

public class PgyerUploadReponse implements Serializable{
	
	public Data data;
	public int code;
	public String message;

	public static class Data implements Serializable{
		public String appName;
		public String appVersion;
		public String appVersionNo;
		public String appQRCodeURL;
	}
}
