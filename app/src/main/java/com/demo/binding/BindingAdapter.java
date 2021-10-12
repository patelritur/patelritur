package com.demo.binding;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
                .apply(new RequestOptions().override(600, 300))
                .into(view);
    }


    @androidx.databinding.BindingAdapter("app:roundHomeOptionsImage")
    public static void loadRoundedImage(AppCompatImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().circleCrop())
                .into(view);
    }

    @androidx.databinding.BindingAdapter("app:carRating")
    public static void carRating(RatingBar view, String rating) {
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
}
