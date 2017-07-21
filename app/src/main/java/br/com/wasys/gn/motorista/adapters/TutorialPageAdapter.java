package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.com.wasys.gn.motorista.R;

/**
 * Created by fernandamoncores on 3/24/16.
 */
public class TutorialPageAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources = {
            R.drawable.tutorial1,
            R.drawable.tutorial2,
            R.drawable.tutorial3,
            R.drawable.tutorial4

    };

    public TutorialPageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);


        try {

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mResources[position]);
           // imageView.setImageResource(mResources[position]);
            imageView.setImageBitmap(bitmap);
        }catch (Exception e)
        {
            imageView.setImageResource(mResources[0]);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
