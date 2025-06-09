package com.techacademy.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // 追加
import org.springframework.validation.annotation.Validated; // 追加
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.entity.User;
import com.techacademy.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("userlist", service.getUserList());
        // user/list.htmlに画面遷移
        return "user/list";
    }

    /** User登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute User user) {
        // User登録画面に遷移
        return "user/register";
    }


    /** User登録処理 */
    @PostMapping("/register")
    public String postRegister(@Validated User user, BindingResult res, Model model) {
        if(res.hasErrors()) {
            // エラーあり
            model.addAttribute("user", user);
            return "user/register";
        }
        // User登録
        service.saveUser(user);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }

    // User更新画面を表示: IDが4ならばアドレスは/user/update/4
    @GetMapping({"/update/{id}/", "/update/"})
    //71変更中　6/6 12:30pm| 更新画面を表示する時はID必要、エラーの時NULL表示
    public String getUser(@PathVariable(name = "id", required = false) Integer id, @ModelAttribute User user, Model model) {
        if(id != null) { //もしIDがnullでないならならば、DBから情報得る
        // そしてModelに登録する
        model.addAttribute("user", service.getUser(id));
        } else { //もしIDがnullならば
            model.addAttribute("user", user);
        }
        // User更新画面に遷移
        return "user/update";
    }

    // User更新処理| postUser()メソッド
    //入力から保存する必要がある。エラーがある場合 getUser() メソッドを呼び出し
    //6/7 76修正中
    @PostMapping("/update/{id}/")
    public String postUser(@PathVariable("id") Integer id, @Validated User user, BindingResult res, Model model) {
        if(res.hasErrors()) { //res.hasErrors() でエラーの有無を確認。エラーだった場合は getRegister() メソッドを呼び出すことで、User登録画面を表示
        //エラーある場合も保存が必要だが、再度入力画面へ
            user.setId(id);
            return getUser(null, user, model);
        }

        //ID 保存
        user.setId(id);
        service.saveUser(user);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }

    /** User削除処理 */
    @PostMapping(path="list", params="deleteRun")
    public String deleteRun(@RequestParam(name="idck") Set<Integer> idck, Model model) {
        // Userを一括削除
        service.deleteUser(idck);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }
}