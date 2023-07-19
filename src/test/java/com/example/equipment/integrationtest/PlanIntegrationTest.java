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

  // POSTメソッドで正しくリクエスト（checkType,periodをいずれも10文字以内で入力）した時に、
  // 指定した設備の点検計画が登録できステータスコード201とメッセージが返されること
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/insert_plan.yml", ignoreCols = "check_plan_id")
  @Transactional
  void 指定した設備の点検計画が登録できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plan")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "checkType": "取替",
                  "period": "10年",
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

  // POSTメソッドでリクエストのcheckType,periodのいずれかがnullの時に、ステータスコード400とエラーメッセージが返されること
  // （NotBlankのバリデーション確認、checkType,periodは同じアノテーションを付与しており同じString型のため、
  // 代表してcheckTypeで確認。以下同様）
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plan")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "checkType": null,
                  "period": "10年",
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
          "message": "checkType,periodは必須項目です。10文字以内で入力してください",
          "path": "/equipments/1/plan"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのcheckType,periodのいずれかが空文字の時に、
  // ステータスコード400とエラーメッセージが返されること（NotBlankのバリデーション確認）
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストで空文字の項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plan")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "checkType": "",
                  "period": "10年",
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
          "message": "checkType,periodは必須項目です。10文字以内で入力してください",
          "path": "/equipments/1/plan"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのcheckType,periodのいずれかが10文字を超えている時に、
  // ステータスコード400とエラーメッセージが返されること（@Size(max = 10)のバリデーション確認）
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストで10文字を超える項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plan")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "checkType": "aaaaaaaaaaa",
                  "period": "10年",
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
          "message": "checkType,periodは必須項目です。10文字以内で入力してください",
          "path": "/equipments/1/plan"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }
}
