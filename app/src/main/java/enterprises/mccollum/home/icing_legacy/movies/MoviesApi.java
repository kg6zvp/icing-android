package enterprises.mccollum.home.icing_legacy.movies;

import org.json.JSONObject;

import java.util.List;

import enterprises.mccollum.home.icing_legacy.ResponseWrapper;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by smccollum on 9/21/17.
 */
public interface MoviesApi {
	@GET("api/movies")
	Call<ResponseWrapper<MovieFile>> getAll();
}
