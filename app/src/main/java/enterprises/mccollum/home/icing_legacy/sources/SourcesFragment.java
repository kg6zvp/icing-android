package enterprises.mccollum.home.icing_legacy.sources;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import enterprises.mccollum.home.icing_legacy.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SourcesFragment extends Fragment {
	
	
	public SourcesFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_sources, container, false);
	}
	
}
