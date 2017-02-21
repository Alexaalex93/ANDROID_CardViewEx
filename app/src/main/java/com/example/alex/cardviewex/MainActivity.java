package com.example.alex.cardviewex;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter albumsAdapter;
    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        albumList = new ArrayList<>();
        albumsAdapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(albumsAdapter);
        prepareAlbums();

        try{
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initCollapsingToolbar(){ //Para que cuando se mueva con el dedo se oculte
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
    private void prepareAlbums(){
        int[] covers = new int[]{
                R.drawable.album1,
        R.drawable.album2,
        R.drawable.album3,
        R.drawable.album4,
        R.drawable.album5,
        R.drawable.album6,
        R.drawable.album7,
        R.drawable.album8,
        R.drawable.album9,
        R.drawable.album10,
        R.drawable.album11
        };
        Album album = new Album("True Romance", 13, covers[0]);
        albumList.add(album);
        Album album2 = new Album("XSpace", 11, covers[1]);
        albumList.add(album);
        Album album3 = new Album("Hola", 5, covers[2]);
        albumList.add(album);
        albumsAdapter.notifyDataSetChanged();

    }

    public  class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int position = parent.getChildAdapterPosition(view);
            int colum = position % spanCount;
            if(includeEdge){
                outRect.left = spacing - colum * spacing / spanCount;
                outRect.right = (colum + 1) * spacing / spanCount;

                if(position < spanCount){
                    outRect.top = spacing;

                }
                outRect.bottom = spacing;
            } else {
                outRect.left = colum * spacing / spanCount;
                outRect.right = spacing - (colum + 1) * spacing / spanCount;

                if(position >= spanCount){
                    outRect.top = spacing;
                }
            }
        }
        private int dpToPx(int dp){
            Resources r = getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }
    }
}


