package com.example.animecatalog.ui.catalog.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.animecatalog.R;
import com.example.animecatalog.databinding.ItemAnimeBinding;
import com.example.animecatalog.data.local.entity.AnimeEntity;
import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {
    private List<AnimeEntity> animeList = new ArrayList<>();
    private OnAnimeClickListener listener;

    public interface OnAnimeClickListener {
        void onAnimeClick(AnimeEntity anime);
    }

    public AnimeAdapter(OnAnimeClickListener listener) {
        this.listener = listener;
    }

    public void setAnimeList(List<AnimeEntity> animeList) {
        this.animeList = animeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAnimeBinding binding = ItemAnimeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AnimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        holder.bind(animeList.get(position));
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    class AnimeViewHolder extends RecyclerView.ViewHolder {
        private ItemAnimeBinding binding;

        public AnimeViewHolder(ItemAnimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AnimeEntity anime) {
            binding.tvTitle.setText(anime.getTitle());
            binding.tvType.setText(anime.getType());
            binding.tvRating.setText("★ " + anime.getRating());
            binding.tvEpisodes.setText(anime.getEpisodes() + " eps");

            Glide.with(binding.getRoot().getContext())
                    .load(anime.getImageUrl())
                    .placeholder(R.drawable.placeholder_anime)
                    .into(binding.ivPoster);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAnimeClick(anime);
                }
            });
        }
    }
}
