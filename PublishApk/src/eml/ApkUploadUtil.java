package eml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import eml.FileClient.DownloadFileCallback;
import okhttp3.Call;
import okhttp3.Response;

public class ApkUploadUtil {

	public static void main(String[] args) {

		if (args.length < 8) {
			throw new IllegalArgumentException("argument is not correct.");
		}

		for (String arg : args) {
			System.out.println("arg:" + arg);
		}

		final String apiKey = args[0];
		final String uKey = args[1];
		final String emailSender = args[2];
		final String emailPwd = args[3];
		final String emailReceivers = args[4];
		final String smtp = args[5];
		final String apk = args[6];
		final String isSendEmail = args[7];

		final String[] receivers = emailReceivers != null ? emailReceivers.split(",") : null;

		String url = "http://www.pgyer.com/apiv1/app/upload";

		Map<String, String> params = new HashMap<>();
		params.put("_api_key", apiKey);
		params.put("uKey", uKey);

		Map<String, File> paramsFiles = new HashMap<>();
		paramsFiles.put("file", new File(apk));

		System.out.println("uploading================================================================");
		FileClient.uploadFile(url, params, paramsFiles, new FileClient.FileUploadCallback() {

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				exit();
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				if (response == null || !response.isSuccessful()) {
					System.err.println("apk upload fail***********************************************");
					exit();
					return;
				}
				String json = response.body().string();
				PgyerUploadReponse pygResp = new Gson().fromJson(json, PgyerUploadReponse.class);

				if (pygResp.code == 0) {
					final PgyerUploadReponse.Data data = pygResp.data;

					if (!"true".equals(isSendEmail)) {
						System.err.println(
								"******************************no need to send email,all-finished-success*********************");
						exit();
						return;
					}

					System.out.println("downloading==============================================================");
					final String qrjpg = "./qr.jpg";
					FileClient.downloadFile(data.appQRCodeURL, qrjpg, new DownloadFileCallback() {

						@Override
						public void onProgress(int totalBytes, int finishedBytes) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFail(Exception e) {
							e.printStackTrace();
							exit();
						}

						@Override
						public void onSuccess() {

							System.out.println(
									"sendEmailing==============================================================");

							EmailManager email = new EmailManager(smtp, emailSender, emailPwd);
							email.sendMail(emailSender, receivers, null, data.appName + "【上传成功！】",
									buildEmailContent(data), qrjpg);

							System.err.println(
									"*****************************email sended,all-finished-success*****************************");
							exit();
						}

					});

				} else {
					System.err.println(pygResp.message + "***********************************************************");
					exit();
				}

			}

			@Override
			public void onRequestProgress(long bytesWritten, long contentLength) {
				// TODO Auto-generated method stub

			}

		});

	}

	private static void exit() {
		System.exit(0);
	}

	private static String buildEmailContent(PgyerUploadReponse.Data data) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");

		sb.append("appName:").append(data.appName).append("</br>");
		sb.append("appVersion:").append(data.appVersion).append("</br>");
		sb.append("appVersionNo:").append(data.appVersionNo).append("</br>");

		sb.append("</body></html>");
		return sb.toString();
	}
}
