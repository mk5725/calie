package com.mk.service;

import com.mk.common.R;
import com.mk.pojo.AddressBook;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface AddressBookService {
    // 查询所有地址信息
    List<AddressBook> listAddressBook();

    // 添加地址信息  POST :/addressBook
    Boolean saveAddress(AddressBook addressBook);

    // 删除地址信息ById  :DELETE /addressBook?ids=1417414526093082626
    Boolean removeAddress(List<Long> ids);

    // 查询地址信息（修改回显数据） ById  :GET /addressBook/1417414526093082626
    AddressBook getAddressById(Long id);
    // 修改地址信息 ById    :PUT /addressBook
    Boolean modifierAddress(AddressBook addressBook);

    // 修改设置默认地址信息   :PUT /addressBook/default
    Boolean modifierDefault(Long id);

    // 查询当前登录的用户的默认地址信息
    AddressBook getDefault();

}
