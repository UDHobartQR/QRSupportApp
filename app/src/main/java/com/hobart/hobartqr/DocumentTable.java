package com.hobart.hobartqr;

/**
 * Created by Craig on 4/6/2016.
 */
public class DocumentTable {

    @com.google.gson.annotations.SerializedName("id")
    public String Id;
    @com.google.gson.annotations.SerializedName("model_number")
    public String model_number;
    @com.google.gson.annotations.SerializedName("spec_sheet")
    public String spec_sheet;
    @com.google.gson.annotations.SerializedName("instruction_manual")
    public String instruction_manual;
    @com.google.gson.annotations.SerializedName("support")
    public String support;
    @com.google.gson.annotations.SerializedName("parts_catalog")
    public String parts_catalog;

    @com.google.gson.annotations.SerializedName("operator_manual")
    public String operator_manual;
    @com.google.gson.annotations.SerializedName("quick_start_guide")
    public String quick_start_guide;
    @com.google.gson.annotations.SerializedName("supervisor_manual")
    public String supervisor_manual;
    @com.google.gson.annotations.SerializedName("replacement_url")
    public String replacement_url;
    @com.google.gson.annotations.SerializedName("installation_manual")
    public String installation_manual;
    @com.google.gson.annotations.SerializedName("accessory_instructions")
    public String accessory_instructions;
    @com.google.gson.annotations.SerializedName("wall_chart")
    public String wall_chart;

//    public String modelNumber;
//    public String specSheet;
//    public String manual;
//    public String support;
//    public String partsNumber;

    /**
     * RegistrationTable constructor
     */
    public DocumentTable() {

    }

    public DocumentTable(String Model, String Spec, String Manual, String Support, String Part,
                         String Op, String QS, String SM, String ReplaceURL,
                         String IstallMan, String AccInstr, String WallChart) {
        model_number = Model;
        spec_sheet = Spec;
        instruction_manual = Manual;
        support = Support;
        parts_catalog = Part;

        operator_manual = Op;
        quick_start_guide = QS;
        supervisor_manual = SM;
        replacement_url = ReplaceURL;
        installation_manual = IstallMan;
        accessory_instructions = AccInstr;
        wall_chart = WallChart;
    }


    public String getModelNumber() { return model_number; }
    public void setModelNumber(String Model) { model_number = Model; }

    public String getSpecSheet() { return spec_sheet; }
    public void setSpecSheet(String Spec) { spec_sheet = Spec; }

    public String getManual() { return instruction_manual; }
    public void setManual(String Manual) { instruction_manual = Manual; }

    public String getSupport() { return support; }
    public void setSupport(String Support) { support = Support; }

    public String getPartsNumber() { return parts_catalog; }
    public void setPartsNumber(String Part) { parts_catalog = Part; }

    public String getOperatorManual() { return operator_manual; }
    public void setOperatorManual(String Op) { operator_manual = Op; }

    public String getQuickStart() { return quick_start_guide; }
    public void setQuickStart(String Qs) { quick_start_guide = Qs; }

    public String getSupervisorMan() { return supervisor_manual; }
    public void setSupervisorMan(String sm) { supervisor_manual = sm; }

    public String getReplacementURL() { return replacement_url; }
    public void setReplacementURL(String rm) { replacement_url = rm; }

    public String getInstallationMan() { return installation_manual; }
    public void setInstallationMan(String im) { installation_manual = im; }

    public String getAccInstructions() { return accessory_instructions; }
    public void setAccInstructions(String AccInstr) { accessory_instructions = AccInstr; }

    public String getWallChart() { return wall_chart; }
    public void setWallChart(String wc) { wall_chart = wc; }



}
