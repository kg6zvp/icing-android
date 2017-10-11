package enterprises.mccollum.home.icing_legacy.servers.view;

import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import enterprises.mccollum.home.icing_legacy.IcingDatabase;
import enterprises.mccollum.home.icing_legacy.R;
import enterprises.mccollum.home.icing_legacy.servers.ServerEntity;

/**
 * Created by smccollum on 10/5/17.
 */
public class ServerEditDialog extends Dialog implements View.OnClickListener {
	LifecycleOwner lifecycleOwner;
	ServerEntity server;
	
	final EditText nameField;
	final EditText hostField;
	
	Button saveBtn;
	Button cancelBtn;
	
	RadioGroup radioGroup;
	RadioButton standaloneServerRdo;
	RadioButton appServerRdo;
	
	CheckBox customCtxCbx;
	EditText customCtxTxt;
	
	CheckBox httpsCbx;
	
	public ServerEditDialog(LifecycleOwner lifecycleOwner, @NonNull Context context, ServerEntity server) {
		super(context, R.style.AppDialog);
		
		this.server = server;
		if(server == null)
			this.server = new ServerEntity();
		
		this.lifecycleOwner = lifecycleOwner;
		
		setTitle(getContext().getString(server == null ? R.string.new_server : R.string.edit_server));
		setContentView(R.layout.server_dialog);
		
		nameField = findViewById(R.id.server_name_field);
		hostField = findViewById(R.id.server_hostname_field);
		
		radioGroup = findViewById(R.id.context_group);
		standaloneServerRdo = findViewById(R.id.standalone_rdo);
		appServerRdo = findViewById(R.id.app_svr_rdo);
		
		customCtxCbx = findViewById(R.id.custom_ctx_cbx);
		customCtxTxt = findViewById(R.id.srvr_ctx_txt);
		
		httpsCbx = findViewById(R.id.https_cbx);
		
		cancelBtn = findViewById(R.id.cancel_btn);
		cancelBtn.setOnClickListener(view -> dismiss());
		
		saveBtn = findViewById(R.id.save_btn);
		//saveBtn.setEnabled(false);
		
		//take care of context
		appServerRdo.setOnCheckedChangeListener((compoundButton, value) -> {
			if(value){
				//appServer
				customCtxCbx.setVisibility(View.VISIBLE);
				customCtxTxt.setVisibility(customCtxCbx.isChecked() ? View.VISIBLE : View.INVISIBLE);
			}else{
				//standalone
				customCtxCbx.setVisibility(View.INVISIBLE);
				customCtxTxt.setVisibility(View.INVISIBLE);
			}
		});
		customCtxCbx.setOnCheckedChangeListener((btn, value) -> customCtxTxt.setVisibility(value ? View.VISIBLE : View.INVISIBLE));
		
		//Save btn
		saveBtn.setOnClickListener(this);
	}
	
	boolean validPort(String hostname) {
		if(!hostname.contains(":"))
			return true;
		return hostname.substring(hostname.indexOf(':')).matches("[0-9]*");
	}
	
	String buildServerUrl(){
		StringBuilder urlBuilder = new StringBuilder(60);
		urlBuilder.append(httpsCbx.isChecked() ? "https://" : "http://"); //protocol
		urlBuilder.append(hostField.getText().toString());
		if(standaloneServerRdo.isChecked()){
			//standalone
			urlBuilder.append("/");
		}else {
			//app server
			if(customCtxCbx.isChecked()){
				String customCtx = customCtxTxt.getText().toString();
				if(!customCtx.startsWith("/"))
					urlBuilder.append('/');
				urlBuilder.append(customCtx);
			}else{
				urlBuilder.append("/media");
			}
		}
		return urlBuilder.toString();
	}
	
	//SAVE
	@Override
	public void onClick(View view) {
		//name validation
		if(TextUtils.isEmpty(nameField.getText().toString())){
			nameField.setError(getContext().getString(R.string.must_include_server_name));
			return;
		}
		
		//hostname validations
		String hostname = hostField.getText().toString();
		if(TextUtils.isEmpty(hostname)){
			hostField.setError(getContext().getString(R.string.must_include_server_hostname));
			return;
		}else if(hostname.contains("/")){
			hostField.setError(getContext().getString(R.string.hostname_must_not_be_url));
			return;
		}else if(!validPort(hostname)){
			hostField.setError(
					String.format(
							getContext().getString(R.string.invalid_port),
							hostname.substring(hostname.indexOf(':'))
					)
			);
			return;
		}
		server.setName(nameField.getText().toString());
		String serverUrl = buildServerUrl();
		System.out.println("url: " + serverUrl);
		server.setUrl(serverUrl);
		
		LiveData<List<Long>> rKeys = IcingDatabase.get(getContext()).serverDaoWrapper().insertAsync(server);
		final Context ctx = getContext();
		rKeys.observe(lifecycleOwner, (keys) -> {
			Toast.makeText(ctx, R.string.server_saved_successfully, Toast.LENGTH_SHORT).show();
		});
		dismiss();
	}
}
