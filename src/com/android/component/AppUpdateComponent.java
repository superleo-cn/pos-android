package com.android.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.R;
import com.android.bean.ResponseData;
import com.android.bean.VersionBean;
import com.android.common.Constants;
import com.android.common.RemoteDataHandler;
import com.android.common.SystemHelper;
import com.android.common.RemoteDataHandler.Callback;
import com.googlecode.androidannotations.annotations.AfterInject;
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

	@AfterInject
	public void initAppUpdate() {
		myUpdateDialog = new MyUpdateDialog(context);
	}

	public void updateApp() {
		String url = Constants.URL_UPDATE_APP;
		RemoteDataHandler.asyncGet(url, new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				if (data.getCode() == 1) {
					String json = data.getJson();
					ArrayList<VersionBean> datas = VersionBean.newInstanceList(json);
					if (datas != null) {
						final VersionBean vbean = datas.get(0);
						final String sysAppVersion = String.valueOf(SystemHelper.getAppVersionCode(context));
						if (!StringUtils.equalsIgnoreCase(vbean.getVersionNo(), sysAppVersion)) {
							myUpdateDialog.dialog_message.setText("有新版本（V" + vbean.getVersionNo() + "）升级");
							myUpdateDialog.show();
							myUpdateDialog.dialog_yes.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
									if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
										saveFileName += vbean.getName();
										apkUrl += vbean.getName();
										Thread downLoadThread = new Thread(mdownApkRunnable);
										downLoadThread.start();
									} else {
										myUpdateDialog.dismiss();
										Toast.makeText(context, "亲，SD卡不在了，快去找找！！", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(context, "当前是最新版本", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
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

	/**
	 * 生成下载的提示框
	 * 
	 * @author superleo
	 * 
	 */
	public static class MyUpdateDialog extends Dialog {
		public Button dialog_yes;
		public Button dialog_no;
		public TextView dialog_message;
		public ProgressBar progress;
		public TextView shuzhi;

		public MyUpdateDialog(Context context) {
			super(context, R.style.MyProgressDialog);
			this.setContentView(R.layout.update_dialog);
			dialog_message = (TextView) findViewById(R.id.dialog_message);
			dialog_yes = (Button) findViewById(R.id.dialog_yes);
			dialog_no = (Button) findViewById(R.id.dialog_no);
			progress = (ProgressBar) findViewById(R.id.progress);
			shuzhi = (TextView) findViewById(R.id.shuzhi);
		}
	}

}
