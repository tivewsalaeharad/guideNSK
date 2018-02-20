package com.hand.guidensk.cluster;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import com.hand.guidensk.R;

public class PlaceRenderer extends DefaultClusterRenderer<PlaceItem> {

    private Context context;

    public PlaceRenderer(Context context, GoogleMap map, ClusterManager<PlaceItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(PlaceItem item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getMarker().getIcon());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<PlaceItem> cluster, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapMarker(context,
                R.drawable.marker_cluster, String.valueOf(cluster.getSize()))));
    }

    private Bitmap getBitmapMarker(Context mContext, int resourceId,  String mText)
    {
        try
        {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            if(bitmapConfig == null) bitmapConfig = Bitmap.Config.ARGB_8888;
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(0xFF008000);
            paint.setTextSize((int) (14 * scale));
            paint.setShadowLayer(2f, 0f, 2f, 0xFF004000);
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width())/2;
            int y = (bitmap.getHeight() + bounds.height())/2;
            canvas.drawText(mText, x, y, paint);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
