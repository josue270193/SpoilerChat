package com.app.spoilerchat.tools;
/*package com.app.spoilerchat.tools;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;

public class AsynTaskImagen extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference imageViewReference;
    private LruCache<Integer, Bitmap> mMemoryCache;
    
    public AsynTaskImagen(ImageView imageView) {
        imageViewReference = new WeakReference(imageView);
        

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory;
		mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(Integer key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
    }
    

    @Override
    protected Bitmap doInBackground(String... params) {
    	
    	Bitmap bitmap = getBitmapFromMemCache(params[0]);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(mContext.getResources(), params[0]);
			addBitmapToMemoryCache(params[0], bitmap);
		}
        return mFakeImageLoader.getImage();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (v.position == position) {
            // If this item hasn't been recycled already, hide the
            // progress and set and show the image
            v.progress.setVisibility(View.GONE);
            v.icon.setVisibility(View.VISIBLE);
            v.icon.setImageBitmap(result);
        }
    }
    
    private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(int key) {
		return mMemoryCache.get(key);
	}
}
*/