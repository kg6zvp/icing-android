package enterprises.mccollum.home.icing_legacy;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import enterprises.mccollum.home.icing_legacy.servers.ServerDao;
import enterprises.mccollum.home.icing_legacy.servers.ServerDaoWrapper;
import enterprises.mccollum.home.icing_legacy.servers.ServerEntity;

/**
 * Created by smccollum on 9/22/17.
 */
@Database(entities = {ServerEntity.class}, version = 1)
public abstract class IcingDatabase extends RoomDatabase {
	private static ServerDao serverDao = null;
	
	public static IcingDatabase get(Context ctx){
		return Room.databaseBuilder(ctx.getApplicationContext(), IcingDatabase.class, "icing_db").build();
	}
	public abstract ServerDao getServerDao();
	
	public ServerDao serverDao(){
		if(serverDao == null)
			serverDao = getServerDao();
		return serverDao;
	}
	
	public ServerDaoWrapper serverDaoWrapper(){
		return new ServerDaoWrapper(serverDao());
	}
}
