package com.example.equipment.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EquipmentManagementApiIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  // GETメソッドでname,number,locationのクエリパラメータを指定しない時に、設備が全数取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void クエリパラメータを指定しない時に設備が全数取得できること() throws Exception {
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
        "location": "Area1"
        },
        {
        "equipmentId": 2,
        "name": "吸込ポンプB",
        "number": "A2-C002B",
        "location": "Area2"
        },
        {
        "equipmentId": 3,
        "name": "吐出ポンプC",
        "number": "A3-C003C",
        "location": "Area3"       
        }
        ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドでname,number,locationのクエリパラメータを指定した時に、
  // 各内容に部分一致する設備が取得できステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void クエリパラメータに指定した内容と部分一致する設備が取得できること() throws Exception {
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
        "location": "Area1"
        }
        ]
        """, response, JSONCompareMode.STRICT);
  }

  // GETメソッドで設備が存在しない時に、空のListが返されステータスコード200が返されること
  @Test
  @DataSet(value = "datasets/empty.yml")
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
  @DataSet(value = "datasets/equipments.yml")
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
  @DataSet(value = "datasets/equipments.yml")
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
}
