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
	boolean edit;
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
		
		edit = true;
		this.server = server;
		if(server == null) {
			edit = false;
			this.server = new ServerEntity();
		}
		
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
		
		restoreStateFromSaved();
	}
	
	private void restoreStateFromSaved(){
		if(!edit)
			return;
		
		nameField.setText(server.getName());
		
		hostField.setText(server.getHostnamePort());
		
		httpsCbx.setChecked(server.getUseHttps());
		
		if(server.isStandaloneContext()){
			standaloneServerRdo.setChecked(true);
		}else{
			appServerRdo.setChecked(true);
			if(server.isCustomContext()){
				customCtxCbx.setChecked(true);
				customCtxTxt.setText(server.getContext());
			}
		}
	}
	
	//SAVE
	@Override
	public void onClick(View view) {
		//name validation
		if(TextUtils.isEmpty(nameField.getText().toString())){
			nameField.setError(getContext().getString(R.string.must_include_server_name));
			return;
		}else{
			nameField.setError(null);
			server.setName(nameField.getText().toString());
		}
		
		//hostname validation
		String hostnamePort = hostField.getText().toString();
		if(TextUtils.isEmpty(hostnamePort)){
			hostField.setError(getContext().getString(R.string.must_include_server_hostname));
			return;
		}else if(hostnamePort.contains("/")){
			hostField.setError(getContext().getString(R.string.hostname_must_not_be_url));
			return;
		}else if(!server.validPort()){
			hostField.setError(
					String.format(
							getContext().getString(R.string.invalid_port),
							hostnamePort.substring(hostnamePort.indexOf(':'))
					)
			);
			return;
		}else{
			hostField.setError(null);
			server.setHostnamePort(hostnamePort);
		}
		
		//set HTTP or HTTPS
		server.setUseHttps(httpsCbx.isChecked());
		
		//set context
		if(standaloneServerRdo.isChecked()){ //if use standalone
			server.setStandaloneContext();
		}else if(appServerRdo.isChecked()){
			if(!customCtxCbx.isChecked()){ //if use default
				server.setDefaultContext();
			}else{
				String serverContext = customCtxTxt.getText().toString();
				if(TextUtils.isEmpty(serverContext)){
					server.setDefaultContext();
				}else if(!serverContext.startsWith("/")){
					customCtxTxt.setError(getContext().getString(R.string.custom_ctx_must_start_with_slash));
				}else{
					server.setCustomContext(serverContext);
				}
			}
		}
		
		System.out.println("Server: " + server.toString());
		LiveData<List<Long>> rKeys = IcingDatabase.get(getContext()).serverDaoWrapper().insertAsync(server);
		final Context ctx = getContext();
		rKeys.observe(lifecycleOwner, (keys) -> {
			Toast.makeText(ctx, R.string.server_saved_successfully, Toast.LENGTH_SHORT).show();
		});
		dismiss();
	}
}
