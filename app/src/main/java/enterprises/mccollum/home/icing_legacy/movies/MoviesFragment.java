package enterprises.mccollum.home.icing_legacy.movies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;

import enterprises.mccollum.home.icing_legacy.IcingApi;
import enterprises.mccollum.home.icing_legacy.R;
import enterprises.mccollum.home.icing_legacy.ResponseWrapper;
import enterprises.mccollum.home.icing_legacy.movies.view.MovieListAdapter;
import enterprises.mccollum.home.icing_legacy.servers.Server;
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
	
	MoviesListViewModel moviesListViewModel;
	
	public MoviesFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_movies, container, false);
		
		refreshLayout = v.findViewById(R.id.movies_swipe_refresh_layout);
		refreshLayout.setOnRefreshListener(this);
		
		moviesListViewModel = ViewModelProviders.of(this).get(MoviesListViewModel.class);
		
		movieListAdapter = new MovieListAdapter(getContext());
		moviesList = v.findViewById(R.id.movies_list);
		moviesList.setAdapter(movieListAdapter);
		moviesList.setLayoutManager(new LinearLayoutManager(getContext()));
		
		subscribe();
		
		return v;
	}
	
	private void subscribe() {
		moviesListViewModel.getServersLiveData().observe(this, servers -> {
			if(servers.size() > 0) {
				refreshMovielist();
			} else {
				refreshLayout.setRefreshing(false);
			}
		});
		
		moviesListViewModel.getMoviesLiveData().observe(this, movieFiles -> {
			movieListAdapter.setMovieFiles(movieFiles);
			refreshLayout.setRefreshing(false);
		});
	}
	
	@Override
	public void onRefresh() {
		refreshMovielist();
	}
	
	private void refreshMovielist(){
		if(moviesListViewModel.getServersLiveData().getValue().size() < 1) {
			Toast.makeText(getContext(), R.string.please_add_at_least_one_server, Toast.LENGTH_LONG).show();
			refreshLayout.setRefreshing(false);
			return;
		}
		for(Server curServer : moviesListViewModel.getServersLiveData().getValue()) {
			final Server server = curServer;
			
			try {
				IcingApi.getMovies(server.getUrl())
						.getAll().enqueue(new Callback<ResponseWrapper<MovieFile>>() {
					@Override
					public void onResponse(Call<ResponseWrapper<MovieFile>> call, Response<ResponseWrapper<MovieFile>> response) {
						ResponseWrapper<MovieFile> movies = response.body();
						Collections.sort(movies.getData());
						for (MovieFile movie : movies.getData()) {
							movie.getSource().setServer(server);
						}
						
						moviesListViewModel.getMoviesLiveData().setValue(movies.getData());
						refreshLayout.setRefreshing(false);
					}
					
					@Override
					public void onFailure(Call<ResponseWrapper<MovieFile>> call, Throwable t) {
						System.err.println("Error getting movies from server");
						t.printStackTrace();
						refreshLayout.setRefreshing(false);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				refreshLayout.setRefreshing(false);
			}
		}
	}
}
