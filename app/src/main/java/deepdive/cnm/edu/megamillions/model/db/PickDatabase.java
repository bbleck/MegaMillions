package deepdive.cnm.edu.megamillions.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import deepdive.cnm.edu.megamillions.model.dao.PickDao;
import deepdive.cnm.edu.megamillions.model.dao.PickNumberDao;
import deepdive.cnm.edu.megamillions.model.db.PickDatabase.Converters;
import deepdive.cnm.edu.megamillions.model.entity.Pick;
import deepdive.cnm.edu.megamillions.model.entity.PickNumber;
import java.util.Date;

@Database(entities = {Pick.class, PickNumber.class},
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters.class)
public abstract class PickDatabase extends RoomDatabase {

  private static final String DB_NAME = "pick_db";

  private static PickDatabase instance = null;

  public abstract PickDao getPickDao();

  public abstract PickNumberDao getPickNumberDao();

  public synchronized static PickDatabase getInstance(Context context){
    if(instance==null){
      instance = Room.databaseBuilder(context.getApplicationContext(), PickDatabase.class, DB_NAME).build();
    }
    return instance;
  }

  public synchronized static void forgetInstance(){
    instance = null;
  }

  public static class Converters{
    @TypeConverter
    public static Date dateFromLong(Long time){
      return (time != null) ? new Date(time) : null;
    }

    @TypeConverter
    public static Long longFromDate(Date date){
      return (date!=null)?date.getTime(): null;
    }
  }


}
