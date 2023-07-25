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
public class HistoryIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  // GETメソッドで存在する設備IDを指定した時に、指定した設備IDの点検履歴が取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 指定した設備IDの点検履歴が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/histories"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
               [
                 {
                    "checkHistoryId": 1,
                    "equipmentId": 1,
                    "implementationDate": "2022-09-30",
                    "checkType": "簡易点検",
                    "result": "良"
                 },
                 {
                   "checkHistoryId": 2,
                   "equipmentId": 1,
                   "implementationDate": "2021-09-30",
                   "checkType": "本格点検",
                   "result": "良"
                 }
               ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 指定したIDの設備が存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/4/histories"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "404",
          "error": "Not Found",
          "message": "Not Found",
          "path": "/equipments/4/histories"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // GETメソッドで点検履歴が存在しない時に、空のListが返されステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/history/empty.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 点検計画が存在しない時に空のListが取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/histories"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        []
        """, response, JSONCompareMode.STRICT);
  }

  // POSTメソッドで正しくリクエスト（implementationDate,checkType,resultが全て入力されており、
  // checkTypeは10文字以内で入力、resultは50文字以内で入力）した時に、
  // 指定した設備の点検履歴が登録できステータスコード201とメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @ExpectedDataSet(value = "datasets/history/insert_history.yml", ignoreCols = "check_history_id")
  @Transactional
  void 指定した設備の点検履歴が登録できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/histories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "implementationDate": "2015-09-30",
                  "checkType": "取替",
                  "result": "良"
                }
                """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検履歴が正常に登録されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // POSTメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 登録の際に指定した設備IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/4/histories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "implementationDate": "2015-09-30",
                      "checkType": "取替",
                      "result": "良"
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
          "path": "/equipments/4/histories"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのimplementationDate,checkType,resultのいずれかがnullの時に、
  // ステータスコード400とエラーメッセージが返されること（NotBlankのバリデーション確認、
  // implementationDate,checkType,resultは同じString型のため、代表してimplementationDateで確認。以下同様）
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 登録時のリクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/histories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "implementationDate": null,
                  "checkType": "取替",
                  "result": "良"
                }
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "implementationDate,checkType,resultは必須項目です。checkTypeは10文字以内、resultは50文字以内で入力してください",
          "path": "/equipments/1/histories"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのimplementationDate,checkType,resultのいずれかが空文字の時に、
  // ステータスコード400とエラーメッセージが返されること（NotBlankのバリデーション確認）
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 登録時のリクエストで空文字の項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/histories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "implementationDate": null,
                  "checkType": "取替",
                  "result": "良"
                }
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "implementationDate,checkType,resultは必須項目です。checkTypeは10文字以内、resultは50文字以内で入力してください",
          "path": "/equipments/1/histories"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのcheckTypeが10文字を超えている時に、
  // ステータスコード400とエラーメッセージが返されること（@Size(max = 10)のバリデーション確認、resultは割愛）
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 登録時のリクエストで10文字を超える項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/histories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "implementationDate": "2015-09-30",
                  "checkType": "aaaaaaaaaaa",
                  "result": "良"
                }
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "implementationDate,checkType,resultは必須項目です。checkTypeは10文字以内、resultは50文字以内で入力してください",
          "path": "/equipments/1/histories"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // PATCHメソッドで存在する点検履歴IDを指定し正しくリクエスト（implementationDate,checkType,resultが
  // 全て入力されており、checkTypeは10文字以内で入力、resultは50文字以内で入力）した時に、
  // 点検計画が更新できステータスコード200とメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/update_history.yml")
  @Transactional
  void 指定したIDの点検履歴が更新できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/histories/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "implementationDate": "2015-09-30",
                  "checkType": "取替",
                  "result": "良"
                }
                """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検履歴が正常に更新されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // PATCHメソッドで存在しない点検履歴IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @Transactional
  void 更新の際に指定した点検履歴IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/histories/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                  "implementationDate": "2015-09-30",
                  "checkType": "取替",
                  "result": "良"
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
          "path": "/histories/5"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // PATCHメソッドでリクエストのimplementationDate,checkType,resultのいずれかがnullの時に、
  // ステータスコード400とエラーメッセージが返されること
  // （NotBlankのバリデーション確認、POSTメソッドでも確認しているため空文字と文字数制限を超える場合は割愛）
  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @Transactional
  void 更新リクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/histories/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                    {
                      "implementationDate": null,
                      "checkType": "取替",
                      "result": "良"
                    }
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "implementationDate,checkType,resultは必須項目です。checkTypeは10文字以内、resultは50文字以内で入力してください",
          "path": "/histories/2"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // DELETEメソッドで存在する点検履歴IDを指定した時に、点検履歴が削除できステータスコード200とメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/delete_history.yml")
  @Transactional
  void 指定したIDの点検履歴が削除できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/histories/4"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検履歴が正常に削除されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // DELETEメソッドで存在しない点検履歴IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @Transactional
  void 削除の際に指定した点検履歴IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/histories/5"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "404",
          "error": "Not Found",
          "message": "Not Found",
          "path": "/histories/5"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // DELETEメソッドで存在する設備IDを指定した時に、点検履歴が削除できステータスコード200とメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/delete_by_equipment_id.yml")
  @Transactional
  void 指定した設備IDの点検履歴が削除できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/2/histories"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "message": "点検履歴が正常に削除されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // DELETEメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/history/histories.yml, datasets/equipment/equipments.yml")
  @Transactional
  void 削除の際に指定した設備IDが存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/4/plans"))
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
}
