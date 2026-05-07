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
public class CheckTypeIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 全ての点検種別が取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/check-types"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        [
          {"checkTypeId": 1, "name": "簡易点検"},
          {"checkTypeId": 2, "name": "本格点検"},
          {"checkTypeId": 3, "name": "取替"}
        ]
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/empty.yml")
  @Transactional
  void 点検種別が存在しない時に空のListが取得できること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.get("/check-types"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("[]", response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @ExpectedDataSet(value = "datasets/check_type/insert_check_type.yml", ignoreCols = "check_type_id")
  @Transactional
  void 点検種別が正常に登録されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/check-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "電気点検"}
                    """))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {"message": "点検種別が正常に登録されました"}
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 点検種別登録の際にnameがnullの時に400エラーが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/check-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": null}
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "name: 必須項目です",
          "path": "/check-types"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 点検種別登録の際にnameが空文字の時に400エラーが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/check-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": ""}
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "name: 必須項目です",
          "path": "/check-types"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 点検種別登録の際にnameが21文字の時に400エラーが返されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.post("/check-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "あいうえおかきくけこさしすせそたちつてなに"}
                    """))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {
          "timestamp": "2023-07-14T12:00:00.511021+09:00[Asia/Tokyo]",
          "status": "400",
          "error": "Bad Request",
          "message": "name: 20文字以内で入力してください",
          "path": "/check-types"
        }
        """, response, new CustomComparator(JSONCompareMode.STRICT,
        new Customization("timestamp", (o1, o2) -> true)));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @ExpectedDataSet(value = "datasets/check_type/delete_check_type.yml")
  @Transactional
  void 点検種別が正常に削除されること() throws Exception {
    String response =
        mockMvc.perform(MockMvcRequestBuilders.delete("/check-types/3"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("""
        {"message": "点検種別が正常に削除されました"}
        """, response, JSONCompareMode.STRICT);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 点検種別削除の際に存在しないIDを指定した時に404エラーが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/check-types/99"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
