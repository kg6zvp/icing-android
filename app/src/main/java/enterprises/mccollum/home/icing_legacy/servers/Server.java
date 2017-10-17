package enterprises.mccollum.home.icing_legacy.servers;

import android.arch.persistence.room.Ignore;

/**
 * Created by smccollum on 9/22/17.
 */
public class Server {
	Long id;
	
	String name;
	
	/*@Relation(entity = MediaSource.class, parentColumn = "id", entityColumn = "server_id")
	List<MediaSource> sources;//*/
	
	String context;
	
	String hostnamePort;
	
	Boolean useHttps;
	
	@Ignore
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
		if(url == null){
			url = ServerUtils.buildUrl(useHttps, hostnamePort, context);
		}
		return url;
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	public boolean isStandaloneContext(){
		return ServerUtils.isStandaloneContext(context);
	}
	public boolean isDefaultContext(){
		return ServerUtils.isDefaultContext(context);
	}
	public boolean isCustomContext(){
		return ServerUtils.isCustomContext(context);
	}
	public void setStandaloneContext(){
		this.context = ServerUtils.STANDALONE_CONTEXT;
	}
	public void setDefaultContext(){
		this.context = ServerUtils.DEFAULT_CONTEXT;
	}
	public void setCustomContext(String context) {
		this.context = context;
	}
	
	public String getHostnamePort() {
		return hostnamePort;
	}
	
	public void setHostnamePort(String hostnamePort) {
		this.hostnamePort = hostnamePort;
	}
	
	public Boolean getUseHttps() {
		return useHttps;
	}
	
	public void setUseHttps(Boolean useHttps) {
		this.useHttps = useHttps;
	}
	
	@Override
	public String toString() {
		return ServerUtils.toString(id, name, getUrl());
	}
}
