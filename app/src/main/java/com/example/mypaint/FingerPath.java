package com.example.mypaint;

import android.graphics.Path;

public class FingerPath {

    public int Color;
    public boolean emboss;
    public boolean blur;
    public float strokeWidth;
    public Path path;

    public FingerPath(int color, boolean emboss, boolean blur, float strokeWidth, Path path) {
        Color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }

}
