package com.zzptc.sky.dbphoneguard.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.zzptc.sky.dbphoneguard.dao.DaoSession;
import com.zzptc.sky.dbphoneguard.dao.ContactInfoDao;

/**
 * Created by Administrator on 2017/5/24.
 */
@Entity(
        active = true, //crud

        nameInDb = "contact_info" //数据库中的表名

)
public class ContactInfo {

    @Property
    private String name;
    @Property
    private String phone;
    @Property
    private String attribute;
    @Property
    private int headColor;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1404638527)
    private transient ContactInfoDao myDao;

    @Generated(hash = 176621087)
    public ContactInfo(String name, String phone, String attribute, int headColor) {
        this.name = name;
        this.phone = phone;
        this.attribute = attribute;
        this.headColor = headColor;
    }

    @Generated(hash = 2019856331)
    public ContactInfo() {
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 323812056)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContactInfoDao() : null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getHeadColor() {
        return this.headColor;
    }

    public void setHeadColor(int headColor) {
        this.headColor = headColor;
    }
}
