package com.mk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mk.common.MyThreadLocal;
import com.mk.mapper.AddressBookMapper;
import com.mk.pojo.AddressBook;
import com.mk.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    // 查询所有地址信息
    @Override
    public List<AddressBook> listAddressBook() {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq( AddressBook::getUserId, MyThreadLocal.getValue());
        return addressBookMapper.selectList(wrapper);
    }

    // 添加地址信息  POST :/addressBook
    @Override
    public Boolean saveAddress(AddressBook addressBook){
        addressBook.setUserId(MyThreadLocal.getValue());
        addressBookMapper.insert(addressBook);
        return null;
    }

    // 删除地址信息ById  :DELETE /addressBook?ids=1417414526093082626
    @Override
    public Boolean removeAddress(List<Long> ids){
        addressBookMapper.deleteBatchIds(ids);
        return null;
    }

    // 查询地址信息（修改回显数据） ById  :GET /addressBook/1417414526093082626
    @Override
    public AddressBook getAddressById(Long id){
        return addressBookMapper.selectById(id);
    }
    // 修改地址信息 ById    :PUT /addressBook
    @Override
    public Boolean modifierAddress(AddressBook addressBook){
        addressBookMapper.updateById(addressBook);
        return null;
    }

    // 修改设置默认地址信息   :PUT /addressBook/default
    @Override
    @Transactional
    public Boolean modifierDefault(Long id){
        // 将原来的默认地址 标识设为 0
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, MyThreadLocal.getValue());
        wrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookMapper.selectOne(wrapper);
        if (addressBook != null){
            addressBook.setIsDefault(0);
            addressBookMapper.updateById(addressBook);
        }

        // 设置默认地址 标识
        AddressBook addressBookDefault = addressBookMapper.selectById(id);
        addressBookDefault.setIsDefault(1);
        log.info(addressBookDefault.toString());
        addressBookMapper.updateById(addressBookDefault);
        return null;
    }

    // 查询当前登录的用户的默认地址信息
    @Override
    public AddressBook getDefault(){
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq( AddressBook::getUserId, MyThreadLocal.getValue());
        wrapper.eq(AddressBook::getIsDefault, 1);
        return addressBookMapper.selectOne(wrapper);
    }
}
