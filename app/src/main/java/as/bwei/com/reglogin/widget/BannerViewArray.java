package as.bwei.com.reglogin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import as.bwei.com.reglogin.R;

/**
 * Created by HP on 2018/10/13.
 */

public class BannerViewArray extends LinearLayout{
    private boolean startAndclose;//是否开启轮播
    private int bannerTime;//轮播时间
    private List<String> imageList = new ArrayList<>();
    private ViewPager viewPager;
    private final int STARTBANNER=100;//开启
    private LinearLayout mPoint;
    private Context context;
    private MyAdapter myAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STARTBANNER:
                    int item=viewPager.getCurrentItem();
                    item++;
                    viewPager.setCurrentItem(item);
                    handler.sendEmptyMessageDelayed(STARTBANNER,bannerTime);
                    break;
            }
        }
    };

    public BannerViewArray(Context context) {
        super(context);
        init(context);
    }

    public BannerViewArray(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取TypedArray
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.BannerViewArray);
        //启动轮播或者关闭轮播
        startAndclose = ty.getBoolean(R.styleable.BannerViewArray_startAndclose,false);
        //获取时间值
        bannerTime = ty.getInteger(R.styleable.BannerViewArray_startTime,1000);
        //释放
        ty.recycle();
        init(context);
    }
    //初始化
    private void init(Context context) {
        this.context=context;
        View view=View.inflate(context,R.layout.view_banner,null);
        viewPager=(ViewPager) view.findViewById(R.id.viewpager);
        mPoint=(LinearLayout)view.findViewById(R.id.layout_point);
        myAdapter=new MyAdapter();
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPoint(position%imageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(view);
    }

    public void setImageList(List<String> imageList){
        this.imageList=imageList;
        myAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(Integer.MAX_VALUE/2-1);
        setPoint(0);//添加指示点

        if (startAndclose){//开启
            handler.sendEmptyMessage(STARTBANNER);
        }else {//关闭
            handler.removeMessages(STARTBANNER);
        }
    }

    private void setPoint(int position) {
        mPoint.removeAllViews();
        for(int a=0;a<imageList.size();a++){
            ImageView imageView=new ImageView(context);
            if(position==a){
                imageView.setBackgroundResource(R.drawable.point_yes);
            }else{
                imageView.setBackgroundResource(R.drawable.point_no);
            }
            mPoint.addView(imageView);
            LinearLayout.LayoutParams params= (LayoutParams) imageView.getLayoutParams();
            params.width=20;
            params.height=20;
            imageView.setLayoutParams(params);
        }
    }

    //适配器
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view=View.inflate(context,R.layout.view_image,null);
            ImageView imageView=(ImageView) view.findViewById(R.id.image);
            String url=imageList.get(position%imageList.size());
            Picasso.with(context).load(url).fit().into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
