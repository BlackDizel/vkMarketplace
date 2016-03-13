package org.byters.vkmarketplace.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import org.byters.vkmarketplace.R;
import org.byters.vkmarketplace.controllers.ControllerMain;
import org.byters.vkmarketplace.model.dataclasses.MarketplaceItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class SharingHelper {
    public static void shareItem(Context context, int itemId) {
        new Task(context, itemId).execute();
    }

    static class Task extends AsyncTask<Void, Void, Void> {

        private Context context;
        private String imgPath;
        private int itemId;

        public Task(Context context, int itemId) {
            this.context = context;
            this.itemId = itemId;
            MarketplaceItem item = ((ControllerMain) context.getApplicationContext()).getControllerItems().getModel().getItemById(itemId);
            if (item == null)
                this.cancel(true);
            if (item.getPhotos() == null || item.getPhotos().size() == 0)
                this.cancel(true);
            imgPath = item.getPhotos().get(0).getSrc_big(context);
            if (TextUtils.isEmpty(imgPath))
                this.cancel(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                InputStream input = new java.net.URL(Uri.parse(imgPath).toString()).openStream();
                // Decode Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                if (bitmap != null) {
                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/image.png";

                    File file = new File(file_path);
                    FileOutputStream fOut = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (context != null) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");

                File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/image.png");
                if (f.exists())
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));

                MarketplaceItem item = ((ControllerMain) context.getApplicationContext()).getControllerItems().getModel().getItemById(itemId);
                if (item == null) return;

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, item.getDescription());
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, item.getTitle());
                if (sharingIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.action_share_title)));
                } else {
                    Toast.makeText(context, R.string.action_share_error, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
