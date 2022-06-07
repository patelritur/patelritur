package com.demo.binding;

import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.demo.R;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BindingAdapter {

    @androidx.databinding.BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }





    @androidx.databinding.BindingAdapter("app:homeOptionsImage")
    public static void loadImage(AppCompatImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }
    @androidx.databinding.BindingAdapter("app:detailImage")
    public static void loadDetailImage(AppCompatImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().override(600, 300).transform(new RoundedCorners(20)))
                .into(view);
    }


    @androidx.databinding.BindingAdapter("app:roundProfileOptionsImage")
    public static void loadProfileRoundedImage(AppCompatImageView view, String imageUrl) {
        if(imageUrl==null)
            return;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_default);
        requestOptions.circleCrop();
        if(imageUrl.contains("http")){
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(view);
        }
        else{
            File file = new File(imageUrl);
            Uri imageUri = Uri.fromFile(file);
            Glide.with(view.getContext())
                    .load(imageUri)
                    .apply(requestOptions)
                    .into(view);
        }




    }

    @androidx.databinding.BindingAdapter("app:roundHomeOptionsImage")
    public static void loadRoundedImage(AppCompatImageView view, String imageUrl) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_default);
        requestOptions.circleCrop();
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(requestOptions)
                .into(view);




    }

    @androidx.databinding.BindingAdapter("app:carRating")
    public static void carRating(RatingBar view, String rating) {
        if(rating!=null)
      view.setRating(Float.parseFloat(rating));
    }


    @androidx.databinding.BindingAdapter("app:addrating")
    public static void addRating(RatingBar view, String rating) {
        if(rating!=null)
            view.setRating(Float.parseFloat(rating));
    }


    @androidx.databinding.BindingAdapter({"android:htmlText"})
    public static void setHtmlTextValue(AppCompatTextView textView, String htmlText) {
        if (htmlText == null)
            return;

        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(htmlText);
        }
        textView.setText(result);
    }

    //26 Oct, 2021
    @androidx.databinding.BindingAdapter({"app:bindNewsTextTime"})
    public static void setNewsTimetext(AppCompatTextView textView, String htmlText) {
        String msg = null;
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
            Date past = format.parse(htmlText);
            PrintLog.v(""+past);
            Date now =  Calendar.getInstance().getTime();
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            msg = days+" days ago";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textView.setText(msg);
    }
    @androidx.databinding.BindingAdapter({"app:bindTextTime"})
    public static void setTimetext(AppCompatTextView textView, String htmlText) {
        String msg = null;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
            Date past = format.parse(htmlText);
            Date now =  Calendar.getInstance().getTime();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(seconds<60)
            {
                msg = seconds+" seconds ago";
            }
            else if(minutes<60)
            {
                msg = minutes+" minutes ago";
            }
            else if(hours<24)
            {
                msg = hours+" hours ago";
            }
            else
            {
                msg = days+" days ago";
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        textView.setText(msg);
    }



}
