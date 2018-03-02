package tech.alvarez.contacts.detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import tech.alvarez.contacts.R;
import tech.alvarez.contacts.db.AppDatabase;
import tech.alvarez.contacts.db.dao.ContactDao;
import tech.alvarez.contacts.db.entity.Contact;
import tech.alvarez.contacts.utils.Constants;
import tech.alvarez.contacts.utils.Util;

public class ContactActivity extends AppCompatActivity implements DateDialogFragment.DateListener {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText birthdayEditText;
    private EditText phoneEditText;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout addressInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout birthdayInputLayout;
    private TextInputLayout phoneTextInputLayout;

    private FloatingActionButton fab;

    private Contact contact;
    private boolean editMode = false;

    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        AppDatabase db = AppDatabase.getDatabase(getApplication());
        contactDao = db.contactModel();

        contact = new Contact();
        checkMode();

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (editMode) {
            Contact person = contactDao.findContact(contact.getId());
            if (person != null) {
                populate(person);
            }
        }
    }

    private void checkMode() {
        if (getIntent().getExtras() != null) {
            contact.setId(getIntent().getLongExtra(Constants.CONTACT_ID, 0));
            editMode = true;
        }
    }

    private void initViews() {
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        birthdayEditText = (EditText) findViewById(R.id.birthdayEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);

        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        addressInputLayout = (TextInputLayout) findViewById(R.id.addressTextInputLayout);
        emailInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        birthdayInputLayout = (TextInputLayout) findViewById(R.id.birthdayTextInputLayout);
        phoneTextInputLayout = (TextInputLayout) findViewById(R.id.phoneTextInputLayout);

        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(editMode ? R.drawable.ic_refresh : R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contact.setName(nameEditText.getText().toString());
                contact.setAddress(addressEditText.getText().toString());
                contact.setEmail(emailEditText.getText().toString());
                contact.setPhone(phoneEditText.getText().toString());

                boolean valid = validate(contact);

                if (!valid) return;

                if (editMode) {
                    update(contact);
                } else {
                    save(contact);
                }
            }
        });
    }

    public void update(Contact contact) {
        int ids = this.contactDao.updateContact(contact);
        close();
    }

    public void save(Contact contact) {
        long ids = this.contactDao.insertContacto(contact);
        close();
    }


    public void showErrorMessage(int field) {
        if (field == Constants.FIELD_NAME) {
            nameTextInputLayout.setError(getString(R.string.invalid_name));
        } else if (field == Constants.FIELD_EMAIL) {
            emailInputLayout.setError(getString(R.string.invalid_email));
        } else if (field == Constants.FIELD_PHONE) {
            phoneTextInputLayout.setError(getString(R.string.invalid_phone));
        } else if (field == Constants.FIELD_ADDRESS) {
            addressInputLayout.setError(getString(R.string.invalid_address));
        } else if (field == Constants.FIELD_BIRTHDAY) {
            birthdayInputLayout.setError(getString(R.string.invalid_birthday));
        }
    }

    public void clearPreErrors() {
        nameTextInputLayout.setErrorEnabled(false);
        emailInputLayout.setErrorEnabled(false);
        phoneTextInputLayout.setErrorEnabled(false);
        addressInputLayout.setErrorEnabled(false);
        birthdayInputLayout.setErrorEnabled(false);
    }

    public void openDateDialog() {
        DateDialogFragment fragment = new DateDialogFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void close() {
        finish();
    }

    public void populate(Contact contact) {
        this.contact = contact;
        nameEditText.setText(contact.getName());
        addressEditText.setText(contact.getAddress());
        emailEditText.setText(contact.getEmail());
        birthdayEditText.setText(Util.format(contact.getBirthday()));
        phoneEditText.setText(contact.getPhone());
    }

    public void setSelectedDate(Date date) {
        contact.setBirthday(date);
        birthdayEditText.setText(Util.format(date));
    }

    public boolean validate(Contact contact) {
        clearPreErrors();
        if (contact.getName().isEmpty() || !Util.isValidName(contact.getName())) {
            showErrorMessage(Constants.FIELD_NAME);
            return false;
        }
        if (contact.getAddress().isEmpty()) {
            showErrorMessage(Constants.FIELD_ADDRESS);
            return false;
        }
        if (contact.getPhone().isEmpty() || !Util.isValidPhone(contact.getPhone())) {
            showErrorMessage(Constants.FIELD_PHONE);
            return false;
        }
        if (contact.getEmail().isEmpty() || !Util.isValidEmail(contact.getEmail())) {
            showErrorMessage(Constants.FIELD_EMAIL);
            return false;
        }
        if (contact.getBirthday() == null) {
            showErrorMessage(Constants.FIELD_BIRTHDAY);
            return false;
        }

        return true;
    }

}
