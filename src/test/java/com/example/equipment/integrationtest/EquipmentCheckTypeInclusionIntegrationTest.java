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
public class EquipmentCheckTypeInclusionIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 指定した設備IDの包含関係が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/inclusions"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        [
          {"inclusionId": 1, "equipmentId": 1, "upperCheckTypeId": 1, "lowerCheckTypeId": 2},
          {"inclusionId": 2, "equipmentId": 1, "upperCheckTypeId": 2, "lowerCheckTypeId": 3}
        ]
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/inclusion/empty.yml")
  @Transactional
  void 包含関係が存在しない時に空のListが取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/equipments/1/inclusions"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("[]", response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/insert_inclusion.yml", ignoreCols = "inclusion_id")
  @Transactional
  void 包含関係が正常に登録されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/inclusions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"upperCheckTypeId": 1, "lowerCheckTypeId": 3}
                    """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {"message": "包含関係が正常に登録されました"}
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 包含関係登録の際に存在しない設備IDを指定した時に404エラーが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/equipments/99/inclusions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"upperCheckTypeId": 1, "lowerCheckTypeId": 2}
                """))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 包含関係登録の際にupperCheckTypeIdが0の時に400エラーが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/inclusions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"upperCheckTypeId": 0, "lowerCheckTypeId": 2}
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "upperCheckTypeId,lowerCheckTypeIdは1以上の値を入力してください",
          "path": "/equipments/1/inclusions"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 包含関係登録の際にlowerCheckTypeIdが0の時に400エラーが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/equipments/1/inclusions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"upperCheckTypeId": 1, "lowerCheckTypeId": 0}
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "upperCheckTypeId,lowerCheckTypeIdは1以上の値を入力してください",
          "path": "/equipments/1/inclusions"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/update_inclusion.yml")
  @Transactional
  void 包含関係が正常に更新されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/equipments/1/inclusions/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"upperCheckTypeId": 1, "lowerCheckTypeId": 3}
                    """))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {"message": "包含関係が正常に更新されました"}
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 包含関係更新の際に存在しない包含関係IDを指定した時に404エラーが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.patch("/equipments/1/inclusions/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"upperCheckTypeId": 1, "lowerCheckTypeId": 2}
                """))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 包含関係更新の際にupperCheckTypeIdが0の時に400エラーが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.patch("/equipments/1/inclusions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"upperCheckTypeId": 0, "lowerCheckTypeId": 2}
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "upperCheckTypeId,lowerCheckTypeIdは1以上の値を入力してください",
          "path": "/equipments/1/inclusions/1"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/delete_inclusion.yml")
  @Transactional
  void 包含関係が正常に削除されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/1/inclusions/2"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {"message": "包含関係が正常に削除されました"}
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @Transactional
  void 包含関係削除の際に存在しない包含関係IDを指定した時に404エラーが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/1/inclusions/99"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/empty.yml")
  @Transactional
  void 指定した設備IDの包含関係が削除できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/equipments/1/inclusions"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {"message": "包含関係が正常に削除されました"}
        """, response, JSONCompareMode.STRICT);
  }
}