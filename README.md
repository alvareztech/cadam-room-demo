# cadam-room-demo

Demo del uso básico de Room, Android Architecture Components.

## TODO

### 1. Guardar el contacto

En `ContactActivity.java`:

```java
long ids = this.contactDao.insertContact(contact);
```

### 2. Mostrar todos los contactos

En `ListActivity.java`:

```java
setContacts(contactDao.getAllContacts());
```

### 3. Mostrar todos los contactos y mantenerse escuchando cambios

En `ListActivity.java` (en lugar del anterior paso):

```java
contactDao.findAllContacts().observeForever(new Observer<List<Contact>>() {
    @Override
    public void onChanged(@Nullable List<Contact> contacts) {
        setContacts(contacts);
        if (contacts == null || contacts.size() < 1) {
            showEmptyMessage();
        }
    }
});
```
