package enterprises.mccollum.home.icing_legacy.servers;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import enterprises.mccollum.home.icing_legacy.IcingDatabase;

/**
 * Created by smccollum on 9/22/17.
 */
public class ServersListViewModel extends AndroidViewModel {
	LiveData<List<Server>> serversLiveData;
	
	IcingDatabase db;
	
	ServerDao serverDao;
	ServerDaoWrapper serverDaoWrapper;
	
	public ServersListViewModel(Application application){
		super(application);
		db = IcingDatabase.get(application);
		serverDao = db.serverDao();
		serverDaoWrapper = db.serverDaoWrapper();
		serversLiveData = serverDao.getAll();
	}
	
	public ServerDao serverDao(){
		return serverDao;
	}
	
	public LiveData<List<Server>> getServersList(){
		return serversLiveData;
	}
	
	public ServerDaoWrapper serverDaoWrapper() {
		return serverDaoWrapper;
	}
}
