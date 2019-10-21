package com.example.powerlogger.ui.nutrition;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.powerlogger.DataFetch;
import com.example.powerlogger.R;
import com.example.powerlogger.ui.nutrition.NutritionViewModel;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.id.text1;

public class NutritionFragment extends Fragment {

    private NutritionViewModel nutritionViewModel;
    private ListView nutritionListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nutritionViewModel =
                ViewModelProviders.of(this).get(NutritionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nutrition, container, false);

        ListView listView = root.findViewById(R.id.nutritionListView);

        List<String> foods = new ArrayList<>(
                Arrays.asList("Burger King", "Pizza", "Olive oil", "Avocado oil", "Keto chocolatte", "Potato")
        );
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, foods);
        listView.setAdapter(arrayAdapter);

        try {
            DataFetch d = new DataFetch("http://www.mocky.io/v2/5d8c53142e00003005abd77a");
            String jsonString = d.execute().get();
            System.out.print("************************ Json result:");
            System.out.print(jsonString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return root;
    }
}