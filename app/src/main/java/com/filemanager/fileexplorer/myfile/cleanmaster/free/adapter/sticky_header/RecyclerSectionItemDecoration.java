package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.sticky_header;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.R;


public class RecyclerSectionItemDecoration extends RecyclerView.ItemDecoration {
    private TextView header;
    private final int headerOffset;
    private View headerView;
    private final SectionCallback sectionCallback;
    private final boolean sticky;


    public interface SectionCallback {
        CharSequence getSectionHeader(int i);

        boolean isSection(int i);
    }

    public RecyclerSectionItemDecoration(int i, boolean z, SectionCallback sectionCallback) {
        this.headerOffset = i;
        this.sticky = z;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        if (this.sectionCallback.isSection(recyclerView.getChildAdapterPosition(view))) {
            rect.top = this.headerOffset;
        }
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        if (this.headerView == null) {
            View inflateHeaderView = inflateHeaderView(recyclerView);
            this.headerView = inflateHeaderView;
            this.header = (TextView) inflateHeaderView.findViewById(R.id.list_item_section_text);
            fixLayoutSize(this.headerView, recyclerView);
        }
        CharSequence charSequence = "";
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View childAt = recyclerView.getChildAt(i);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt);
            CharSequence sectionHeader = this.sectionCallback.getSectionHeader(childAdapterPosition);
            this.header.setText(sectionHeader);
            if (!charSequence.equals(sectionHeader) || this.sectionCallback.isSection(childAdapterPosition)) {
                drawHeader(canvas, childAt, this.headerView);
                charSequence = sectionHeader;
            }
        }
    }

    private void drawHeader(Canvas canvas, View view, View view2) {
        canvas.save();
        if (this.sticky) {
            canvas.translate(0.0f, Math.max(0, view.getTop() - view2.getHeight()));
        } else {
            canvas.translate(0.0f, view.getTop() - view2.getHeight());
        }
        view2.draw(canvas);
        canvas.restore();
    }

    private View inflateHeaderView(RecyclerView recyclerView) {
        return LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.custom_item_header, (ViewGroup) recyclerView, false);
    }

    private void fixLayoutSize(View view, ViewGroup viewGroup) {
        view.measure(ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(viewGroup.getWidth(), View.MeasureSpec.EXACTLY), viewGroup.getPaddingLeft() + viewGroup.getPaddingRight(), view.getLayoutParams().width), ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(viewGroup.getHeight(), View.MeasureSpec.UNSPECIFIED), viewGroup.getPaddingTop() + viewGroup.getPaddingBottom(), view.getLayoutParams().height));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }
}
