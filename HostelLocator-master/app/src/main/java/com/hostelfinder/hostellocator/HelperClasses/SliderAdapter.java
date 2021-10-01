package com.hostelfinder.hostellocator.HelperClasses;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.hostelfinder.hostellocator.R;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context)
    {
        this.context = context;
    }

    int images[] = {
            R.drawable.welcomenewpng,
            R.drawable.search_hostel_image_two,
            R.drawable.chat_image_two,
            R.drawable.addlocationpng
    };
    int headings[] = {
            R.string.welcometohostellocator,
            R.string.searchhostelstitle,
            R.string.chatfeature,
            R.string.addyourHosteltitle
    };
    int descriptions[] = {
            R.string.welcometohostelloctordescription,
            R.string.searchforhosteldesc,
            R.string.chatfeaturedesc,
            R.string.addyourhosteldesc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);
        //Hooks
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.slider_heading);
        TextView desc = view.findViewById(R.id.slider_description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}

