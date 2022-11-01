package application.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PrioridadeEnum {

    ALTA(1), MEDIA(2), BAIXA(3);


    private Integer code;

    public static PrioridadeEnum valueOf(Integer code) {
        for (PrioridadeEnum value : PrioridadeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Codigo Inválido!");
    }
}
