package com.flexbox.backend.admin.analytics.service;

import com.flexbox.backend.admin.analytics.model.MonthlySales;
import com.flexbox.backend.admin.analytics.model.SubscriptionBoxCost;
import com.flexbox.backend.admin.analytics.model.SubscriptionBoxProductCost;
import com.flexbox.backend.admin.analytics.repository.MonthlySalesRepository;
import com.flexbox.backend.admin.analytics.repository.SubscriptionBoxCostRepository;
import com.flexbox.backend.admin.analytics.repository.SubscriptionBoxProductCostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAnalyticsServiceTest {

    @Mock
    MonthlySalesRepository salesRepository;

    @Mock
    SubscriptionBoxProductCostRepository boxProductCostRepository;

    @Mock
    SubscriptionBoxCostRepository boxCostRepository;

    @InjectMocks
    AdminAnalyticsService analyticsService;
    //        ReflectionTestUtils.setField(boxCost1,"subscriptionBoxId", 1L);
//        ReflectionTestUtils.setField(boxCost1,"boxName", "Running Box");
//        ReflectionTestUtils.setField(boxCost1,"boxCost", BigDecimal.valueOf(12.99));

//        var boxCost2 = new SubscriptionBoxCost();
//        ReflectionTestUtils.setField(boxCost2,"subscriptionBoxId", 2L);
//        ReflectionTestUtils.setField(boxCost2,"boxName", "Yoga Box");
//        ReflectionTestUtils.setField(boxCost2,"boxCost", BigDecimal.valueOf(14.99));

    private SubscriptionBoxCost buildBoxCost(Long id, String name, BigDecimal cost){
        var boxCost = new SubscriptionBoxCost();
        ReflectionTestUtils.setField(boxCost,"subscriptionBoxId", id);
        ReflectionTestUtils.setField(boxCost,"boxName", name);
        ReflectionTestUtils.setField(boxCost,"boxCost", cost);

        return boxCost;
    }

    private SubscriptionBoxProductCost buildProductCost(Long boxId, Long productId, String boxName,
                                                        String brand, String productName, Long categoryId,
                                                        String categoryName, Integer quantity, BigDecimal productCost){
        var boxProductCost = new SubscriptionBoxProductCost();
        ReflectionTestUtils.setField(boxProductCost, "subscriptionBoxId", boxId);
        ReflectionTestUtils.setField(boxProductCost, "productId", productId);
        ReflectionTestUtils.setField(boxProductCost, "boxName", boxName);
        ReflectionTestUtils.setField(boxProductCost, "brand", brand);
        ReflectionTestUtils.setField(boxProductCost, "productName", productName);
        ReflectionTestUtils.setField(boxProductCost, "categoryId", categoryId);
        ReflectionTestUtils.setField(boxProductCost, "categoryName", categoryName);
        ReflectionTestUtils.setField(boxProductCost, "quantity", quantity);
        ReflectionTestUtils.setField(boxProductCost, "productCost", productCost);

        return boxProductCost;
    }

    private MonthlySales buildMonthlySales(OffsetDateTime month, Long boxId, String boxName, Long unitsSold, BigDecimal grossRevenue,
                                           BigDecimal productCost, BigDecimal grossProfit) {
        var monthlySales = new MonthlySales();
        ReflectionTestUtils.setField(monthlySales,"month", month);
        ReflectionTestUtils.setField(monthlySales, "subscriptionBoxId", boxId);
        ReflectionTestUtils.setField(monthlySales, "boxName", boxName);
        ReflectionTestUtils.setField(monthlySales, "unitsSold", unitsSold);
        ReflectionTestUtils.setField(monthlySales, "grossRevenue", grossRevenue);
        ReflectionTestUtils.setField(monthlySales, "productCost", productCost);
        ReflectionTestUtils.setField(monthlySales, "grossProfit", grossProfit);

        return monthlySales;
    }

    @Test
    void getBoxCosts() {

        var boxCost1 = buildBoxCost(1L, "Running Box", BigDecimal.valueOf(12.99));
        var boxCost2 = buildBoxCost(2L, "Yoga Box", BigDecimal.valueOf(14.00));

        when(boxCostRepository.findAll())
                .thenReturn(List.of(boxCost1, boxCost2));

        var result = analyticsService.getBoxCosts();

        assertThat(result.items())
                .hasSize(2);

        verify(boxCostRepository.findAll());
    }

    @Test
    void getBoxProductCosts() {


    }

    @Test
    void getBoxProductCostByBoxId() {
    }

    @Test
    void getMonthlySales() {

        var monthlySales = buildMonthlySales(, 1L, "Running Box", 123,
                BigDecimal.valueOf(5642.32), BigDecimal.valueOf(2342.32), BigDecimal.valueOf(3223.32));
    }
}