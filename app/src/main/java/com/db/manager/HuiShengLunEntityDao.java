package com.db.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.hu.huproject.Entity.HuiShengLunEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HUI_SHENG_LUN_ENTITY".
*/
public class HuiShengLunEntityDao extends AbstractDao<HuiShengLunEntity, Long> {

    public static final String TABLENAME = "HUI_SHENG_LUN_ENTITY";

    /**
     * Properties of entity HuiShengLunEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, long.class, "key", false, "KEY");
        public final static Property TestingNum = new Property(2, Integer.class, "testingNum", false, "TESTING_NUM");
        public final static Property MaxYuZhangJinLi = new Property(3, String.class, "maxYuZhangJinLi", false, "MAX_YU_ZHANG_JIN_LI");
        public final static Property Bizhi = new Property(4, String.class, "bizhi", false, "BIZHI");
        public final static Property PoDuanLi = new Property(5, String.class, "poDuanLi", false, "PO_DUAN_LI");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
        public final static Property IsSave = new Property(7, boolean.class, "isSave", false, "IS_SAVE");
    }


    public HuiShengLunEntityDao(DaoConfig config) {
        super(config);
    }
    
    public HuiShengLunEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HUI_SHENG_LUN_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"KEY\" INTEGER NOT NULL ," + // 1: key
                "\"TESTING_NUM\" INTEGER," + // 2: testingNum
                "\"MAX_YU_ZHANG_JIN_LI\" TEXT," + // 3: maxYuZhangJinLi
                "\"BIZHI\" TEXT," + // 4: bizhi
                "\"PO_DUAN_LI\" TEXT," + // 5: poDuanLi
                "\"TIME\" TEXT," + // 6: time
                "\"IS_SAVE\" INTEGER NOT NULL );"); // 7: isSave
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HUI_SHENG_LUN_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HuiShengLunEntity entity) {
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
 
        String maxYuZhangJinLi = entity.getMaxYuZhangJinLi();
        if (maxYuZhangJinLi != null) {
            stmt.bindString(4, maxYuZhangJinLi);
        }
 
        String bizhi = entity.getBizhi();
        if (bizhi != null) {
            stmt.bindString(5, bizhi);
        }
 
        String poDuanLi = entity.getPoDuanLi();
        if (poDuanLi != null) {
            stmt.bindString(6, poDuanLi);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
        stmt.bindLong(8, entity.getIsSave() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HuiShengLunEntity entity) {
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
 
        String maxYuZhangJinLi = entity.getMaxYuZhangJinLi();
        if (maxYuZhangJinLi != null) {
            stmt.bindString(4, maxYuZhangJinLi);
        }
 
        String bizhi = entity.getBizhi();
        if (bizhi != null) {
            stmt.bindString(5, bizhi);
        }
 
        String poDuanLi = entity.getPoDuanLi();
        if (poDuanLi != null) {
            stmt.bindString(6, poDuanLi);
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
    public HuiShengLunEntity readEntity(Cursor cursor, int offset) {
        HuiShengLunEntity entity = new HuiShengLunEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // testingNum
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // maxYuZhangJinLi
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // bizhi
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // poDuanLi
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // time
            cursor.getShort(offset + 7) != 0 // isSave
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HuiShengLunEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.getLong(offset + 1));
        entity.setTestingNum(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setMaxYuZhangJinLi(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBizhi(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPoDuanLi(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsSave(cursor.getShort(offset + 7) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HuiShengLunEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HuiShengLunEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HuiShengLunEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
