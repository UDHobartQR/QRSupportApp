package com.hobart.hobartqr;

/**
 * Created by Craig on 4/6/2016.
 */
public class RegistrationTable {

    @com.google.gson.annotations.SerializedName("id")
    public String Id;

    @com.google.gson.annotations.SerializedName("serial_number")
    public String serial_number;

    @com.google.gson.annotations.SerializedName("model_number")
    public String model_number;

    @com.google.gson.annotations.SerializedName("zip_code")
    public String zip_code;

    @com.google.gson.annotations.SerializedName("name")
    public String name;

//    public String Id;
//    public String serial_number;
//    public String model_number;
//    public String zip_code;
//    public String name;


    /**
     * RegistrationTable constructor
     */
    public RegistrationTable() {

    }

    public RegistrationTable(String Serial, String Model, String Zip, String Name) {
        this.setSerial(Serial);
        this.setModel(Model);
        this.setZip(Zip);
        this.setName(Name);
    }

    public String getSerial() { return serial_number; }
    public void setSerial(String serial) { serial_number = serial; }

    public String getZip() { return zip_code; }
    public void setZip(String zip) { zip_code = zip; }

    public String getModel() { return model_number; }
    public void setModel(String model) { model_number = model; }

    public String getName() { return name; }
    public void setName(String name) { name = name; }

    public String getId() { return Id; }
    public void setId(String id) { Id = id; }

}