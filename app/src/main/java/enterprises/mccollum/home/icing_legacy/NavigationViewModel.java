package enterprises.mccollum.home.icing_legacy;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

/**
 * Created by smccollum on 9/20/17.
 */
public class NavigationViewModel extends ViewModel {
	MutableLiveData<Integer> selectedItem = new MutableLiveData<>();
	
	public NavigationViewModel(){
		selectedItem.setValue(0);
	}
	
	public MutableLiveData<Integer> getSelectedItem() {
		return selectedItem;
	}
	
	public void setSelectedItem(Integer selectedItem){
		this.selectedItem.setValue(selectedItem);
	}
}
