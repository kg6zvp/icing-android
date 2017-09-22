package enterprises.mccollum.home.icing_legacy;

import java.util.List;

/**
 * Created by smccollum on 9/23/17.
 */
public class ResponseWrapper<T> {
	int size;
	List<T> data;
	
	public ResponseWrapper(List<T> sources) {
		this.data = sources;
		this.size = sources.size();
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
}

