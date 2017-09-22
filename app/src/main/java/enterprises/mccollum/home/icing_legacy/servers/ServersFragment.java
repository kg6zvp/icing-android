package enterprises.mccollum.home.icing_legacy.servers;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import enterprises.mccollum.home.icing_legacy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServersFragment extends Fragment {
	ListViewCompat listView;
	
	Button newBtn;
	
	ServersListViewModel serversListViewModel;
	
	public ServersFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_servers, container, false);
		listView = v.findViewById(R.id.servers_list_view);
		
		serversListViewModel = ViewModelProviders.of(this).get(ServersListViewModel.class);
		
		listView = v.findViewById(R.id.servers_list_view);
		listView.setAdapter(new ServerItemAdapter(getContext()));
		listView.setOnItemClickListener(new ServerClickListener());
		
		newBtn = v.findViewById(R.id.new_server_button);
		newBtn.setOnClickListener(new ServerCreateListener());
		
		subscribe();
		
		return v;
	}
	
	private void subscribe() {
		final Context ctx = getContext();
		final Observer<List<Server>> serverListObserver = new Observer<List<Server>>() {
			@Override
			public void onChanged(@Nullable List<Server> servers) {
				listView.setAdapter(new ServerItemAdapter(ctx, servers));
			}
		};
		
		serversListViewModel.getServersList().observe(this, serverListObserver);
	}
	
	private class ServerClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			System.out.println("TODO onItemClick()");
		}
	}
	
	private class ServerItemAdapter extends ArrayAdapter<Server>{
		public ServerItemAdapter(@NonNull Context context) {
			super(context, R.layout.server_list_item);
		}
		public ServerItemAdapter(@NonNull Context context, @NonNull List<Server> servers) {
			super(context, R.layout.server_list_item, servers);
		}
		
		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.server_list_item, parent, false);
			}
			
			TextView name = convertView.findViewById(R.id.server_name_view);
			TextView url = convertView.findViewById(R.id.server_url_view);
			
			name.setText(getItem(position).getName());
			url.setText(getItem(position).getUrl());
			
			return convertView;
		}
	}
	
	private class ServerCreateListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			final Dialog serverDialog = new Dialog(getContext());
			serverDialog.setContentView(R.layout.server_dialog);
			serverDialog.setTitle("New Server");
			
			final EditText nameField = serverDialog.findViewById(R.id.server_name_field);
			final EditText urlField = serverDialog.findViewById(R.id.server_url_field);
			
			Button saveBtn = serverDialog.findViewById(R.id.save_btn);
			Button cancelBtn = serverDialog.findViewById(R.id.cancel_btn);
			
			cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					serverDialog.dismiss();
				}
			});
			
			saveBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//TOOD: Save to DB
					final ServerEntity server = new ServerEntity();
					server.setName(nameField.getText().toString());
					server.setUrl(urlField.getText().toString());
					HandlerThread ht = new HandlerThread("insert");
					ht.start();
					Handler h = new Handler(ht.getLooper());
					h.post(new Runnable() {
						@Override
						public void run() {
							serversListViewModel.serverDao().insert(server);
						}
					});
					serverDialog.dismiss();
				}
			});
			
			serverDialog.show();
		}
	}
}
