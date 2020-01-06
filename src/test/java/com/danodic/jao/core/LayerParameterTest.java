package com.danodic.jao.core;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LayerParameterTest {

    LayerParameters params;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        params = new LayerParameters();
    }

    @Test
    public void testGetAsString() {
        params.put("sample_param", "ok");
        assert params.getAsString("sample_param") instanceof String;
        assert params.getAsString("sample_param").equals("ok");
    }

    @Test
    public void testGetAsStringDefault() {
        assert params.getAsString("sample_param") instanceof String;
        assert params.getAsString("sample_param").equals("");
    }

    @Test
    public void testGetAsInteger() {
        params.put("sample_param", 1);
        assert params.getAsInteger("sample_param") instanceof Integer;
        assert params.getAsInteger("sample_param").equals(1);
    }

    @Test
    public void testGetAsIntegerDefault() {
        assert params.getAsInteger("sample_param") instanceof Integer;
        assert params.getAsInteger("sample_param").equals(0);
    }

    @Test
    public void testGetAsLong() {
        params.put("sample_param", 1L);
        assert params.getAsLong("sample_param") instanceof Long;
        assert params.getAsLong("sample_param").equals(1L);
    }

    @Test
    public void testGetAsLongDefaults() {
        assert params.getAsLong("sample_param") instanceof Long;
        assert params.getAsLong("sample_param").equals(0L);
    }

    @Test
    public void testGetAsFloat() {
        params.put("sample_param", 1F);
        assert params.getAsFloat("sample_param") instanceof Float;
        assert params.getAsFloat("sample_param").equals(1F);
    }

    @Test
    public void testGetAsFloatDefaults() {
        assert params.getAsFloat("sample_param") instanceof Float;
        assert params.getAsFloat("sample_param").equals(0F);
    }

    @Test
    public void testGetAsDouble() {
        params.put("sample_param", 1D);
        assert params.getAsDouble("sample_param") instanceof Double;
        assert params.getAsDouble("sample_param").equals(1D);
    }

    @Test
    public void testGetAsDoubleDefaults() {
        assert params.getAsDouble("sample_param") instanceof Double;
        assert params.getAsDouble("sample_param").equals(0D);
    }

    @Test
    public void testGetAsBoolean() {
        params.put("sample_param", true);
        assert params.getAsBoolean("sample_param") instanceof Boolean;
        assert params.getAsBoolean("sample_param").equals(true);
    }

    @Test
    public void testGetAsBooleanDefaults() {
        assert params.getAsBoolean("sample_param") instanceof Boolean;
        assert params.getAsBoolean("sample_param").equals(false);
    }

}