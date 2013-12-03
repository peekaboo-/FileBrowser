package com.peekaboo.filebrowset.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.SMBActivity;

/**
 * 
 * @author peekaboo
 *
 */
public class SMBConnectionDialog extends Dialog {

	private Button mButtonOk = null;
	private EditText mEditTextPath = null;
	
	public SMBConnectionDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public SMBConnectionDialog(final Context context) {
		super(context);
		init();
		mButtonOk = (Button) findViewById(R.id.button_ok);
		mEditTextPath = (EditText) findViewById(R.id.edittext_path);
		mButtonOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SMBActivity.class);
				intent.putExtra(SMBActivity.SMB_PATH, mEditTextPath.getText().toString());
				context.startActivity(intent);
				SMBConnectionDialog.this.dismiss();
			}
		});
	}

	private void init() {
		this.setTitle("新建/编辑局域网共享");
		this.setContentView(R.layout.dialog_smb_layout);
	}
}
