package enterprises.mccollum.home.icing_legacy.movies.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import enterprises.mccollum.home.icing_legacy.R;
import enterprises.mccollum.home.icing_legacy.movies.MovieFile;
import enterprises.mccollum.home.icing_legacy.utils.ImageFetch;

/**
 * Created by smccollum on 9/24/17.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {
	List<MovieFile> movieFiles;
	Context ctx;
	
	public MovieListAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	public void setMovieFiles(List<MovieFile> movieFiles) {
		this.movieFiles = movieFiles;
		System.out.println("setMovieFiles: " + movieFiles.size());
		notifyDataSetChanged();
	}
	
	@Override
	public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
		return new MovieViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(MovieViewHolder holder, int position) {
		final MovieFile movie = movieFiles.get(position);
		holder.movieTitle.setText(movie.getTitleWithYear());
		
		//actually get the poster
		holder.moviePoster.setImageUrl("https://image.tmdb.org/t/p/w342/" + movie.getMetaData().getPoster_path(),
				ImageFetch.getImageLoader(ctx));
		
		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String videoUrl = null;
				videoUrl = movie.getSource().getServer().getUrl()
								+ "api/raw/"
								+ movie.getSource().getName()
								+ movie.getFilePath().replace(" ", "%20");
				
				System.out.println("video url: " + videoUrl);
				
				Intent video = new Intent(Intent.ACTION_VIEW);
				video.setDataAndType(Uri.parse(videoUrl), movie.getMimeType());
				video.putExtra("title", movie.getTitleWithYear());
				ctx.startActivity(video);
			}
		});
	}
	
	@Override
	public int getItemCount() {
		return (movieFiles == null ? 0 : movieFiles.size());
	}
}
