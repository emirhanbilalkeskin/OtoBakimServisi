package service;

import dao.CarDAO;
import model.Car;
import java.util.List;

public class CarService {

    private CarDAO carDAO;

    public CarService() {
        carDAO = new CarDAO();
    }

    // GÜNCELLEME: void yerine boolean döndürerek başarı kontrolü sağlıyoruz
    public boolean addCar(int userId, String plate, String brand, String model, int year) {
        Car car = new Car(
                0, // carId (AUTO_INCREMENT)
                userId,
                plate,
                brand,
                model,
                year
        );

        // DAO'dan dönen sonucu yukarı iletiyoruz (DAO'nuzun da boolean döndüğünden emin olun)
        return carDAO.addCar(car);
    }
    
    public Car getCarById(int carId) {
        return carDAO.getCarById(carId);
    }

    public List<Car> getCarsByUser(int userId) {
        return carDAO.getCarsByUser(userId);
    }
}