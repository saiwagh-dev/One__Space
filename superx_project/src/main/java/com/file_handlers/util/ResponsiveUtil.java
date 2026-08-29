package com.file_handlers.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ResponsiveUtil {

    private static final Rectangle2D BOUNDS = Screen.getPrimary().getVisualBounds();

    public static final double WIDTH = BOUNDS.getWidth();
    public static final double HEIGHT = BOUNDS.getHeight();

    public static final boolean COMPACT = WIDTH <= 1600 || HEIGHT <= 900;

    public static final double SIDEBAR_WIDTH = COMPACT ? 200 : 235;
    public static final double CONTENT_MAX_WIDTH = COMPACT ? 960 : 1120;
    public static final double PAGE_PADDING = COMPACT ? 20 : 28;

    public static final double AUTH_CARD_WIDTH = 420;
    public static final double AUTH_FIELD_WIDTH = 350;
    
    // Private constructor to prevent instantiation
    private ResponsiveUtil() {}
}