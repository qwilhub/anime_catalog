package com.example.animecatalog.ui.favorites.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.animecatalog.R;
import com.example.animecatalog.databinding.ItemFavoriteBinding;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<AnimeEntity> favoritesList = new ArrayList<>();
    private OnFavoriteActionListener listener;

    public interface OnFavoriteActionListener {
        void onFavoriteClick(AnimeEntity anime);
        void onRemoveClick(AnimeEntity anime);
        void onEditClick(AnimeEntity anime);
    }

    public FavoritesAdapter(OnFavoriteActionListener listener) {
        this.listener = listener;
    }

    public void setFavoritesList(List<AnimeEntity> favoritesList) {
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding binding = ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(favoritesList.get(position));
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ItemFavoriteBinding binding;

        public FavoriteViewHolder(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AnimeEntity anime) {
            binding.tvTitle.setText(anime.getTitle());
            binding.tvType.setText(anime.getType() + " • " + anime.getEpisodes() + " eps");
            binding.tvRating.setText("★ " + anime.getRating());
            binding.ratingBar.setRating((float) anime.getUserRating());
            binding.tvComment.setText(anime.getComment());

            Glide.with(binding.getRoot().getContext())
                    .load(anime.getImageUrl())
                    .placeholder(R.drawable.placeholder_anime)
                    .into(binding.ivPoster);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavoriteClick(anime);
                }
            });

            binding.btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveClick(anime);
                }
            });

            binding.btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(anime);
                }
            });
        }
    }
}