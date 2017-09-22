package enterprises.mccollum.home.icing_legacy;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class IcingMain extends AppCompatActivity {
	NavigationViewModel navigationViewModel;
	String[] drawerItemStrings;
	DrawerLayout drawerLayout;
	ListView drawerList;
	ActionBarDrawerToggle drawerToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_icing);
		
		navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);
		
		drawerItemStrings = getResources().getStringArray(R.array.drawer_items);
		drawerLayout = (DrawerLayout)findViewById(R.id.icing_drawer_layout);
		drawerList = (ListView)findViewById(R.id.navigation_drawer);
		
		drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerItemStrings));
		drawerList.setOnItemClickListener(new DrawerClickListener());
		
		//Setup drawer toggle
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_expand_description, R.string.drawer_close_description);
		drawerLayout.addDrawerListener(drawerToggle);
		drawerToggle.syncState();
		
		subscribe(); //subscribe to lifecycle-aware changes :D
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(drawerToggle.onOptionsItemSelected(item)) //necessary for drawer toggle to work via button
			return true;
		return super.onOptionsItemSelected(item);
	}
	
	void subscribe(){
		final Observer<Integer> selectionObserver = new Observer<Integer>() {
			@Override
			public void onChanged(@Nullable Integer selection) {
				makeSelection(selection);
			}
		};
		
		navigationViewModel.getSelectedItem().observe(this, selectionObserver);
	}
	
	private void makeSelection(Integer selection) {
		System.out.println("Selection: " + selection);
		Fragment fragment = navigationViewModel.getSelectedFragment();
		
		if(fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment)
					.commit();//*/
		}
		
		drawerList.setItemChecked(selection, true);
		getSupportActionBar().setTitle(drawerItemStrings[selection]);
		drawerLayout.closeDrawer(drawerList);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	private class DrawerClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			navigationViewModel.setSelectedItem(position);
		}
	}
}
