package com.ximsfei.skin.sample.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ximsfei.skin.sample.R;
import com.ximsfei.skin.sample.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;
import skin.support.content.res.ColorState;
import skin.support.content.res.SkinCompatUserColorManager;
import skin.support.utils.Slog;

public class ColorPickerActivity extends AppCompatActivity {
    private static final String TAG = "ColorPickerActivity";
    private Toolbar mToolbar;
    private ColorPickerAdapter mAdapter;
    private boolean hasChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Color Picker");
        mToolbar.setSubtitle("Define your exclusive application.");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ColorPickerAdapter();
        mAdapter.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPickerData data) {
                hasChanged = true;
                String color = data.getColor();
                SkinCompatUserColorManager.get().addColorState(data.colorRes, color);
                // 如果用户已经修改了colorAccent 和 colorDefault，那么把navigation也修改掉。
                if (SkinCompatUserColorManager.get().getColorState(R.color.colorAccent) != null
                        && SkinCompatUserColorManager.get().getColorState(R.color.colorDefault) != null) {
                    SkinCompatUserColorManager.get().addColorState(
                            R.color.navigation_item_tint, new SkinCompatUserColorManager.ColorBuilder()
                                    .setColorSelected(ColorPickerActivity.this, R.color.colorAccent)
                                    .setColorPressed(ColorPickerActivity.this, R.color.colorAccent)
                                    .setColorChecked(ColorPickerActivity.this, R.color.colorAccent)
                                    .setColorDefault(ColorPickerActivity.this, R.color.colorDefault)
                                    .build());
                } else if (SkinCompatUserColorManager.get().getColorState(R.color.navigation_item_tint) != null) {
                    // 如果navigation_item_tint 依赖的颜色用户未设置，则删除。
                    SkinCompatUserColorManager.get().removeColorState(R.color.navigation_item_tint);
                }
                switch (data.colorRes) {
                    case R.color.colorPrimary:
                        break;
                    case R.color.colorPrimaryDark:
                        break;
                    case R.color.colorAccent:
                        break;
                    case R.color.colorDefault:
                        break;
                    case R.color.titleTextColor:
                        break;
                    case R.color.subtitleTextColor:
                        break;
                    case R.color.textColorPrimary:
                        break;
                    case R.color.textColorPrimaryPressed:
                        break;
                    case R.color.textColorSecondary:
                        break;
                    case R.color.textColorSecondaryPressed:
                        break;
                    case R.color.textColorTip:
                        break;
                    case R.color.textColorTipPressed:
                        break;
                    case R.color.textBackground:
                        break;
                    case R.color.background:
                        break;
                    case R.color.divider:
                        break;
                    default:
                        break;
                }
                SkinCompatManager.getInstance().previewSkinInCurActivity(ColorPickerActivity.this);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ColorPickerActivity.this));
        Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasChanged = false;
                SkinCompatUserColorManager.get().clearColors();
                mAdapter.setItems(prepareData());
            }
        });
        Button apply = findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasChanged = false;
                SkinCompatUserColorManager.get().applyColors();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setItems(prepareData());
    }

    @Override
    public void onBackPressed() {
        if (hasChanged) {
            // TODO 弹框提示是否保存修改.
            Slog.i(TAG, "自定义颜色值未保存！");
        }
        super.onBackPressed();
    }

    private List<ColorPickerData> prepareData() {
        List<ColorPickerData> dataList = new ArrayList<>();
        dataList.add(generateData(R.color.colorPrimary));
        dataList.add(generateData(R.color.colorPrimaryDark));
        dataList.add(generateData(R.color.colorAccent));
        dataList.add(generateData(R.color.colorDefault));
        dataList.add(generateData(R.color.titleTextColor));
        dataList.add(generateData(R.color.subtitleTextColor));
        dataList.add(generateData(R.color.textColorPrimary));
        dataList.add(generateData(R.color.textColorPrimaryPressed));
        dataList.add(generateData(R.color.textColorSecondary));
        dataList.add(generateData(R.color.textColorSecondaryPressed));
        dataList.add(generateData(R.color.textColorTip));
        dataList.add(generateData(R.color.textColorTipPressed));
        dataList.add(generateData(R.color.textBackground));
        dataList.add(generateData(R.color.background));
        dataList.add(generateData(R.color.divider));
        return dataList;
    }

    private ColorPickerData generateData(@ColorRes int colorRes) {
        ColorState state = SkinCompatUserColorManager.get().getColorState(colorRes);
        ColorPickerData data = new ColorPickerData();
        data.colorRes = colorRes;
        if (state == null) {
            data.name = getResources().getResourceEntryName(colorRes);
        } else {
            data.name = state.getColorName();
            String colorDefault = state.getColorDefault();
            if (!TextUtils.isEmpty(colorDefault)) {
                if (colorDefault.length() == 7) {
                    data.setValue(colorDefault.substring(1));
                } else if (colorDefault.length() == 9) {
                    data.setValue(colorDefault.substring(3));
                    data.setAlpha(colorDefault.substring(1, 3));
                }
            }
        }
        return data;
    }

    interface OnColorChangedListener {
        void onColorChanged(ColorPickerData data);
    }

    private class ColorPickerAdapter extends BaseRecyclerAdapter<ColorPickerData, ColorPickerViewHolder> {
        private OnColorChangedListener mOnColorChangedListener;

        public void setOnColorChangedListener(OnColorChangedListener listener) {
            mOnColorChangedListener = listener;
        }

        @NonNull
        @Override
        public ColorPickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.item_color_picker_layout, parent, false);
            return new ColorPickerViewHolder(itemView, this);
        }

        @Override
        public void onBindViewHolder(ColorPickerViewHolder holder, int position) {
            ColorPickerData item = getItem(position);
            int color = 0;
            try {
                color = Integer.valueOf(item.getValue(), 16);
            } catch (Exception e) {

            }
            int alpha = 0xff;
            try {
                alpha = Integer.valueOf(item.getAlpha(), 16);
            } catch (Exception e) {

            }
            holder.mColorName.setText(item.name);
            holder.mColorSeekBar.setProgress(color);
            holder.mAlphaSeekBar.setProgress(alpha);
            holder.mColorPreview.setText(item.getColor());
            holder.mColorPreview.setTextColor(Color.parseColor(item.getColor()));
            holder.mColorPreview.setBackgroundResource(item.colorRes);
        }

        private void onColorChanged(ColorPickerViewHolder viewHolder) {
            mOnColorChangedListener.onColorChanged(getItem(viewHolder.getAdapterPosition()));
        }
    }

    private static class ColorPickerData {
        private String name = "";
        private String value = "000000";
        private String alpha = "ff";
        private int colorRes;

        public void setAlpha(String alpha) {
            if (alpha.length() == 2) {
                this.alpha = alpha;
            } else if (alpha.length() == 1) {
                this.alpha = "0" + alpha;
            } else {
                this.alpha = "ff";
            }
        }

        public String getAlpha() {
            return alpha;
        }

        public void setValue(String value) {
            if (value.length() == 6) {
                this.value = value;
            } else if (value.length() < 6) {
                int valueLen = value.length();
                for (int i = 0; i < 6 - valueLen; i++) {
                    value = "0" + value;
                }
                this.value = value;
            } else {
                this.value = "000000";
            }
        }

        public String getValue() {
            return value;
        }

        public String getColor() {
            return "#" + alpha + value;
        }
    }

    private static class ColorPickerViewHolder extends BaseRecyclerAdapter.BaseViewHolder<ColorPickerAdapter> {
        private final TextView mColorPreview;
        private final TextView mColorName;
        private final SeekBar mColorSeekBar;
        private final SeekBar mAlphaSeekBar;

        public ColorPickerViewHolder(View itemView, ColorPickerAdapter adapter) {
            super(itemView, adapter);
            mColorSeekBar = itemView.findViewById(R.id.color_seek_bar);
            mAlphaSeekBar = itemView.findViewById(R.id.alpha_seek_bar);
            mColorPreview = itemView.findViewById(R.id.preview);
            mColorName = itemView.findViewById(R.id.name);
            mColorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int pos = getAdapterPosition();
                    ColorPickerData data = mAdapter.getItem(pos);
                    String colorStr = Integer.toHexString(progress);
                    data.setValue(colorStr);
                    mColorPreview.setText(data.getColor());
                    mColorPreview.setTextColor(Color.parseColor(data.getColor()));
                    if (fromUser) {
                        mAdapter.onColorChanged(ColorPickerViewHolder.this);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mAlphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int pos = getAdapterPosition();
                    ColorPickerData data = mAdapter.getItem(pos);
                    String alphaStr = Integer.toHexString(progress);
                    data.setAlpha(alphaStr);
                    mColorPreview.setText(data.getColor());
                    mColorPreview.setTextColor(Color.parseColor(data.getColor()));
                    if (fromUser) {
                        mAdapter.onColorChanged(ColorPickerViewHolder.this);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }
}
