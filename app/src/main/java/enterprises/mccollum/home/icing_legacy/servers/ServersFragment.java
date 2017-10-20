package enterprises.mccollum.home.icing_legacy.servers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import enterprises.mccollum.home.icing_legacy.R;
import enterprises.mccollum.home.icing_legacy.servers.view.ServerEditDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServersFragment extends Fragment {
	ListViewCompat listView;
	
	Button newBtn;
	
	ServersListViewModel serversListViewModel;
	
	ServerItemAdapter adapter;
	
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
		
		newBtn = v.findViewById(R.id.new_server_button);
		newBtn.setOnClickListener(view -> new ServerEditDialog(this, getContext(), null).show() );
		
		adapter = new ServerItemAdapter(getContext());
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener((adapterView, view, i, l) -> {
			System.out.println("edit " + i);
			Server s = adapter.getItem(i);
			final LiveData<ServerEntity> serverEntityLiveData = serversListViewModel.serverDao().getRaw(s.getId());
			serverEntityLiveData.observe(this, serverEntity -> {
				new ServerEditDialog(this, getContext(), serverEntity).show();
				serverEntityLiveData.removeObservers(this);
			});//*/
		});
		listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
			Server s = adapter.getItem(i);
			serversListViewModel.serverDaoWrapper().deleteAsync(s);
			return true;
		});
		
		subscribe();
		
		return v;
	}
	
	private void subscribe() {
		final ServerItemAdapter adptr = adapter;
		serversListViewModel.getServersList().observe(this, servers -> {
			adptr.clear();
			adptr.addAll(servers);
//			adptr.notifyDataSetChanged();
		});
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
}
