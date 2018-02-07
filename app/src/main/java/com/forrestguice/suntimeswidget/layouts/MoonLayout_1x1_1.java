/**
   Copyright (C) 2018 Forrest Guice
   This file is part of SuntimesWidget.

   SuntimesWidget is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   SuntimesWidget is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with SuntimesWidget.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.forrestguice.suntimeswidget.layouts;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import com.forrestguice.suntimeswidget.R;
import com.forrestguice.suntimeswidget.SuntimesUtils;
import com.forrestguice.suntimeswidget.calculator.MoonPhaseDisplay;
import com.forrestguice.suntimeswidget.calculator.SuntimesMoonData;
import com.forrestguice.suntimeswidget.themes.SuntimesTheme;

import java.text.NumberFormat;

public class MoonLayout_1x1_1 extends MoonLayout
{
    public MoonLayout_1x1_1()
    {
        super();
    }

    public MoonLayout_1x1_1(int layoutID)
    {
        this.layoutID = layoutID;
    }

    @Override
    public void initLayoutID()
    {
        this.layoutID = R.layout.layout_widget_moon_1x1_1;
    }

    @Override
    public void updateViews(Context context, int appWidgetId, RemoteViews views, SuntimesMoonData data)
    {
        super.updateViews(context, appWidgetId, views, data);

        NumberFormat percentage = NumberFormat.getPercentInstance();
        String illum = percentage.format(data.getMoonIlluminationToday());
        String illumNote = context.getString(R.string.moon_illumination, illum);
        SpannableString illumNoteSpan = SuntimesUtils.createColorSpan(illumNote, illum, illumColor);
        views.setTextViewText(R.id.text_info_moonillum, illumNoteSpan);

        for (MoonPhaseDisplay moonPhase : MoonPhaseDisplay.values())
        {
            views.setViewVisibility(moonPhase.getView(), View.GONE);
        }

        MoonPhaseDisplay phase = data.getMoonPhaseToday();
        if (phase != null)
        {
            views.setTextViewText(R.id.text_info_moonphase, phase.getLongDisplayString());
            views.setViewVisibility(phase.getView(), View.VISIBLE);
        }
    }

    private int illumColor = Color.WHITE;

    @Override
    public void themeViews(Context context, RemoteViews views, SuntimesTheme theme)
    {
        super.themeViews(context, views, theme);

        illumColor = theme.getTimeColor();
        int textColor = theme.getTextColor();
        views.setTextColor(R.id.text_info_moonillum, textColor);
        views.setTextColor(R.id.text_info_moonphase, textColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            float textSize = theme.getTextSizeSp();
            views.setTextViewTextSize(R.id.text_info_moonphase, TypedValue.COMPLEX_UNIT_SP, textSize);
            views.setTextViewTextSize(R.id.text_info_moonillum, TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    @Override
    public void prepareForUpdate(SuntimesMoonData data)
    {
        // EMPTY
    }
}
