package tech.alvarez.contacts.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tech.alvarez.contacts.db.entity.Contact;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    LiveData<List<Contact>> findAllContacts();

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts WHERE id=:id")
    Contact findContactById(String id);

    @Query("SELECT * FROM contacts WHERE id=:id")
    Contact findContact(long id);

    @Insert(onConflict = IGNORE)
    long insertContact(Contact contact);

    @Update
    int updateContact(Contact contact);

    @Update
    void updateContact(List<Contact> contacts);

    @Delete
    void deleteContact(Contact contact);

    @Query("DELETE FROM contacts")
    void deleteAll();
}
