package com.thesis.dont.loyaltypointuser.views;

import android.view.View;
import com.ToxicBakery.viewpager.transforms.ABaseTransformer;

public class SwitchPagerTransformer extends ABaseTransformer {
    private static final float MIN_SCALE = 0.75F;

    public SwitchPagerTransformer() {
    }

    protected void onTransform(View view, float position) {
        float height = (float)view.getHeight();
        float width = (float)view.getWidth();
        float scale = min(position < 0.0F?1.0F:Math.abs(1.0F - position), 0.5F);
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(width * 0.5F);
        view.setPivotY(height * 0.5F);
        view.setTranslationX(position < 0.0F?width * position:-width * position * 0.25F);
    }

    protected boolean isPagingEnabled() {
        return true;
    }
}

