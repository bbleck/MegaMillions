package deepdive.cnm.edu.megamillions.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import deepdive.cnm.edu.megamillions.R;
import deepdive.cnm.edu.megamillions.model.db.PickDatabase;
import deepdive.cnm.edu.megamillions.model.entity.Pick;
import deepdive.cnm.edu.megamillions.model.entity.PickNumber;
import deepdive.cnm.edu.megamillions.model.pojo.PickAndNumbers;
import deepdive.cnm.edu.megamillions.view.PickAdapter;
import edu.cnm.deepdive.Generator;
import edu.cnm.deepdive.MMGenerator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Generator generator;
  private RecyclerView pickListView;
  private PickAdapter adapter;
  private List<int[]> picks;
  private Random rng;
  private PickDatabase database;
//  private LinearLayoutManager manager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    pickListView = findViewById(R.id.pick_list_view);
    picks = new ArrayList<>();
    adapter = new PickAdapter(this, picks);
    pickListView.setAdapter(adapter);
    rng = new SecureRandom();
    generator = new MMGenerator(rng);
//    manager = new LinearLayoutManager(this);
//    pickListView.setLayoutManager(manager);
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_pick);
    fab.setOnClickListener((view) -> {
      int[] pick = generator.generate();
      new AddTask().execute(pick);
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    boolean handled = true;
    switch(item.getItemId()){
      case R.id.action_clear:
        picks.clear();
        adapter.notifyDataSetChanged();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  protected void onStart() {
    super.onStart();
    database = PickDatabase.getInstance(this);
    new QueryTask().execute();
  }

  @Override
  protected void onStop() {
    database = null;
    PickDatabase.forgetInstance();
    super.onStop();
  }

  private class QueryTask extends AsyncTask<Void, Void, List<PickAndNumbers>>{

    @Override
    protected List<PickAndNumbers> doInBackground(Void... voids) {
      return database.getPickDao().selectWithNumbers();
    }

    @Override
    protected void onPostExecute(List<PickAndNumbers> pickAndNumbers) {
      //FIXME Assume less with data model.
      picks.clear();
      for (PickAndNumbers pick :
          pickAndNumbers) {
        int[] numbers = new int[pick.getNumbers().size()];
        int index = 0;
        for (PickNumber number :
            pick.getNumbers()) {
          numbers[index++] = number.getValue();
        }
        picks.add(numbers);
      }
      adapter.notifyDataSetChanged();
    }
  }

  private class AddTask extends AsyncTask<int[], Void, int[]>{

    @Override
    protected int[] doInBackground(int[]... ints) {
      int[] numbers = ints[0];
      Pick pick = new Pick();
      long pickId = database.getPickDao().insert(pick);
      List<PickNumber> pickNumbers = new LinkedList<>();
      for (int i = 0; i<numbers.length; i++) {
        PickNumber pickNumber = new PickNumber();
        pickNumber.setPickId(pickId);
        pickNumber.setValue(numbers[i]);
        pickNumber.setPool((i == numbers.length - 1) ? 1 : 0);
        pickNumbers.add(pickNumber);
      }
      database.getPickNumberDao().insert(pickNumbers);
      return numbers;
    }

    @Override
    protected void onPostExecute(int[] numbers ) {
      picks.add(numbers);
      adapter.notifyItemInserted(picks.size()-1);
    }
  }

}
