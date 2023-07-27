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

  // GETメソッドで設備IDを指定した時に、指定した設備IDの点検計画が取得できステータスコード200が返されること
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
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/plans"))
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
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
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

  // POSTメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/plan/plans.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 登録の際に指定した設備IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/4/plans")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "checkType": "取替",
                      "period": "10年",
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

  // POSTメソッドでリクエストのcheckType,periodのいずれかがnullの時に、ステータスコード400とエラーメッセージが返されること
  // （NotBlankのバリデーション確認、checkType,periodは同じアノテーションを付与しており同じString型のため、
  // 代表してcheckTypeで確認。以下同様）
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 登録時のリクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
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
          "path": "/equipments/1/plans"
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
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
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
          "path": "/equipments/1/plans"
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
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/plans")
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
          "path": "/equipments/1/plans"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // PATCHメソッドで存在する点検計画IDを指定し正しくリクエスト（checkType,periodをいずれも10文字以内で入力）した時に、
  // 点検計画が更新できステータスコード200とメッセージが返されること
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
                  "checkType": "取替",
                  "period": "10年",
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

  // PATCHメソッドで存在しない点検計画IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 更新の際に指定した点検計画IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/plans/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "checkType": "取替",
                  "period": "10年",
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

  // PATCHメソッドでリクエストのcheckType,periodのいずれかがnullの時に、ステータスコード400とエラーメッセージが返されること
  // （NotBlankのバリデーション確認、POSTメソッドでも確認しているため空文字と20文字を超える場合は割愛）
  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 更新リクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/plans/2")
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
          "path": "/plans/2"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // DELETEメソッドで存在する点検計画IDを指定した時に、点検計画が削除できステータスコード200とメッセージが返されること
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

  // DELETEメソッドで存在しない点検計画IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
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

  // DELETEメソッドで設備IDを指定した時に、点検計画が削除できステータスコード200とメッセージが返されること
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
