package com.example.equipment.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 指定した設備IDの点検計画が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/plans"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
             [
               {
                 "checkPlanId": 1,
                 "equipmentId": 1,
                 "checkTypeId": 1,
                 "periodValue": 1,
                 "periodUnit": "year",
                 "deadline": "2023-09-30",
                 "manualOverride": false
               },
               {
                 "checkPlanId": 2,
                 "equipmentId": 1,
                 "checkTypeId": 2,
                 "periodValue": 5,
                 "periodUnit": "year",
                 "deadline": "2026-09-30",
                 "manualOverride": true
               }
         ]
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/plan/empty.yml")
  @Transactional
  void 点検計画が存在しない時に空のListが取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/plans"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        []
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/insert_plan.yml", ignoreCols = "check_plan_id")
  @Transactional
  void 指定した設備の点検計画が登録できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 3,
                      "periodValue": 10,
                      "periodUnit": "year",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検計画が正常に登録されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void 登録の際に指定した設備IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/4/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 3,
                      "periodValue": 10,
                      "periodUnit": "year",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "404",
          "error": "Not Found",
          "message": "Not Found",
          "path": "/equipments/4/plans"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストでcheckTypeIdが0の時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 0,
                      "periodValue": 10,
                      "periodUnit": "year",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "checkTypeId,periodValue,periodUnitは必須項目です",
          "path": "/equipments/1/plans"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストでperiodUnitが空文字の時にエラーメッセージが返されること()
      throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 1,
                      "periodValue": 10,
                      "periodUnit": "",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "checkTypeId,periodValue,periodUnitは必須項目です",
          "path": "/equipments/1/plans"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストでperiodUnitが不正な値の時にエラーメッセージが返されること()
      throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 1,
                      "periodValue": 10,
                      "periodUnit": "invalid",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "checkTypeId,periodValue,periodUnitは必須項目です",
          "path": "/equipments/1/plans"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストでcheckTypeIdがnullの時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": null,
                      "periodValue": 10,
                      "periodUnit": "year",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "checkTypeId,periodValue,periodUnitは必須項目です",
          "path": "/equipments/1/plans"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/update_plan.yml")
  @Transactional
  void 指定したIDの点検計画が更新できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/plans/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 3,
                      "periodValue": 10,
                      "periodUnit": "year",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検計画が正常に更新されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 更新の際に指定した点検計画IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/plans/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 3,
                      "periodValue": 10,
                      "periodUnit": "year",
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "404",
          "error": "Not Found",
          "message": "Not Found",
          "path": "/plans/5"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 更新リクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/plans/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkTypeId": 1,
                      "periodValue": 10,
                      "periodUnit": null,
                      "deadline": "2030-09-30"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "checkTypeId,periodValue,periodUnitは必須項目です",
          "path": "/plans/2"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/delete_plan.yml")
  @Transactional
  void 指定したIDの点検計画が削除できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/plans/4"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検計画が正常に削除されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 削除の際に指定した点検計画IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/plans/5"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "404",
          "error": "Not Found",
          "message": "Not Found",
          "path": "/plans/5"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/delete_by_equipment_id.yml")
  @Transactional
  void 指定した設備IDの点検計画が削除できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/2/plans"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検計画が正常に削除されました"
        }
        """, response, JSONCompareMode.STRICT);
  }
}
