package enterprises.mccollum.home.icing_legacy.movies;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import enterprises.mccollum.home.icing_legacy.IcingApi;
import enterprises.mccollum.home.icing_legacy.IcingDatabase;
import enterprises.mccollum.home.icing_legacy.R;
import enterprises.mccollum.home.icing_legacy.ResponseWrapper;
import enterprises.mccollum.home.icing_legacy.movies.view.MovieListAdapter;
import enterprises.mccollum.home.icing_legacy.servers.Server;
import enterprises.mccollum.home.icing_legacy.servers.ServerDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
	SwipeRefreshLayout refreshLayout;
	RecyclerView moviesList;
	
	MovieListAdapter movieListAdapter;
	
	public MoviesFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_movies, container, false);
		
		refreshLayout = v.findViewById(R.id.movies_swipe_refresh_layout);
		refreshLayout.setOnRefreshListener(this);
		
		movieListAdapter = new MovieListAdapter(getContext());
		moviesList = v.findViewById(R.id.movies_list);
		moviesList.setAdapter(movieListAdapter);
		moviesList.setLayoutManager(new LinearLayoutManager(getContext()));
		
		subscribe();
		
		return v;
	}
	
	private void subscribe() {
		refreshLayout.setRefreshing(true);
		IcingDatabase db = IcingDatabase.get(getContext());
		ServerDao serverDao = db.serverDao();
		serverDao.getAll().observe(this, new Observer<List<Server>>() {
			@Override
			public void onChanged(@Nullable List<Server> servers) {
				if(servers.size() != 1){
					System.out.println("Servers.size != 1: "+ servers.size());
					if(servers.size() < 1) {
						Toast.makeText(getContext(), "Please add an icing server on the Servers screen", Toast.LENGTH_SHORT).show();
					}
					if(servers.size() > 1) {
						Toast.makeText(getContext(), "Please remove an icing server (only 1 supported)", Toast.LENGTH_SHORT).show();
					}
					refreshLayout.setRefreshing(false);
					return;
				}
				final Server server = servers.get(0);
				
				try {
					IcingApi.getMovies(server.getUrl())
							.getAll().enqueue(new Callback<ResponseWrapper<MovieFile>>() {
						@Override
						public void onResponse(Call<ResponseWrapper<MovieFile>> call, Response<ResponseWrapper<MovieFile>> response) {
							System.out.println("URL: " + call.request().url().toString());
							
							System.out.println("code: " + response.code());
							
							System.out.println("movies: " + response.body().getSize());
							ResponseWrapper<MovieFile> movies = response.body();
							Collections.sort(movies.getData());
							for(MovieFile movie : movies.getData()){
								movie.getSource().setServer(server);
							}
							movieListAdapter.setMovieFiles(movies.getData());
							refreshLayout.setRefreshing(false);
						}
						
						@Override
						public void onFailure(Call<ResponseWrapper<MovieFile>> call, Throwable t) {
							System.err.println("Error getting movies from server");
							t.printStackTrace();
						}
					});
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onRefresh() {
		subscribe();
	}
}
