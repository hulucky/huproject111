package com.db.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.hu.huproject.Entity.QianYinLiEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "QIAN_YIN_LI_ENTITY".
*/
public class QianYinLiEntityDao extends AbstractDao<QianYinLiEntity, Long> {

    public static final String TABLENAME = "QIAN_YIN_LI_ENTITY";

    /**
     * Properties of entity QianYinLiEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, long.class, "key", false, "KEY");
        public final static Property TestingNum = new Property(2, Integer.class, "testingNum", false, "TESTING_NUM");
        public final static Property MaxQianyinli = new Property(3, String.class, "maxQianyinli", false, "MAX_QIANYINLI");
        public final static Property Chazhi = new Property(4, String.class, "chazhi", false, "CHAZHI");
        public final static Property EdingQianyinli = new Property(5, String.class, "edingQianyinli", false, "EDING_QIANYINLI");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
        public final static Property IsSave = new Property(7, boolean.class, "isSave", false, "IS_SAVE");
    }


    public QianYinLiEntityDao(DaoConfig config) {
        super(config);
    }
    
    public QianYinLiEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"QIAN_YIN_LI_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"KEY\" INTEGER NOT NULL ," + // 1: key
                "\"TESTING_NUM\" INTEGER," + // 2: testingNum
                "\"MAX_QIANYINLI\" TEXT," + // 3: maxQianyinli
                "\"CHAZHI\" TEXT," + // 4: chazhi
                "\"EDING_QIANYINLI\" TEXT," + // 5: edingQianyinli
                "\"TIME\" TEXT," + // 6: time
                "\"IS_SAVE\" INTEGER NOT NULL );"); // 7: isSave
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"QIAN_YIN_LI_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, QianYinLiEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getKey());
 
        Integer testingNum = entity.getTestingNum();
        if (testingNum != null) {
            stmt.bindLong(3, testingNum);
        }
 
        String maxQianyinli = entity.getMaxQianyinli();
        if (maxQianyinli != null) {
            stmt.bindString(4, maxQianyinli);
        }
 
        String chazhi = entity.getChazhi();
        if (chazhi != null) {
            stmt.bindString(5, chazhi);
        }
 
        String edingQianyinli = entity.getEdingQianyinli();
        if (edingQianyinli != null) {
            stmt.bindString(6, edingQianyinli);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
        stmt.bindLong(8, entity.getIsSave() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, QianYinLiEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getKey());
 
        Integer testingNum = entity.getTestingNum();
        if (testingNum != null) {
            stmt.bindLong(3, testingNum);
        }
 
        String maxQianyinli = entity.getMaxQianyinli();
        if (maxQianyinli != null) {
            stmt.bindString(4, maxQianyinli);
        }
 
        String chazhi = entity.getChazhi();
        if (chazhi != null) {
            stmt.bindString(5, chazhi);
        }
 
        String edingQianyinli = entity.getEdingQianyinli();
        if (edingQianyinli != null) {
            stmt.bindString(6, edingQianyinli);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
        stmt.bindLong(8, entity.getIsSave() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public QianYinLiEntity readEntity(Cursor cursor, int offset) {
        QianYinLiEntity entity = new QianYinLiEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // testingNum
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // maxQianyinli
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // chazhi
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // edingQianyinli
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // time
            cursor.getShort(offset + 7) != 0 // isSave
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, QianYinLiEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.getLong(offset + 1));
        entity.setTestingNum(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setMaxQianyinli(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setChazhi(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEdingQianyinli(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsSave(cursor.getShort(offset + 7) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(QianYinLiEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(QianYinLiEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(QianYinLiEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
