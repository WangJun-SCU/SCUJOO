package com.example.scujoo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.scujoo.utils.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalMessage extends Activity {

	private ImageButton back;
	private TextView name;
	private TextView college;
	private TextView major;
	private CircleImageView head;
	private Bitmap headBitmap;
	private static String path = "/sdcard/myHead/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_message);

		init();
		initMessage();

		back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
		head.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				new AlertDialog.Builder(PersonalMessage.this) // build AlertDialog  
                .setTitle("����ͷ��") // title  
                .setItems(R.array.items, new DialogInterface.OnClickListener() { //content  
                    public void onClick(DialogInterface dialog, int which) {  
                        final String[] aryShop = getResources().getStringArray(R.array.items); //array  
                        if("����".equals(aryShop[which]))
                        {
                        	Toast.makeText(getApplicationContext(), aryShop[which], 1).show();
                        	Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                			intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                					Environment.getExternalStorageDirectory(), "head.jpg")));
                			startActivityForResult(intent2, 2);// ����ForResult��
                        }else if("�����ѡ��".equals(aryShop[which]))
                        {
                        	Toast.makeText(getApplicationContext(), aryShop[which], 1).show();
                        	Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                			intent1.setDataAndType(
                					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                			startActivityForResult(intent1, 1);
                        }
                    }  
                })  
                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
                      
                    public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss(); //�ر�alertDialog  
                    }  
                })  
                .show(); 
			}
		});
	}

	private void init() {
		back = (ImageButton) findViewById(R.id.personal_message_back);
		name = (TextView) findViewById(R.id.personal_message_name);
		college = (TextView) findViewById(R.id.personal_message_college);
		major = (TextView) findViewById(R.id.personal_message_major);
		head = (CircleImageView) findViewById(R.id.personal_message_head);

		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// ��Sd����ͷ��ת����Bitmap
		if (bt != null) {
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);// ת����drawable
			head.setImageDrawable(drawable);
		} else {
			/**
			 * ���SD����û������Ҫ�ӷ�����ȡͷ��ȡ������ͷ���ٱ�����SD��
			 * 
			 */
		}
	}

	// ���ø�����Ϣ
	private void initMessage() {
		SharedPreferences sp = getSharedPreferences("datas",
				Activity.MODE_PRIVATE);
		System.out.println("Datas-name" + sp.getString("name", ""));
		name.setText(sp.getString("name", ""));
		college.setText(sp.getString("college", ""));
		major.setText(sp.getString("major", ""));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// �ü�ͼƬ
			}

			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/head.jpg");
				cropPhoto(Uri.fromFile(temp));// �ü�ͼƬ
			}

			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				headBitmap = extras.getParcelable("data");
				if (headBitmap != null) {
					/**
					 * �ϴ�����������
					 */
					setPicToView(headBitmap);// ������SD����
					head.setImageBitmap(headBitmap);// ��ImageView��ʾ����
				}
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	
	
	/**
	 * ����ϵͳ�Ĳü�
	 * 
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	// ����Ƭ������sd����
	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// �����ļ���
		String fileName = path + "head.jpg";// ͼƬ����
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// �ر���
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
