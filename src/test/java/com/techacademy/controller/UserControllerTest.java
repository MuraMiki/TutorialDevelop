package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import com.techacademy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class USerControllerTest2 {

    private MockMvc mockMvc;

    private WebApplicationContext webApplicationContext = null;

    // コンストラクタインジェクション
    void UserControllerTest2(WebApplicationContext context) {
        this.webApplicationContext = context;
    }

    @BeforeEach
    void beforeEach() {
        // Spring Security 対応の MockMvc 構築
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("User更新画面")
    @WithMockUser
    void testGetUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/update/1/"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("user"))
            .andExpect(model().hasNoErrors())
            .andExpect(view().name("user/update"))
            .andReturn();

        User user = (User) result.getModelAndView().getModel().get("user");
        assertEquals(1, user.getId());
        assertEquals("キラメキ太郎", user.getName());
    }

    @Test
    @DisplayName("User一覧画面（/user/list）")
    @WithMockUser
    void testGetList() throws Exception {
        // HTTPリクエスト実行
        MvcResult result = mockMvc.perform(get("/user/list"))
            .andExpect(status().isOk()) // HTTP 200 OK
            .andExpect(model().attributeExists("userlist")) // userlist あり

            .andExpect(model().hasNoErrors()) // modelエラーなし
            .andExpect(view().name("user/list")) // ビュー名一致
            .andReturn();

        // モデルから userlist を取得
        @SuppressWarnings("unchecked")
        List<User> userlist = (List<User>) result.getModelAndView().getModel().get("userlist");

        // 件数検証（3件）
        assertEquals(3, userlist.size(), "ユーザー数が3件であること");

        // 各ユーザー内容確認
        assertEquals(1, userlist.get(0).getId());
        assertEquals("キラメキ太郎", userlist.get(0).getName());

        assertEquals(2, userlist.get(1).getId());
        assertEquals("テストユーザー1", userlist.get(1).getName());

        assertEquals(3, userlist.get(2).getId());
        assertEquals("テストユーザー2", userlist.get(2).getName());
    }
}