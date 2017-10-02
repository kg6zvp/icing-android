package enterprises.mccollum.home.icing_legacy.movies;

import android.support.annotation.NonNull;

/**
 * Created by smccollum on 9/23/17.
 */
public class MovieFile implements Comparable<MovieFile> {
	Long movieId;
	
	MediaSource source;
	String filePath;
	
	String mimeType;
	
	MovieMetadata metaData;
	
	public Long getId() {
		return movieId;
	}
	public void setId(Long id) {
		this.movieId = id;
	}
	public MediaSource getSource() {
		return source;
	}
	public void setSource(MediaSource src) {
		this.source = src;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String relPath) {
		this.filePath = relPath;
	}
	public MovieMetadata getMetaData() {
		return metaData;
	}
	public void setMetaData(MovieMetadata data) {
		this.metaData = data;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getMimeType() {
		if(mimeType == null)
			return "video/*";
		return mimeType;
	}
	
	@Override
	public int compareTo(@NonNull MovieFile movieFile) {
		return getMetaData().getSortTitle().compareTo(movieFile.getMetaData().getSortTitle());
	}
	
	public String getTitleWithYear() {
		return String.format("%s (%s)",
				getMetaData().getTitle(),
				getMetaData().getYear());
	}
}
