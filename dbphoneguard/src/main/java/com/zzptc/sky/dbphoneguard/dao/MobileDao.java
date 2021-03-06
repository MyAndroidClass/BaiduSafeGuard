package com.zzptc.sky.dbphoneguard.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zzptc.sky.dbphoneguard.entity.Mobile;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "dm_mobile".
*/
public class MobileDao extends AbstractDao<Mobile, Void> {

    public static final String TABLENAME = "dm_mobile";

    /**
     * Properties of entity Mobile.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Integer.class, "id", false, "ID");
        public final static Property MobileNumber = new Property(1, String.class, "mobileNumber", false, "MobileNumber");
        public final static Property MobileArea = new Property(2, String.class, "mobileArea", false, "MobileArea");
        public final static Property MobileType = new Property(3, String.class, "mobileType", false, "MobileType");
        public final static Property AreaCode = new Property(4, String.class, "areaCode", false, "AreaCode");
        public final static Property PostCode = new Property(5, String.class, "postCode", false, "PostCode");
    }


    public MobileDao(DaoConfig config) {
        super(config);
    }
    
    public MobileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Mobile entity) {
        stmt.clearBindings();
 
        Integer id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mobileNumber = entity.getMobileNumber();
        if (mobileNumber != null) {
            stmt.bindString(2, mobileNumber);
        }
 
        String mobileArea = entity.getMobileArea();
        if (mobileArea != null) {
            stmt.bindString(3, mobileArea);
        }
 
        String mobileType = entity.getMobileType();
        if (mobileType != null) {
            stmt.bindString(4, mobileType);
        }
 
        String areaCode = entity.getAreaCode();
        if (areaCode != null) {
            stmt.bindString(5, areaCode);
        }
 
        String postCode = entity.getPostCode();
        if (postCode != null) {
            stmt.bindString(6, postCode);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Mobile entity) {
        stmt.clearBindings();
 
        Integer id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mobileNumber = entity.getMobileNumber();
        if (mobileNumber != null) {
            stmt.bindString(2, mobileNumber);
        }
 
        String mobileArea = entity.getMobileArea();
        if (mobileArea != null) {
            stmt.bindString(3, mobileArea);
        }
 
        String mobileType = entity.getMobileType();
        if (mobileType != null) {
            stmt.bindString(4, mobileType);
        }
 
        String areaCode = entity.getAreaCode();
        if (areaCode != null) {
            stmt.bindString(5, areaCode);
        }
 
        String postCode = entity.getPostCode();
        if (postCode != null) {
            stmt.bindString(6, postCode);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Mobile readEntity(Cursor cursor, int offset) {
        Mobile entity = new Mobile( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // mobileNumber
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // mobileArea
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // mobileType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // areaCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // postCode
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Mobile entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setMobileNumber(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMobileArea(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMobileType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAreaCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPostCode(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Mobile entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Mobile entity) {
        return null;
    }

    @Override
    public boolean hasKey(Mobile entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
