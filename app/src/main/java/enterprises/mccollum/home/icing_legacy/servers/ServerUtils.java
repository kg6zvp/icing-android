package enterprises.mccollum.home.icing_legacy.servers;

/**
 * Created by smccollum on 16.10.17.
 */
public class ServerUtils {
	public static final String DEFAULT_CONTEXT = "/media",
								STANDALONE_CONTEXT = "/";
	public static final String HTTP_PREFIX = "http://",
								HTTPS_PREFIX = "https://";
	
	public static String buildUrl(boolean useHttps, String ipPort, String context){
		return (useHttps ? HTTPS_PREFIX : HTTP_PREFIX)
							+ ipPort +
				(!context.endsWith("/") ? context + "/" : context);
	}
	public static boolean hasPort(String hostnamePort){
		return (hostnamePort == null || hostnamePort.substring(hostnamePort.indexOf(':')).length() > 0);
	}
	public static boolean validPort(String hostnamePort) {
		if(hostnamePort == null)
			return true;
		if(!hostnamePort.contains(":"))
			return true;
		return (hostnamePort.substring(hostnamePort.indexOf(':')).length() > 0 &&
				hostnamePort.substring(hostnamePort.indexOf(':')).matches("[0-9]*"));
	}
	
	public static boolean isStandaloneContext(String context) {
		return STANDALONE_CONTEXT.equals(context);
	}
	
	public static boolean isDefaultContext(String context) {
		return DEFAULT_CONTEXT.equals(context);
	}
	
	public static boolean isCustomContext(String context) {
		return !(isStandaloneContext(context) || isDefaultContext(context));
	}
	
	public static String toString(Long id, String name, String url){
		return "Server{" +
				"id=" + id +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
