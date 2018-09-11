package solulab.demo.recyclerview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import solulab.demo.recyclerview.R;
import solulab.demo.recyclerview.models.ImageModel;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder> {
    private final Context context;
    private List<ImageModel> list;
    private RequestOptions options;

    public ListAdapter(Context context, List<ImageModel> list) {
        this.context = context;
        this.list = list;

        options = new RequestOptions();
        options.centerCrop();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.error(R.mipmap.ic_placeholder);
        options.placeholder(R.mipmap.ic_placeholder);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list, parent, false);
        return new ListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        ImageModel item = list.get(position);
        holder.tvTags.setText(item.getTags().toUpperCase());
        holder.tvLikes.setText(String.valueOf(item.getLikes()));
        holder.tvFavorites.setText(String.valueOf(item.getFavorites()));
        holder.tvComments.setText(String.valueOf(item.getComments()));

        Glide.with(context)
                .load(item.getPreviewURL())
                .apply(options)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTags, tvLikes, tvFavorites, tvComments;
        private ImageView image;

        ListItemViewHolder(View view) {
            super(view);
            tvTags = view.findViewById(R.id.tvTags);
            tvLikes = view.findViewById(R.id.tvLikes);
            tvFavorites = view.findViewById(R.id.tvFavorites);
            tvComments = view.findViewById(R.id.tvComments);
            image = view.findViewById(R.id.image);
        }
    }
}