package com.bradleege.batterydroid;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import com.bradleege.batterydroid.data.CardData;
import java.util.ArrayList;
import java.util.List;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.CardDataViewHolder> {

    private static final String TAG = "CardRecyclerViewAdapter";

    private List<CardData> cardData = new ArrayList<>();

    public CardRecyclerViewAdapter(List<CardData> cardData) {
        super();
        this.cardData.clear();
        this.cardData.addAll(cardData);
    }

    public void updateData(List<CardData> newData) {
        Log.i(TAG, "updateData() called");
        cardData.clear();
        cardData.addAll(newData);
        notifyDataSetChanged();
        Log.i(TAG, "updateData() finished");
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public CardDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_battery_feature_card, parent, false);
        CardDataViewHolder cdvh = new CardDataViewHolder(v);
        return cdvh;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CardDataViewHolder holder, int position) {
        holder.feature.setText(cardData.get(position).getFeature());
        holder.status.setText(cardData.get(position).getStatus());
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return cardData.size();
    }

    public static class CardDataViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView feature;
        TextView status;

        public CardDataViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv);
            feature = (TextView)itemView.findViewById(R.id.feature);
            status = (TextView)itemView.findViewById(R.id.status);
        }
    }
}
