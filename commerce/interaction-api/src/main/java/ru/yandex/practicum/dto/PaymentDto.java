package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private String paymentId;

    private BigDecimal totalPayment;

    //Возникла неоднозначность - сумма за товары никак не передается в order а в нем есть такое поле.
    //Чтобы снять вопросы, решил добавить здесь еще поле. Кстати, а доставка под НДС не подпадает?
    //Согласно заданию, нет. Поэтому сделал как в задании, если что - подправить секундное дело...
    private BigDecimal productCost;

    private BigDecimal deliveryTotal;

    private BigDecimal feeTotal;
}
