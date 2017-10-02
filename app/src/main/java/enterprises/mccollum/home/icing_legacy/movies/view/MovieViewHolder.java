package enterprises.mccollum.home.icing_legacy.movies.view;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import enterprises.mccollum.home.icing_legacy.R;
import enterprises.mccollum.home.icing_legacy.movies.MovieFile;
import enterprises.mccollum.home.icing_legacy.servers.ServerDao;

/**
 * Created by smccollum on 9/24/17.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
	public TextView movieTitle;
	public NetworkImageView moviePoster;
	public View view;
	
	public MovieViewHolder(View itemView) {
		super(itemView);
		
		view = itemView;
		
		movieTitle = itemView.findViewById(R.id.movie_title);
		moviePoster = itemView.findViewById(R.id.movie_poster);
		moviePoster.setDefaultImageResId(android.R.drawable.ic_menu_recent_history);
	}
}