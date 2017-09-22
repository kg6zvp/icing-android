package enterprises.mccollum.home.icing_legacy.servers;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by smccollum on 9/22/17.
 */
@Entity(tableName = "server")
public class ServerEntity {
	@PrimaryKey(autoGenerate = true)
	Long id;
	
	String name;
	
	String url;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
}
