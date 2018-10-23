package deepdive.cnm.edu.megamillions.view;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import deepdive.cnm.edu.megamillions.R;
import java.util.List;

public class PickAdapter extends RecyclerView.Adapter<PickAdapter.Holder>{

  private List<int[]> picks;
  private Context mContext;


  //TODO modify to take List<PickWithNumber>
  public PickAdapter(Context context, List<int[]> picks){
    this.picks = picks;
    this.mContext = context;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.pick_item, viewGroup, false);

    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind();//todo: pass current pickwithnumbers instance
    if(position%2 == 1){
      holder.itemView.setBackgroundColor(Color.argb(16, 0, 0, 0));
    }
  }



  @Override
  public int getItemCount() {
    return picks.size();
  }

  public class Holder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

    private TextView[] numbers;
    private static final int PICK_LENGTH = 6;


    public Holder(@NonNull View itemView) {
      super(itemView);
      itemView.setOnCreateContextMenuListener(this);
      numbers = new TextView[PICK_LENGTH];
      for (int i = 0; i < PICK_LENGTH; i++) {
        int id = mContext.getResources().getIdentifier("num_"+i, "id", mContext.getPackageName());
        numbers[i] = itemView.findViewById(id);
      }
    }

    private void bind(){
      //todo:use pickwithnumbers instance
      int[] numbers = picks.get(getAdapterPosition());
      for (int i = 0; i < numbers.length; i++) {
        this.numbers[i].setText(String.format("%02d", numbers[i]));
      }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      menu.add(R.string.delete).setOnMenuItemClickListener((item) -> {
        picks.remove(getAdapterPosition());
        notifyItemRemoved(getAdapterPosition());
        return true;
      });
    }
  }

  //todo: create deletetask that takes a pickwithnumbers instance
}
