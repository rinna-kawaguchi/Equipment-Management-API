package com.example.equipment.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  // GETメソッドで存在する設備IDを指定した時に、指定した設備IDの点検計画が取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 指定した設備IDの点検計画が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/plan"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
             [
               {
                 "checkPlanId": 1,
                 "equipmentId": 1,
                 "checkType": "簡易点検",
                 "period": "1年",
                 "deadline": "2023-09-30"
               },
               {
                 "checkPlanId": 2,
                 "equipmentId": 1,
                 "checkType": "本格点検",
                 "period": "5年",
                 "deadline": "2026-09-30"
               }
         ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドで点検計画が存在しない時に、空のListが返されステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/plan/empty.yml")
  @Transactional
  void 点検計画が存在しない時に空のListが取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/plan"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        []
        """, response, JSONCompareMode.STRICT);
  }
}
