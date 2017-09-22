package enterprises.mccollum.home.icing_legacy;

import enterprises.mccollum.home.icing_legacy.movies.MoviesApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by smccollum on 9/23/17.
 */
public class IcingApi {
	public static MoviesApi getMovies(String baseUrl){
		return new Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(MoviesApi.class);
	}
}
