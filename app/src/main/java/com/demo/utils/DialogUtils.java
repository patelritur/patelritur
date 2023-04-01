package com.demo.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


/**
 * to show dolaig or toast throught the app this class method is called
 */

public class DialogUtils {

    /**
     * The M progress dialog.
     */
    private static ProgressDialog mProgressDialog;


    /**
     * Shows a long time duration toast message.
     *
     * @param ctx the ctx
     * @param msg Message to be show in the toast.
     */
    public static void showToast(Context ctx, CharSequence msg) {
        if (ctx instanceof AppCompatActivity && !((AppCompatActivity) ctx).isFinishing())
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }




    /**
     * Show alert info.
     *
     * @param context the context
     * @param message the message
     */
    public static void showAlertInfo(Context context, String message) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, id) -> {
                        if(!(((Activity)context).isFinishing()||((Activity)context).isDestroyed()))
                            dialog.dismiss();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Shows an alert dialog with the OK button. When the user presses OK button, the dialog
     * dismisses.
     *
     * @param context the context
     * @param title   the title
     * @param body    the body
     */
    public static void showAlertDialog(Context context, String title, String body) {
        try {
            showAlertDialog(context, title, body, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*-------------------------------------Progress Dialog-----------------------------------------*/

    /**
     * Shows an alert dialog with OK button
     *
     * @param context    the context
     * @param title      the title
     * @param body       the body
     * @param okListener the ok listener
     */
    public static void showAlertDialog(Context context, String title, String body, DialogInterface.OnClickListener okListener) {
        if (okListener == null) {

            okListener = (dialog, which) -> {
                if (!((Activity) context).isFinishing())
                    dialog.cancel();
            };

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context,0).setMessage(body).setCancelable(false).setPositiveButton("Ok", okListener);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.show();
    }

    /**
     * Shows a progress dialog with a spinning animation in it. This method must preferably called
     * from a UI thread.
     *
     * @param ctx           Activity context
     * @param title         Title of the progress dialog
     * @param body          Body/Message to be shown in the progress dialog
     * @param isCancellable True if the dialog can be cancelled on back button press, false otherwise
     */
    public static void showProgressDialog(Context ctx, String title, String body, boolean isCancellable) {
        if (ctx instanceof AppCompatActivity && !((AppCompatActivity) ctx).isFinishing()) {
            showProgressDialog(ctx, title, body, null, isCancellable);
        }
    }

    /**
     * Shows a progress dialog with a spinning animation in it. This method must preferably called
     * from a UI thread.
     *
     * @param ctx           Activity context
     * @param title         Title of the progress dialog
     * @param body          Body/Message to be shown in the progress dialog
     * @param icon          Icon to show in the progress dialog. It can be null.
     * @param isCancellable True if the dialog can be cancelled on back button press, false otherwise
     */
    private static void showProgressDialog(Context ctx, String title, String body, Drawable icon, boolean isCancellable) {
        if ((ctx instanceof AppCompatActivity && !((AppCompatActivity) ctx).isFinishing())) {
            mProgressDialog = ProgressDialog.show(ctx, title, body, true);
            mProgressDialog.setIcon(icon);
            mProgressDialog.setCancelable(isCancellable);
        }
    }

    /**
     * Check if the {@link ProgressDialog} is visible in the UI.
     *
     * @return the boolean
     */
    public static boolean isProgressDialogVisible() {
        return (mProgressDialog != null);
    }

    /**
     * Dismiss the progress dialog if it is visible.
     */
    public static void dismissProgressDialog() {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = null;
    }


    /**
     * Creates comman confirmation dialog with Yes-No Button. By default the buttons just dismiss the
     * dialog.
     *
     * @param ctx         the ctx
     * @param message     Message to be shown in the dialog.
     * @param yesListener Yes click handler
     * @param noListener  the no listener
     * @param yesLabel    Label for yes button
     * @param noLabel     Label for no button
     */
    public static void showConfirmDialog(Context ctx, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String yesLabel, String noLabel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        if (yesListener == null) {
            yesListener = (dialog, which) -> dialog.dismiss();
        }
        if (noListener == null) {
            noListener = (dialog, which) -> dialog.dismiss();
        }
        builder.setMessage(message).setPositiveButton(yesLabel, yesListener).setNegativeButton(noLabel, noListener).setCancelable(false).show();
    }









}