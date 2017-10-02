package enterprises.mccollum.home.icing_legacy;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;

import enterprises.mccollum.home.icing_legacy.movies.MoviesFragment;
import enterprises.mccollum.home.icing_legacy.servers.ServersFragment;
import enterprises.mccollum.home.icing_legacy.sources.SourcesFragment;

/**
 * Created by smccollum on 9/20/17.
 */
public class NavigationViewModel extends ViewModel {
	MutableLiveData<Integer> selectedItem = new MutableLiveData<>();
	MoviesFragment moviesFragment;
	ServersFragment serversFragment;
	SourcesFragment sourcesFragment;
	
	public NavigationViewModel(){
		selectedItem.setValue(0);
	}
	
	public MutableLiveData<Integer> getSelectedItem() {
		return selectedItem;
	}
	
	public Fragment getSelectedFragment() {
		switch(getSelectedItem().getValue()){
			case 0: //movies
				return getMoviesFragment();
			case 1: //sources
				return getSourcesFragment();
			case 2: //servers
				return getServersFragment();
			default:
				return null;
		}
	}
	
	private Fragment getMoviesFragment() {
		if(this.moviesFragment == null){
			this.moviesFragment = new MoviesFragment();
		}
		return moviesFragment;
	}
	
	public Fragment getServersFragment(){
		if(this.serversFragment == null){
			this.serversFragment = new ServersFragment();
		}
		return serversFragment;
	}
	
	public Fragment getSourcesFragment(){
		if(this.sourcesFragment == null){
			this.sourcesFragment = new SourcesFragment();
		}
		return sourcesFragment;
	}
	
	public void setSelectedItem(Integer selectedItem){
		this.selectedItem.setValue(selectedItem);
	}
}
