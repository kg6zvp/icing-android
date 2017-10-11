package enterprises.mccollum.home.icing_legacy.movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import enterprises.mccollum.home.icing_legacy.IcingDatabase;
import enterprises.mccollum.home.icing_legacy.servers.Server;
import enterprises.mccollum.home.icing_legacy.servers.ServerDao;

/**
 * Created by smccollum on 9/23/17.
 */
public class MoviesListViewModel extends AndroidViewModel {
	LiveData<List<Server>> serversLiveData;
	MutableLiveData<List<MovieFile>> moviesLiveData;
	
	public MoviesListViewModel(Application application) {
		super(application);
		
		IcingDatabase db = IcingDatabase.get(application);
		ServerDao serverDao = db.serverDao();
		serversLiveData = serverDao.getAll();
	}
	
	public LiveData<List<Server>> getServersLiveData() {
		return serversLiveData;
	}
	public void setServersLiveData(LiveData<List<Server>> serversLiveData) {
		this.serversLiveData = serversLiveData;
	}
	public MutableLiveData<List<MovieFile>> getMoviesLiveData() {
		if(moviesLiveData == null)
			moviesLiveData = new MutableLiveData<>();
		return moviesLiveData;
	}
	public void setMoviesLiveData(MutableLiveData<List<MovieFile>> moviesLiveData) {
		this.moviesLiveData = moviesLiveData;
	}
}
