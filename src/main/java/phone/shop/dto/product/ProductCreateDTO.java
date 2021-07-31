package phone.shop.dto.product;

import lombok.Getter;
import lombok.Setter;
import phone.shop.dto.ManufacturerDTO;
import phone.shop.entity.ManufacturerEntity;
import phone.shop.types.ProductType;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProductCreateDTO {
    private Integer id;
    private String name;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEn;
    private String model;
    private ProductType type; // Android / IOS
    private Double price;
    private Double oldPrice;
    private Integer amount;
    private Boolean availability;
    private Double weight;
    private String color;
    private Double screenDiagonal; // Диагональ экрана
    private Boolean isFrontCameraExists; // Фронтальная фотокамера
    private Double frontCameraMp; // Главная фотокамера
    private Integer simCardAmount; // Количество SIM-карт
    private Double batteryCapacity; // Емкость аккумулятора
    private ManufacturerDTO manufacturer;
//    private Integer manufactureId;
    //    private ManufacturerEntity manufacturer; //  ishlab chiqaruvchi
    private LocalDateTime createdDate;

}
