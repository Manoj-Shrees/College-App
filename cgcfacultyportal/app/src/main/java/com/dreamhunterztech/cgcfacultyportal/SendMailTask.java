package com.dreamhunterztech.cgcfacultyportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.BooleanNode;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.List;
import java.util.Objects;

public class SendMailTask extends AsyncTask<Object, Integer,Objects> {

	private Dialog forgotpassdialog, forgotpassotpdialog, forgotpasschangedialog;
	private Activity sendMailActivity;
	private Button frgtpassgenotp, frgtpasssubotp,changepass;
	private TextView forgothead, mailhint,newpass,confirmpass;
	private ProgressBar otpprg;
	private EditText mailinput, otpinput;
	private Firebase checkref;
	private ImageView frgtpassdialogclose, frgtpassotpdialogclose, frgtpasschangedialogclose;
	private StateProgressBar stateProgressBar;
	private Firebase mref;
	private int state = 0;
	private Boolean chk = true;
	private String mailid = "", temp = "", OTP = "",dataloc;
	private String[] descriptionData = {"Start", "Check", "OTP code", "Done"};

	public SendMailTask(Activity activity) {
		sendMailActivity = activity;
	}

	@Override
	protected Objects doInBackground(Object... args) {
		chk=true;
		while (chk) {

			if (isCancelled()==true)
			{
				break;
			}

				if (state == 0) {
					publishProgress(0);
					setemailidhint(args);
				}

				else if (state == 1)
				{

					if (mailid.equals(temp)) {
						publishProgress(1);
						state = 2;
					}

					else {
						publishProgress(7);
						state = 12;
					}
				}

				else if (state == 2) {
					updateOTP(args);
					state = 3;
				}

				else if (state == 3) {
					publishProgress(2);
					state = 4;
				}

				else if (state == 4) {
					genrateoptmail(args);
				}

				else if (state == 5) {
					publishProgress(3);
					state = 6;
				}

				else if (state == 6) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					publishProgress(4);
				}

				else if (state == 7) {
					checkotp(args);
				}

				else if (state == 8) {
					if (OTP.equals(temp)) {
						publishProgress(5);
					} else {
						publishProgress(8);
						state = 12;
					}
				}

				else if (state == 9) {
					publishProgress(9);
					state = 10;
				}

				else if (state == 10) {
					break;
				}

			}

		return null;
	}

	protected void onPreExecute() {
		createdialog1();
		createdialog2();
		createdialog3();
		state=0;
		forgotpassdialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		switch (values[0]) {

			case 0:
				stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
				break;

			case 1:
				stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
				break;

			case 2:
				stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
				break;

			case 3:
				stateProgressBar.enableAnimationToCurrentState(false);
				stateProgressBar.setAllStatesCompleted(true);
				break;

			case 4:
				forgotpassdialog.dismiss();
				forgotpassotpdialog.show();
				break;
			case 5:
				forgotpasschangedialog.show();
				forgotpassotpdialog.dismiss();
				break;

			case 7:
				mailinput.setError("Mail ID not matched");
				break;

			case 8:
				otpinput.setError("OTP not matched");
				frgtpasssubotp.setEnabled(true);
				otpprg.setVisibility(View.GONE);
				break;

			case 9:
				forgotpasschangedialog.dismiss();
				AlertDialog.Builder passchangemsg = new AlertDialog.Builder(sendMailActivity);
				passchangemsg.setTitle("Message");
				passchangemsg.setMessage("\npassword changed Sucessfully");
				passchangemsg.setPositiveButton("OK",null);
				passchangemsg.show();
				new Login_frame().closeasynctask();
				break;

		}

	}


	private void createdialog1() {
		forgotpassdialog = new Dialog(sendMailActivity);
		forgotpassdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotpassdialog.getWindow().setWindowAnimations(R.style.animateddialog);
		forgotpassdialog.setCanceledOnTouchOutside(false);
		forgotpassdialog.setCancelable(false);
		forgotpassdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		forgotpassdialog.setContentView(R.layout.forgotpasslayout);
		frgtpassdialogclose = (ImageView) forgotpassdialog.findViewById(R.id.frgtdialogclose);
		forgothead = (TextView) forgotpassdialog.findViewById(R.id.frgtpassnotice);
		forgothead.setSelected(true);
		frgtpassgenotp = (Button) forgotpassdialog.findViewById(R.id.frgtpassgenotp);
		mailhint = (TextView) forgotpassdialog.findViewById(R.id.frgtpasshintmail);
		mailinput = (EditText) forgotpassdialog.findViewById(R.id.frgtpassinputemail);
		stateProgressBar = (StateProgressBar) forgotpassdialog.findViewById(R.id.progress_bar);
		stateProgressBar.setStateDescriptionData(descriptionData);
		frgtpassgenotp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				temp = mailinput.getText().toString().trim();
				state = 1;
			}
		});
		frgtpassdialogclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				forgotpassdialog.dismiss();
				chk=false;
				state=10;
				new Login_frame().closeasynctask();
				cancel(true);
			}
		});

	}

	private void createdialog2() {
		forgotpassotpdialog = new Dialog(sendMailActivity);
		forgotpassotpdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotpassotpdialog.getWindow().setWindowAnimations(R.style.animateddialog);
		forgotpassotpdialog.setCanceledOnTouchOutside(false);
		forgotpassotpdialog.setCancelable(false);
		forgotpassotpdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		forgotpassotpdialog.setContentView(R.layout.frgtotplayout);
		otpinput = (EditText) forgotpassotpdialog.findViewById(R.id.optinput);
		otpprg = (ProgressBar) forgotpassotpdialog.findViewById(R.id.otpprogressbar);
		frgtpasssubotp = (Button) forgotpassotpdialog.findViewById(R.id.frgtsubotp);
		frgtpasssubotp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				temp = otpinput.getText().toString().trim();
				state = 7;
				frgtpasssubotp.setEnabled(false);
				otpprg.setVisibility(View.VISIBLE);

			}
		});
		frgtpassotpdialogclose = (ImageView) forgotpassotpdialog.findViewById(R.id.frgtotpdialogclosebtn);
		frgtpassotpdialogclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				forgotpassotpdialog.dismiss();
				new Login_frame().closeasynctask();
				cancel(true);
			}
		});
	}

	private void createdialog3() {
		forgotpasschangedialog = new Dialog(sendMailActivity);
		forgotpasschangedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotpasschangedialog.getWindow().setWindowAnimations(R.style.animateddialog);
		forgotpasschangedialog.setCanceledOnTouchOutside(false);
		forgotpasschangedialog.setCancelable(false);
		forgotpasschangedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		forgotpasschangedialog.setContentView(R.layout.frgtchangelayout);
		 newpass = (TextView) forgotpasschangedialog.findViewById(R.id.frgtinputpass);
		 confirmpass = (TextView) forgotpasschangedialog.findViewById(R.id.frgtchkpass);
		 changepass = (Button) forgotpasschangedialog.findViewById(R.id.frgtchangepass);
		 changepass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (newpass.getText().toString().trim().equals(confirmpass.getText().toString().trim()))
				{
					mref = new Firebase("https://cgc-database.firebaseio.com/" +dataloc+"/Password");
					mref.setValue(newpass.getText().toString());
					changepass.setEnabled(false);
					state=9;
				}

				else
				{
					confirmpass.setError("pass not matched");
				}
			}
		});
		frgtpassotpdialogclose = (ImageView) forgotpasschangedialog.findViewById(R.id.changepassdialogclose);
		frgtpassotpdialogclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chk = false;
				cancel(true);
				new Login_frame().closeasynctask();
				forgotpasschangedialog.dismiss();
			}
		});
	}


	private void setemailidhint(final Object... args) {
		mailid = args[5].toString().trim();
		int loc = mailid.indexOf("@");
		temp = mailid.substring(1, loc);
		temp = mailid.replace(temp, "xxxxx");
		mailhint.setText("   " + temp + "  ");
		state = 12;

	}

	private void genrateoptmail(Object... args) {
		try {
			GMail androidEmail = new GMail(args[0].toString(),
					args[1].toString(), (List) args[2], args[3].toString(),
					args[4].toString());
			androidEmail.createEmailMessage();
			androidEmail.sendEmail();
		}
		catch (Exception e) {
		}

		state = 5;
	}


	private void updateOTP(final Object... args) {
		checkref = new Firebase("https://cgc-database.firebaseio.com/" + args[6] + "/" + args[7]);
		mref=checkref;
		dataloc=args[6].toString()+"/"+args[7];
		checkref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.hasChild("OTPCODE")) {
					checkref.child("OTPCODE").setValue(args[8]);
				} else {
					Firebase optref = checkref.child("OTPCODE");
					optref.setValue(args[8]);
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {
			}
		});

	}


	private void checkotp(final Object... args) {
		checkref = new Firebase("https://cgc-database.firebaseio.com/" + args[6] + "/" + args[7] + "/OTPCODE");
		checkref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				OTP = dataSnapshot.getValue(String.class);
				state = 8;
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {

			}
		});


	}

	@Override
	protected void onCancelled() {
		chk = false;
	}

	@Override
	protected void onPostExecute(Objects objects) {
		super.onPostExecute(objects);
	}
}



