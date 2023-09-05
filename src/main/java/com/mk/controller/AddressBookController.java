package com.mk.controller;

import com.mk.common.R;
import com.mk.pojo.AddressBook;
import com.mk.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public R list(){
        List<AddressBook> bookList = addressBookService.listAddressBook();
        return R.SUCCESS(bookList);
    }
    // 添加地址信息  POST :/addressBook
    @PostMapping
    public R saveAddress(@RequestBody AddressBook addressBook){
        addressBookService.saveAddress(addressBook);
        return R.SUCCESS();
    }

    @DeleteMapping
    // 删除地址信息ById  :DELETE /addressBook?ids=1417414526093082626
    public R removeAddress(@RequestParam("ids") List<Long> ids){
        addressBookService.removeAddress(ids);
        return R.SUCCESS();
    }

    // 查询地址信息（修改回显数据） ById  :GET /addressBook/1417414526093082626
    @GetMapping("/{id}")
    public R getAddressById(@PathVariable("id") Long id){
        return R.SUCCESS(addressBookService.getAddressById(id));
    }
    // 修改地址信息 ById    :PUT /addressBook
    @PutMapping
    public R modifierAddress(@RequestBody AddressBook addressBook){
        addressBookService.modifierAddress(addressBook);
        return R.SUCCESS();
    }

    // 修改设置默认地址信息   :PUT /addressBook/default
    @PutMapping("/default")
    public R modifierDefault(@RequestBody AddressBook addressBook){
        addressBookService.modifierDefault(addressBook.getId());
        return R.SUCCESS();
    }

    // 查询当前登录的用户的默认地址信息
    @GetMapping("/default")
    public R getDefault(){
        return R.SUCCESS(addressBookService.getDefault());
    }


}
