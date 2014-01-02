package com.android.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.collections4.CollectionUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.common.Constants;
import com.android.common.SystemHelper;
import com.android.dialog.MyUpdateDialog;
import com.android.mapping.VersionMapping;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

/**
 * 更新组件
 * 
 * @author superleo
 * 
 */
// 定义成一个可以注入的组件
@EBean(scope = Scope.Singleton)
public class AppUpdateComponent {

	private MyUpdateDialog myUpdateDialog = null;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private boolean interceptFlag = false;
	private String apkUrl = Constants.URL_UPDATE_APP_DOWN;
	private String savePath = Constants.CACHE_DIR;
	private String saveFileName = savePath + "/";
	private int progress;

	// 注入 Context 变量
	@RootContext
	Context context;

	@Bean
	WifiComponent wifiComponent;

	@Bean
	ToastComponent toastComponent;

	@AfterInject
	public void initAppUpdate() {
		myUpdateDialog = new MyUpdateDialog(context);
	}

	public void updateApp() {
		String url = Constants.URL_UPDATE_APP;
		if (wifiComponent.isConnected()) {
			VersionMapping data = VersionMapping.getJSON(url);
			if (data.code == Constants.STATUS_SUCCESS && CollectionUtils.isNotEmpty(data.datas)) {
				final VersionMapping.Version remoteVersion = data.datas.get(0);
				int appVersion = SystemHelper.getAppVersionCode(context);
				if (appVersion < remoteVersion.versionNo) {
					myUpdateDialog.dialog_message.setText("有新版本（V" + remoteVersion.description + "）升级");
					myUpdateDialog.show();
					myUpdateDialog.dialog_yes.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
							if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
								saveFileName += remoteVersion.name;
								apkUrl += remoteVersion.name;
								Thread downLoadThread = new Thread(mdownApkRunnable);
								downLoadThread.start();
							} else {
								myUpdateDialog.dismiss();
								toastComponent.show("亲，SD卡不在了，快去找找！！");
							}
						}
					});
					myUpdateDialog.dialog_no.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							interceptFlag = true;
							myUpdateDialog.dismiss();
						}
					});
				} else {
					toastComponent.show("当前是最新版本.");
				}
			}
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				myUpdateDialog.shuzhi.setText("下载:" + (progress + 1) + "%");
				myUpdateDialog.progress.setProgress(progress);
				break;
			case DOWN_OVER:
				Toast.makeText(context, "下载完成现在安装！！", Toast.LENGTH_SHORT).show();
				myUpdateDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);

	}

}
