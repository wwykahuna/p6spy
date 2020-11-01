package com.p6spy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.p6spy.mybaits.AllianceMapper;
import com.p6spy.mybaits.entity.AlliancePO;

@RestController
public class TestController {

	@Autowired
	private AllianceMapper userMapper;

	@RequestMapping(value = "/dbtest")
	public String dbtest() {
		List<AlliancePO> list = userMapper.selectList(null);
		return "OK"+list.size();
	}
}
