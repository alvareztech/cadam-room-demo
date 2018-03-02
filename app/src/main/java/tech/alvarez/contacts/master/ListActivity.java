package tech.alvarez.contacts.master;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import tech.alvarez.contacts.R;
import tech.alvarez.contacts.db.AppDatabase;
import tech.alvarez.contacts.db.dao.ContactDao;
import tech.alvarez.contacts.db.entity.Contact;
import tech.alvarez.contacts.detail.ContactActivity;
import tech.alvarez.contacts.utils.Constants;

public class ListActivity extends AppCompatActivity implements PeopleAdapter.OnItemClickListener, DeleteConfirmFragment.DeleteListener {

    private PeopleAdapter mPeopleAdapter;
    private TextView mEmptyTextView;

    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        AppDatabase db = AppDatabase.getDatabase(getApplication());
        contactDao = db.contactModel();

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateContacts();
    }

    private void initViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPerson();
            }
        });

        mEmptyTextView = (TextView) findViewById(R.id.emptyTextView);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPeopleAdapter = new PeopleAdapter(this);
        recyclerView.setAdapter(mPeopleAdapter);
    }

    public void populateContacts() {
        contactDao.findAllContacts().observeForever(new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                setContacts(contacts);
                if (contacts == null || contacts.size() < 1) {
                    showEmptyMessage();
                }
            }
        });
    }

    public void showAddPerson() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void setContacts(List<Contact> contacts) {
        mEmptyTextView.setVisibility(View.GONE);
        mPeopleAdapter.setValues(contacts);
    }

    public void showEditScreen(long id) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(Constants.CONTACT_ID, id);
        startActivity(intent);
    }

    public void showDeleteConfirmDialog(Contact contact) {
        DeleteConfirmFragment fragment = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.CONTACT_ID, contact.getId());
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "confirmDialog");
    }

    public void showEmptyMessage() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }


    public void clickItem(Contact contact) {
        showEditScreen(contact.getId());
    }

    public void clickLongItem(Contact contact) {
        showDeleteConfirmDialog(contact);
    }

    public void setConfirm(boolean confirm, long personId) {
        if (confirm) {
            Contact contact = contactDao.findContact(personId);
            contactDao.deleteContact(contact);
        }
    }
}
