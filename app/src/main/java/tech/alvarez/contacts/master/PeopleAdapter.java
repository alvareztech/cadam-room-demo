package tech.alvarez.contacts.master;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.alvarez.contacts.R;
import tech.alvarez.contacts.db.entity.Contact;
import tech.alvarez.contacts.utils.Util;


public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<Contact> mValues;
    private OnItemClickListener mOnItemClickListener;

    public PeopleAdapter(OnItemClickListener onItemClickListener) {
        mValues = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.contact = mValues.get(position);
        holder.nameTextView.setText(holder.contact.getName());
        holder.phoneTextView.setText(holder.contact.getPhone());

        holder.addressTextView.setText(holder.contact.getAddress());
        holder.emailTextView.setText(holder.contact.getEmail());
        holder.birthdayTextView.setText(Util.formatMin(holder.contact.getBirthday()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.clickItem(holder.contact);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.clickLongItem(holder.contact);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setValues(List<Contact> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTextView;
        public final TextView phoneTextView;
        public final TextView emailTextView;
        public final TextView birthdayTextView;
        public final TextView addressTextView;
        public Contact contact;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
            emailTextView = (TextView) view.findViewById(R.id.emailTextView);
            birthdayTextView = (TextView) view.findViewById(R.id.birthdayTextView);
            addressTextView = (TextView) view.findViewById(R.id.addressTextView);
        }
    }

    interface OnItemClickListener {

        void clickItem(Contact contact);

        void clickLongItem(Contact contact);
    }
}
