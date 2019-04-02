package com.foraconnect.fora;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class ScaleView extends View {

    private int people, pBoys;
    private Context context;

    public ScaleView(Context context, int people, int pBoys) {
        super(context);

        this.context = context;
        this.people = people;
        this.pBoys = pBoys;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        if (width > height) {
            radius = height / 4;
        } else {
            radius = width / 4;
        }

        Path path = new Path();
        path.addCircle(width / 2,
                height / 2, radius,
                Path.Direction.CW);

        Paint paint1 = new Paint();
        paint1.setColor(Color.parseColor("#1E90FF"));
        paint1.setStrokeWidth(50);
        paint1.setStyle(Paint.Style.STROKE);

        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor("#FF69B4"));
        paint2.setStrokeWidth(50);
        paint2.setStyle(Paint.Style.STROKE);

        float center_x, center_y;
        final RectF oval1 = new RectF();
        final RectF oval2 = new RectF();

        center_x = width / 2;
        center_y = height / 2;
        int angleDif = (int)(pBoys*1.2);

        oval1.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval1, 210, angleDif, false, paint1);

        oval2.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval2, 210+angleDif, 120-angleDif, false, paint2);
    }
}
