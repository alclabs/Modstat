package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.checks.Checker;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class BaseChecker implements Checker {
    protected static final NumberFormat numberFormat = new DecimalFormat("#,###");

    protected String name;
    protected String description;
    private   boolean enabled;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
