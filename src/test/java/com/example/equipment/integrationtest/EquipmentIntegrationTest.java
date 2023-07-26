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
public class EquipmentIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  // GETメソッドでname,number,location,deadlineのクエリパラメータを指定しない時に、
  // 設備と点検期限が全数取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void クエリパラメータを指定しない時に設備と点検期限が全数取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        [
          {
            "equipmentId": 1,
            "name": "真空ポンプA",
            "number": "A1-C001A",
            "location": "Area1",
            "checkPlanId": 1,
            "checkType": "簡易点検",
            "deadline": "2023-09-30"
          },
          {
            "equipmentId": 1,
            "name": "真空ポンプA",
            "number": "A1-C001A",
            "location": "Area1",
            "checkPlanId": 2,
            "checkType": "本格点検",
            "deadline": "2026-09-30"
          },
          {
            "equipmentId": 2,
            "name": "吸込ポンプB",
            "number": "A2-C002B",
            "location": "Area2",
            "checkPlanId": 3,
            "checkType": "簡易点検",
            "deadline": "2023-10-30"
          },
          {
            "equipmentId": 2,
            "name": "吸込ポンプB",
            "number": "A2-C002B",
            "location": "Area2",
            "checkPlanId": 4,
            "checkType": "本格点検",
            "deadline": "2025-11-30"
          },
          {
            "equipmentId": 3,
            "name": "吐出ポンプC",
            "number": "A3-C003C",
            "location": "Area3",
            "checkPlanId": null,
            "checkType": null,
            "deadline": null
          }
        ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドでname,number,locationのクエリパラメータを指定しdeadlineを指定しない時に、
  // 各内容に部分一致する設備と点検期限が取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void name_number_locationに指定した内容と部分一致する設備と点検期限が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments?name=ポンプ&number=C00&location=1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        [
          {
            "equipmentId": 1,
            "name": "真空ポンプA",
            "number": "A1-C001A",
            "location": "Area1",
            "checkPlanId": 1,
            "checkType": "簡易点検",
            "deadline": "2023-09-30"
          },
          {
            "equipmentId": 1,
            "name": "真空ポンプA",
            "number": "A1-C001A",
            "location": "Area1",
            "checkPlanId": 2,
            "checkType": "本格点検",
            "deadline": "2026-09-30"
          }
        ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドでdeadlineのクエリパラメータを指定した時に、
  // deadlineが指定した日付以前の設備と点検期限が取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml, datasets/plan/plans.yml")
  @Transactional
  void deadlineが指定した日付以前の設備と点検期限が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders
                .get("/equipments?name=ポンプ&number=C00&location=1&deadline=2023-10-30"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        [
          {
            "equipmentId": 1,
            "name": "真空ポンプA",
            "number": "A1-C001A",
            "location": "Area1",
            "checkPlanId": 1,
            "checkType": "簡易点検",
            "deadline": "2023-09-30"
          }
        ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドで設備が存在しない時に、空のListが返されステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipment/empty.yml, datasets/plan/plans.yml")
  @Transactional
  void 設備が存在しない時に空のListが取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        []
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドで存在する設備IDを指定した時に、指定したIDの設備が取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 指定したIDの設備が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "equipmentId": 1,
        "name": "真空ポンプA",
        "number": "A1-C001A",
        "location": "Area1"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 指定したIDの設備が存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/4"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
        "status": "404",
        "error": "Not Found",
        "message": "Not Found",
        "path": "/equipments/4"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドで正しくリクエスト（name,number,locationをすべて20文字以内で入力）した時に、
  // 設備が登録できステータスコード201とメッセージが返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @ExpectedDataSet(value = "datasets/equipment/insert_equipment.yml", ignoreCols = "equipment_id")
  @Transactional
  void 設備が登録できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content("""
                {
                "name": "真空ポンプB",
                "number": "A1-C001B",
                "location": "Area1"
                }                                
                """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "message": "設備が正常に登録されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // POSTメソッドでリクエストのname,number,locationのいずれかがnullの時に、ステータスコード400とエラーメッセージが返されること
  // （NotBlankのバリデーション確認、name,number,locationはすべて同じアノテーションを付与しており同じString型のため、
  // 代表してnameで確認。以下同様）
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 登録時のリクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                "name": null,
                "number": "A1-C001B",
                "location": "Area1"
                }                                
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
        "status": "400",
        "error": "Bad Request",
        "message": "name,number,locationは必須項目です。20文字以内で入力してください",
        "path": "/equipments"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのname,number,locationのいずれかが空文字の時に、
  // ステータスコード400とエラーメッセージが返されること（NotBlankのバリデーション確認）
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 登録時のリクエストで空文字の項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                "name": "",
                "number": "A1-C001B",
                "location": "Area1"
                }                                
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
        "status": "400",
        "error": "Bad Request",
        "message": "name,number,locationは必須項目です。20文字以内で入力してください",
        "path": "/equipments"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // POSTメソッドでリクエストのname,number,locationのいずれかが20文字を超えている時に、
  // ステータスコード400とエラーメッセージが返されること（@Size(max = 10)のバリデーション確認）
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 登録時のリクエストで20文字を超えている項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                "name": "aaaaaaaaaaaaaaaaaaaaa",
                "number": "A1-C001B",
                "location": "Area1"
                }                                
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
        "status": "400",
        "error": "Bad Request",
        "message": "name,number,locationは必須項目です。20文字以内で入力してください",
        "path": "/equipments"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // PATCHメソッドで存在する設備IDを指定し正しくリクエストした時に、設備が更新できステータスコード200とメッセージが返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @ExpectedDataSet(value = "datasets/equipment/update_equipment.yml")
  @Transactional
  void 設備が更新できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/equipments/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                "name": "真空ポンプB",
                "number": "A1-C001B",
                "location": "Area1"
                }                                
                """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "message": "設備が正常に更新されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // PATCHメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 更新リクエストで指定したIDの設備が存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/equipments/4")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                "name": "真空ポンプB",
                "number": "A1-C001B",
                "location": "Area1"
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
        "path": "/equipments/4"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // PATCHメソッドでリクエストのname,number,locationのいずれかがnullの時に、ステータスコード400とエラーメッセージが返されること
  // （NotBlankのバリデーション確認、POSTメソッドでも確認しているため空文字と20文字を超える場合は割愛）
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 更新リクエストでnullの項目がある時にエラーメッセージが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/equipments/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                {
                "name": null,
                "number": "A1-C001B",
                "location": "Area1"
                }                                
                """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
        "status": "400",
        "error": "Bad Request",
        "message": "name,number,locationは必須項目です。20文字以内で入力してください",
        "path": "/equipments/1"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }

  // DELETEメソッドで存在する設備IDを指定した時に、設備が削除できステータスコード200とメッセージが返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @ExpectedDataSet(value = "datasets/equipment/delete_equipment.yml")
  @Transactional
  void 設備が削除できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "message": "設備が正常に削除されました"
        }
        """, response, JSONCompareMode.STRICT);
  }

  // DELETEメソッドで存在しない設備IDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
  @Test
  @DataSet(value = "datasets/equipment/equipments.yml")
  @Transactional
  void 削除リクエストで指定したIDの設備が存在しない時に例外がスローされること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/4"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
        "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
        "status": "404",
        "error": "Not Found",
        "message": "Not Found",
        "path": "/equipments/4"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", ((o1, o2) -> true))));
  }
}
