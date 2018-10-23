package deepdive.cnm.edu.megamillions.controller;

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
import deepdive.cnm.edu.megamillions.view.PickAdapter;
import edu.cnm.deepdive.Generator;
import edu.cnm.deepdive.MMGenerator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private Generator generator;
  private RecyclerView pickListView;
  private PickAdapter adapter;
  private List<int[]> picks;
  private Random rng;
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
      picks.add(pick);
      adapter.notifyItemInserted(picks.size()-1);
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
}
