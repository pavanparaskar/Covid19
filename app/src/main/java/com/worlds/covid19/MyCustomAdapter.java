package com.worlds.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<ContryModel> {
    private Context context;
    private List<ContryModel> countryModelsList;
    private List<ContryModel> countryModelsListFiltered;

    public MyCustomAdapter( Context context, List<ContryModel> countryModelsList) {
        super(context, R.layout.custom_country_layout,countryModelsList);

        this.context = context;
        this.countryModelsList = countryModelsList;
        this.countryModelsListFiltered = countryModelsList;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_country_layout,null,true);
        final TextView tvCountryName = view.findViewById(R.id.tv_CountryName);
        ImageView imageView = view.findViewById(R.id.ivFlag);

        tvCountryName.setText(countryModelsListFiltered.get(position).getCountry());
       /* tvCountryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Name"+countryModelsListFiltered.get(position).getCountry(), Toast.LENGTH_SHORT).show();
            }
        });*/
       Picasso.get().load(countryModelsListFiltered.get(position).getFlag()).into(imageView);
        return view;
    }

    @Override
    public int getCount() {
        return countryModelsListFiltered.size();
    }

    @Nullable
    @Override
    public ContryModel getItem(int position) {
        return countryModelsListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = countryModelsList.size();
                    filterResults.values = countryModelsList;

                }else{
                    List<ContryModel> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(ContryModel itemsModel:countryModelsList){
                        if(itemsModel.getCountry().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                countryModelsListFiltered = (List<ContryModel>) results.values;
                AffectedCountriesActivity.contryModelList = (List<ContryModel>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
