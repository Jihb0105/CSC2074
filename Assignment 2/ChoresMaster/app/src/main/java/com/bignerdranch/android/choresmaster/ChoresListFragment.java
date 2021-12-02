package com.bignerdranch.android.choresmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChoresListFragment extends Fragment {

    private RecyclerView cChoresList;
    private ChoresAdapter cAdapter;

    private ChoresList choreslist;
    private List<Chores> chores;

    //Retrieve menu callbacks
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override //Linking chores_add_recyclerview to ChoresListFragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chores_add_recyclerview, container, false);

        cChoresList = (RecyclerView) view.findViewById(R.id.chores_recycler_view);
        cChoresList.setLayoutManager(new LinearLayoutManager(getActivity()));

        update();

        return view;
    }

    @Override
    public void onResume(){ //to update the fragment view
        super.onResume();
        update();
    }

    //Inflating menu resource
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chores_list_activity, menu);
    }

    //Respond to MenuItem Selection or toolbar
    //Adding the add image into the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_chore:
                Chores chores = new Chores();
                ChoresList.get(getActivity()).addChores(chores); //Adding chores to the list
                //Back to ChoresListFragment by Intent
                Intent i = ChoresViewActivity.newIntent(getActivity(), chores.getId());
                startActivity(i);
                return true;
            case R.id.count_chores:
                updateCount(); //setting up the toolbar to hold the chores counter
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Method to count the numbers of chores in the recyclerview
    public void updateCount(){
        ChoresList chores = ChoresList.get(getActivity());
        int choresCount = chores.getChores().size(); // count the number of chores in the list
        String counter = getString(R.string.count_format, choresCount);
        //to get access to AppCompatActivity
        AppCompatActivity act = (AppCompatActivity) getActivity();
        act.getSupportActionBar().setSubtitle(counter);
    }

    //ViewHolder will hold on to the chores_list_activity.xml
    public class ChoresListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView cChoresTitle;
        public TextView cChoresDate;
        private ImageView cCompletedImage;
        private ImageView cThumbnail;
        private Chores cChore;
        private TextDrawable mDrawableBuilder;
        private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;

        public ChoresListHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.chores_list_activity, parent, false));
            itemView.setOnClickListener(this); //set listener to all the widgets

            cThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            cChoresTitle = (TextView) itemView.findViewById(R.id.recycle_title);
            cChoresDate = (TextView) itemView.findViewById(R.id.recycle_date);
            cCompletedImage = (ImageView) itemView.findViewById(R.id.active_image);
        }

        public void bind(Chores chores){
            cChore = chores;

            //Setting up title text
            String title = cChore.getTitle();
            setChoresTitle(title);

            //Setting the date text
            //changing date format
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a ");
            String date = dateFormat.format( cChore.getDate()).toString();
            cChoresDate.setText(date);

            //Setting the checkbox
            cCompletedImage.setVisibility(chores.isCompleted() ? View.VISIBLE : View.GONE);

        }

        @Override //Detecting clicks
        public void onClick(View v){
            //Linking ChoresViewsActivity to ChoresListFragment
            Intent intent = ChoresViewActivity.newIntent(getActivity(), cChore.getId());
            startActivity(intent);

        }

        //method to set up the textdrawable(Thumbnail) into the chores title
        public void setChoresTitle(String title){
            title = cChore.getTitle();
            cChoresTitle.setText(title);

            String letter = "A";

            //Setting up the thumbnail to always have the first letter of the title
            if(title != null && !title.isEmpty()) {
                letter = title.substring(0, 1);
            }

            int color = mColorGenerator.getRandomColor(); //random color for each Thumbnail
            mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);
            cThumbnail.setImageDrawable(mDrawableBuilder);
        }

    }

    //creating the lists using the RecyclerView
    private class ChoresAdapter extends RecyclerView.Adapter<ChoresListHolder> {

        private List<Chores> cChores;

        public ChoresAdapter(List<Chores> chores) {
            cChores = chores;
        }

        @Override
        public ChoresListHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return  new ChoresListHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(ChoresListHolder holder, int position){
            Chores chores = cChores.get(position);
            holder.bind(chores);
        }
        @Override
        public int getItemCount(){
            return cChores.size();
        }

        public void setChores(List<Chores> c){ //method to swap out crimes it display
            chores = c;
        }

    }

    private void update(){
        choreslist = ChoresList.get(getActivity());
        chores = choreslist.getChores();

        if(cAdapter == null){
            cAdapter = new ChoresAdapter(chores);
            cChoresList.setAdapter(cAdapter);
        }else{
            cAdapter.setChores(chores); //call the setChores method
            cAdapter.notifyDataSetChanged();
        }
    }
}
