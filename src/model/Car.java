package model;

/*
 * Araç entity class'ı
 * car tablosuna birebir karşılık gelir
 */
public class Car {

    private int carId;
    private int userId;
    private String plate;
    private String brand;
    private String model;
    private int year;

    // 🔹 Veritabanından okurken kullanılacak constructor
    public Car(int carId,
               int userId,
               String plate,
               String brand,
               String model,
               int year) {

        this.carId = carId;
        this.userId = userId;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // 🔹 GUI → Service tarafında yeni araç eklerken kullanılacak
    public Car(int userId,
               String plate,
               String brand,
               String model,
               int year) {

        this.userId = userId;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // Getter / Setter'lar
    public int getCarId() {
        return carId;
    }

    public int getUserId() {
        return userId;
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

@Override
public String toString() {
    return plate + " - " + brand + " " + model + " (" + year + ")";
}}

