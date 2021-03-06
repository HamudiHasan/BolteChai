package com.aims.boltechai.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.ui.activity.MainActivity;
import com.aims.boltechai.ui.adapter.CategoryAdapter;
import com.aims.boltechai.util.AppUtils;
import com.aims.boltechai.util.DialogUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityListFragment extends Fragment implements CategoryAdapter.CategoryListener {

    private static final int REQUEST_CODE_RECORD = 1;
    public static final String PARENT_ID = "parentId";
    private List<Category> categoriesItems = new ArrayList<Category>();
    private View rootView;
    private ViewFlipper viewFlipper;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private CategoryAdapter adapter;
    private int parentId;
    private TextView tvWelcomeText;
    private int spanNumb = 2;

    public ActivityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (getArguments() != null) {
            parentId = (int) getArguments().getLong(PARENT_ID);
        }

        viewFlipper = (ViewFlipper) rootView.findViewById(R.id.view_flipper);
        tvWelcomeText = (TextView) rootView.findViewById(R.id.tv_welcome_text);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_main_category);
        spanNumb = getSpanNumb("BolteChaiImage");

        gridLayout = new GridLayoutManager(getActivity(), spanNumb);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CategoryAdapter(categoriesItems, getActivity(), this);
        recyclerView.setAdapter(adapter);

        addRecycleItems();
        if (parentId == 0) {
            getActivity().setTitle(R.string.app_name);
        } else {
            Category category = new Select().from(Category.class).where("categoryId = ?", parentId).executeSingle();
            getActivity().setTitle(category.categoryTitle.toUpperCase());
            tvWelcomeText.setText("Start adding items to " + category.categoryTitle.toUpperCase());
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Category Category = categoriesItems.get(position);
                if (!getRoleName().equals(AppUtils.MODE_CHILD)) {
                    DailogLongPress(Category.getId());
                }
            }
        }));
        return rootView;
    }

    private int getSpanNumb(String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getInt(name, 2);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ActivityListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ActivityListFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void addRecycleItems() {
        categoriesItems.clear();

        String lang;
        lang = getLanguage();

        // Read from Database
        List<Category> categories = new Select().from(Category.class).where("parentId = ? and language = ?", parentId, lang).execute();
        if (categories.size() > 0) {
            categoriesItems.clear();
            viewFlipper.setDisplayedChild(1);
            categoriesItems.addAll(categories);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCategoryClicked(long id) {
        ((MainActivity) getActivity()).moveToFragment(id);
    }

    public void onAddButtonClicked() {
        DialogUtils.showDialog(getActivity(), new DialogUtils.CategoryDialogListener() {

            @Override
            public void onUpdate(EditText editText, ImageView imageView, TextView textView) {

            }

            @Override
            public void onAudioButtonClicked() {

            }

            @Override
            public void onImageButtonClicked() {

            }

            @Override
            public void onSaveButtonClicked(Category categoryItem) {
                categoryItem.save();
                addRecycleItems();
            }
        }, parentId);
    }

    public void onUpdate(final long id) {
        DialogUtils.showDialog(getActivity(), new DialogUtils.CategoryDialogListener() {

            @Override
            public void onUpdate(EditText editText, ImageView imageView, TextView textView) {
                List<Category> categories = new Select().from(Category.class).where("categoryId = ?", id).execute();
                editText.setText(categories.get(0).categoryTitle);
                if (categories.get(0).categoryImage != null) {
                    File f = new File(categories.get(0).categoryImage.toString());
                    Picasso.with(getContext()).load(f).into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }

                if (categories.get(0).categoryAudio != null) {
                    textView.setText(categories.get(0).categoryAudio + "");
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAudioButtonClicked() {

            }

            @Override
            public void onImageButtonClicked() {

            }

            @Override
            public void onSaveButtonClicked(Category categoryItem) {

                Category category = Category.load(Category.class, id);

                if (categoryItem.categoryTitle != null)
                    category.categoryTitle = categoryItem.categoryTitle;

                if (categoryItem.categoryImage != null)
                    category.categoryImage = categoryItem.categoryImage;

                if (categoryItem.categoryAudio != null)
                    category.categoryAudio = categoryItem.categoryAudio;

                category.save();

                categoriesItems.clear();
                addRecycleItems();
            }


        }, parentId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_RECORD:
                    data.getDataString();
                    Toast.makeText(getActivity(), data.getDataString().toString(), Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    public void DailogLongPress(final long id) {
        final CharSequence[] items = {"Edit", "Delete Item", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Edit")) {
                    onUpdate(id);
                    Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("Delete Item")) {
                    new Delete().from(Category.class).where("categoryId = ?", id).execute();
                    categoriesItems.clear();
                    addRecycleItems();
                    adapter.notifyDataSetChanged();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getLanguage() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ln = preferences.getString(AppUtils.BOLTE_CHAI_LANGUAGE, "en");
        if (ln == null) {
            ln = "en";
        }
        return ln;
    }

    public String getRoleName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString(AppUtils.BOLTE_CHAI_ROLE, AppUtils.MODE_PARENT);
    }
}
