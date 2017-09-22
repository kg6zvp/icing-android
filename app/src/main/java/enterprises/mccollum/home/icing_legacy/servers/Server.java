package enterprises.mccollum.home.icing_legacy.servers;

import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by smccollum on 9/22/17.
 */
public class Server {
	Long id;
	
	String name;
	
	/*@Relation(entity = MediaSource.class, parentColumn = "id", entityColumn = "server_id")
	List<MediaSource> sources;//*/
	
	String url;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/*public List<MediaSource> getSources() {
		return sources;
	}
	public void setSources(List<MediaSource> sources) {
		this.sources = sources;
	}//*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "Server{" +
				"id=" + id +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
