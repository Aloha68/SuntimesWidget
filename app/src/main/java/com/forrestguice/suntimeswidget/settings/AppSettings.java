/**
    Copyright (C) 2014 Forrest Guice
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

package com.forrestguice.suntimeswidget.settings;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.forrestguice.suntimeswidget.R;
<<<<<<< HEAD
import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetDataset;
import com.forrestguice.suntimeswidget.settings.appcolors.AppColors;
=======
import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetData;
>>>>>>> master

import java.util.Locale;

/**
 * Shared preferences used by the app; uses getDefaultSharedPreferences (stored in com.forrestguice.suntimeswidget_preferences.xml).
 */
public class AppSettings
{
    public static final String THEME_DARK = "dark";
    public static final String THEME_LIGHT = "light";
    public static final String THEME_DAYNIGHT = "daynight";

    public static final String PREF_KEY_APPEARANCE_THEME = "app_appearance_theme";
    public static final String PREF_DEF_APPEARANCE_THEME = THEME_DARK;

    public static final String PREF_KEY_APPEARANCE_COLORS = "app_appearance_colors";
    public static final String PREF_DEF_APPEARANCE_COLORS = "default";

    public static final String PREF_KEY_LOCALE_MODE = "app_locale_mode";
    public static final LocaleMode PREF_DEF_LOCALE_MODE = LocaleMode.SYSTEM_LOCALE;

    public static final String PREF_KEY_LOCALE = "app_locale";
    public static final String PREF_DEF_LOCALE = "en";

    public static final String PREF_KEY_UI_DATETAPACTION = "app_ui_datetapaction";
    public static final DateTapAction PREF_DEF_UI_DATETAPACTION = DateTapAction.CONFIG_DATE;

    public static final String PREF_KEY_UI_CLOCKTAPACTION = "app_ui_clocktapaction";
    public static final ClockTapAction PREF_DEF_UI_CLOCKTAPACTION = ClockTapAction.ALARM;

    public static final String PREF_KEY_UI_NOTETAPACTION = "app_ui_notetapaction";
    public static final ClockTapAction PREF_DEF_UI_NOTETAPACTION = ClockTapAction.NEXT_NOTE;

    public static final String PREF_KEY_UI_SHOWWARNINGS = "app_ui_showwarnings";
    public static final boolean PREF_DEF_UI_SHOWWARNINGS = true;

    public static final String PREF_KEY_UI_SHOWLIGHTMAP = "app_ui_showlightmap";
    public static final boolean PREF_DEF_UI_SHOWLIGHTMAP = true;

    public static final String PREF_KEY_UI_SHOWEQUINOX = "app_ui_showequinox";
    public static final boolean PREF_DEF_UI_SHOWEQUINOX = true;

    public static final String PREF_KEY_UI_SHOWDATASOURCE = "app_ui_showdatasource";
    public static final boolean PREF_DEF_UI_SHOWDATASOURCE = true;

    public static final String PREF_KEY_UI_TIMEZONESORT = "app_ui_timezonesort";
    public static final WidgetTimezones.TimeZoneSort PREF_DEF_UI_TIMEZONESORT = WidgetTimezones.TimeZoneSort.SORT_BY_ID;

    public static final String PREF_KEY_GETFIX_MINELAPSED = "getFix_minElapsed";
    public static final String PREF_KEY_GETFIX_MAXELAPSED = "getFix_maxElapsed";
    public static final String PREF_KEY_GETFIX_MAXAGE = "getFix_maxAge";

    /**
     * Language modes (system, user defined)
     */
    public static enum LocaleMode
    {
        SYSTEM_LOCALE("System Locale"),
        CUSTOM_LOCALE("Custom Locale");

        private String displayString;

        private LocaleMode( String displayString )
        {
            this.displayString = displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }
        public static void initDisplayStrings( Context context )
        {
            String[] labels = context.getResources().getStringArray(R.array.localeMode_display);
            SYSTEM_LOCALE.setDisplayString(labels[0]);
            CUSTOM_LOCALE.setDisplayString(labels[1]);
        }
    }

    /**
     * Preference: locale mode
     */
    public static LocaleMode loadLocaleModePref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return loadLocaleModePref(pref);
    }

    public static LocaleMode loadLocaleModePref( SharedPreferences pref )
    {
        String modeString = pref.getString(PREF_KEY_LOCALE_MODE, PREF_DEF_LOCALE_MODE.name());

        LocaleMode localeMode;
        try {
            localeMode = LocaleMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            localeMode = PREF_DEF_LOCALE_MODE;
        }
        return localeMode;
    }

    /**
     * Preference: custom locale
     */
    public static String loadLocalePref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_KEY_LOCALE, PREF_DEF_LOCALE);
    }

    /**
     * @return true if locale was changed by init, false otherwise
     */
    public static boolean initLocale( Context context )
    {
        AppSettings.LocaleMode localeMode = AppSettings.loadLocaleModePref(context);
        if (localeMode == AppSettings.LocaleMode.CUSTOM_LOCALE)
        {
            return AppSettings.loadLocale(context, AppSettings.loadLocalePref(context));

        } else {
            return resetLocale(context);
        }
    }

    /**
     * @return true if the locale was changed by reset, false otherwise
     */
    public static boolean resetLocale( Context context )
    {
        //noinspection SimplifiableIfStatement
        if (systemLocale != null)
        {
            //Log.d("resetLocale", "locale reset to " + systemLocale);
            return loadLocale(context, systemLocale);
        }
        return false;
    }

    private static String systemLocale = null;  // null until locale is overridden w/ loadLocale
    public static String getSystemLocale()
    {
        if (systemLocale == null)
        {
            systemLocale = Locale.getDefault().getLanguage();
        }
        return systemLocale;
    }
    public static Locale getLocale()
    {
        return Locale.getDefault();
    }

    public static boolean loadLocale( Context context, String localeCode )
    {
        Resources resources = context.getApplicationContext().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        if (systemLocale == null)
        {
            systemLocale = Locale.getDefault().getLanguage();
        }
        Locale customLocale = new Locale(localeCode);

        Locale.setDefault(customLocale);
        config.locale = customLocale;
        resources.updateConfiguration(config, metrics);

        //Log.d("loadLocale", "locale loaded " + localeCode);
        return true;
    }

    /**
     * Is the current locale right-to-left?
     * @param context a context used to access resources
     * @return true the locale is right-to-left, false the locale is left-to-right
     */
    public static boolean isLocaleRtl(Context context)
    {
        return context.getResources().getBoolean(R.bool.is_rtl);
    }

    /**
     * Preference: color scheme
     * @return color scheme name
     */
    public static String loadAppColorsPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_KEY_APPEARANCE_COLORS, PREF_DEF_APPEARANCE_COLORS);
    }

    public static void setAppColorsPref( Context context, String schemeName )
    {
        SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(context).edit();
        pref.putString(PREF_KEY_APPEARANCE_COLORS, schemeName);
        pref.apply();
    }

    /**
     * @param context context for accessing sharedprefs
     * @return an AppColors obj (or null if configured to default colors)
     */
    public static AppColors loadAppColors(Context context )
    {
        String appColorsName = loadAppColorsPref(context);
        AppColors appColors = null;
        if (!appColorsName.equals(AppColors.DEFAULT_NAME))
        {
            appColors = new AppColors(context);
            appColors.loadAppColors(context, appColorsName);
        }
        return appColors;
    }

    /**
     * Actions that can be performed when the clock is clicked.
     */
    public static enum ClockTapAction
    {
        NOTHING("Do Nothing"),
        ALARM("Set an Alarm"),
        NEXT_NOTE("Show next note"),
        PREV_NOTE("Show previous note");

        private String displayString;

        private ClockTapAction(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            String[] labels = context.getResources().getStringArray(R.array.clockTapActions_display);
            NOTHING.setDisplayString(labels[0]);
            ALARM.setDisplayString(labels[1]);
            NEXT_NOTE.setDisplayString(labels[2]);
            PREV_NOTE.setDisplayString(labels[3]);
        }
    }

    /**
     * Actions that can be performed when the date field is clicked.
     */
    public static enum DateTapAction
    {
        NOTHING("Do Nothing"),
        SWAP_CARD("Swap Cards"),
        SHOW_CALENDAR("Show Calendar"),
        CONFIG_DATE("Set Custom Date");

        private String displayString;

        private DateTapAction(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            String[] labels = context.getResources().getStringArray(R.array.dateTapActions_display);
            NOTHING.setDisplayString(labels[0]);
            SWAP_CARD.setDisplayString(labels[1]);
            SHOW_CALENDAR.setDisplayString(labels[2]);
            CONFIG_DATE.setDisplayString(labels[3]);
        }
    }

    public static void setTimeZoneSortPref( Context context, WidgetTimezones.TimeZoneSort sortMode )
    {
        SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(context).edit();
        pref.putString(PREF_KEY_UI_TIMEZONESORT, sortMode.name());
        pref.apply();
    }

    public static WidgetTimezones.TimeZoneSort loadTimeZoneSortPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String modeString = pref.getString(PREF_KEY_UI_TIMEZONESORT, PREF_DEF_UI_TIMEZONESORT.name());

        WidgetTimezones.TimeZoneSort sortMode;
        try {
            sortMode = WidgetTimezones.TimeZoneSort.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            sortMode = PREF_DEF_UI_TIMEZONESORT;
        }
        return sortMode;
    }

    public static boolean loadShowWarningsPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(PREF_KEY_UI_SHOWWARNINGS, PREF_DEF_UI_SHOWWARNINGS);
    }

    public static boolean loadShowLightmapPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(PREF_KEY_UI_SHOWLIGHTMAP, PREF_DEF_UI_SHOWLIGHTMAP);
    }

    public static boolean loadShowEquinoxPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(PREF_KEY_UI_SHOWEQUINOX, PREF_DEF_UI_SHOWEQUINOX);
    }

    public static boolean loadDatasourceUIPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(PREF_KEY_UI_SHOWDATASOURCE, PREF_DEF_UI_SHOWDATASOURCE);
    }

    /**
     * Preference: the action that is performed when the clock ui is clicked/tapped
     */
    public static ClockTapAction loadClockTapActionPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String modeString = pref.getString(PREF_KEY_UI_CLOCKTAPACTION, PREF_DEF_UI_CLOCKTAPACTION.name());

        ClockTapAction actionMode;
        try {
            actionMode = ClockTapAction.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            actionMode = PREF_DEF_UI_CLOCKTAPACTION;
        }
        return actionMode;
    }

    /**
     * Preference: the action that is performed when the date field is clicked/tapped
     */
    public static DateTapAction loadDateTapActionPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String modeString = pref.getString(PREF_KEY_UI_DATETAPACTION, PREF_DEF_UI_DATETAPACTION.name());

        DateTapAction actionMode;
        try {
            actionMode = DateTapAction.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            actionMode = PREF_DEF_UI_DATETAPACTION;
        }
        return actionMode;
    }

    /**
     * Preference: the action that is performed when the note ui is clicked/tapped
     */
    public static ClockTapAction loadNoteTapActionPref( Context context )
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String modeString = pref.getString(PREF_KEY_UI_NOTETAPACTION, PREF_DEF_UI_NOTETAPACTION.name());
        ClockTapAction actionMode;

        try {
            actionMode = ClockTapAction.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            actionMode = PREF_DEF_UI_NOTETAPACTION;
        }
        return actionMode;
    }

    /**
     * @param context an application context
     * @return a theme identifier
     */
    public static String loadThemePref(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_KEY_APPEARANCE_THEME, PREF_DEF_APPEARANCE_THEME);
    }

    public static int loadTheme(Context context)
    {
        return themePrefToStyleId(context, loadThemePref(context), null);
    }
    public static int loadTheme(Context context, SuntimesRiseSetData data)
    {
        return themePrefToStyleId(context, loadThemePref(context), data);
    }

    public static int themePrefToStyleId( Context context, String themeName )
    {
        return themePrefToStyleId(context, themeName, null);
    }
    public static int themePrefToStyleId( Context context, String themeName, SuntimesRiseSetData data )
    {
        int styleID = R.style.AppTheme_Dark;
        if (themeName != null)
        {
            if (themeName.equals(THEME_LIGHT))
            {
                styleID = R.style.AppTheme_Light;

            } else if (themeName.equals(THEME_DARK)) {
                styleID = R.style.AppTheme_Dark;

            } else if (themeName.equals(THEME_DAYNIGHT)) {
                if (data == null)
                {
                    data = new SuntimesRiseSetData(context, AppWidgetManager.INVALID_APPWIDGET_ID);
                    data.initCalculator();
                }
                styleID = (data.isDay() ? R.style.AppTheme_Light : R.style.AppTheme_Dark);
            }
        }
        return styleID;
    }

    /**
     * @param prefs an instance of SharedPreferences
     * @param defaultValue the default max age value if pref can't be loaded
     * @return the gps max age value (milliseconds)
     */
    public static int loadPrefGpsMaxAge(SharedPreferences prefs, int defaultValue)
    {
        int retValue;
        try {
            String maxAgeString = prefs.getString(PREF_KEY_GETFIX_MAXAGE, defaultValue+"");
            retValue = Integer.parseInt(maxAgeString);
        } catch (NumberFormatException e) {
            Log.e("loadPrefGPSMaxAge", "Bad setting! " + e);
            retValue = defaultValue;
        }
        return retValue;
    }

    /**
     * @param prefs an instance of SharedPreferences
     * @param defaultValue the default min elapsed value if pref can't be loaded
     * @return the gps min elapsed value (milliseconds)
     */
    public static int loadPrefGpsMinElapsed(SharedPreferences prefs, int defaultValue)
    {
        int retValue;
        try {
            String minAgeString = prefs.getString(PREF_KEY_GETFIX_MINELAPSED, defaultValue+"");
            retValue = Integer.parseInt(minAgeString);
        } catch (NumberFormatException e) {
            Log.e("loadPrefGPSMinElapsed", "Bad setting! " + e);
            retValue = defaultValue;
        }
        return retValue;
    }

    /**
     * @param prefs an instance of SharedPreferences
     * @param defaultValue the default max elapsed value if pref can't be loaded
     * @return the gps max elapsed value (milliseconds)
     */
    public static int loadPrefGpsMaxElapsed(SharedPreferences prefs, int defaultValue)
    {
        int retValue;
        try {
            String maxElapsedString = prefs.getString(PREF_KEY_GETFIX_MAXELAPSED, defaultValue+"");
            retValue = Integer.parseInt(maxElapsedString);
        } catch (NumberFormatException e) {
            Log.e("loadPrefGPSMaxElapsed", "Bad setting! " + e);
            retValue = defaultValue;
        }
        return retValue;
    }

    /**
     * @param context a context used to access resources
     */
    public static void initDisplayStrings( Context context )
    {
        LocaleMode.initDisplayStrings(context);
        ClockTapAction.initDisplayStrings(context);
    }

}
