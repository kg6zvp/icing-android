package enterprises.mccollum.home.icing_legacy.servers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by smccollum on 10/6/17.
 */
public class ServerDaoWrapper {
	private final ServerDao serverDao;
	
	public ServerDaoWrapper(ServerDao serverDao) {
		this.serverDao = serverDao;
	}
	
	public LiveData<List<Long>> insertAsync(ServerEntity... servers){
		final MutableLiveData<List<Long>> keys = new MutableLiveData<>();
		HandlerThread ht = new HandlerThread("");
		ht.start();
		Handler h = new Handler(ht.getLooper());
		h.post(() -> keys.postValue(Arrays.asList(serverDao.insert(servers))));
		return keys;
	}
	
	public void deleteAsync(Server... servers){
		HandlerThread ht = new HandlerThread("");
		ht.start();
		Handler h = new Handler(ht.getLooper());
		h.post(() -> {
			for(Server e : servers)
				serverDao.delete(e.getId());
		});
	}
}
