package enterprises.mccollum.home.icing_legacy.servers;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by smccollum on 9/22/17.
 */
@Dao
public interface ServerDao {
	@Query("SELECT * from server")
	LiveData<List<Server>> getAll();
	
	@Query("SELECT * from server")
	List<Server> getAllSynchronous();
	
	@Query("SELECT * from server WHERE id = :id LIMIT 1")
	LiveData<Server> get(long id);
	
	@Query("SELECT * from server WHERE id = :id LIMIT 1")
	LiveData<ServerEntity> getRaw(long id);
	
	@Query("SELECT * from server where name = :name LIMIT 1")
	LiveData<Server> getByName(String name);
	
	@Insert(onConflict = REPLACE)
	Long[] insert(ServerEntity...servers);
	
	@Update
	void update(ServerEntity...servers);
	
	@Delete
	void delete(ServerEntity...servers);
	
	@Query("DELETE from server where id = :id")
	void delete(Long id);
}
