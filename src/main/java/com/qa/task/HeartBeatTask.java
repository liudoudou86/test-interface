package com.qa.task;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author Tesla.liu
 * @Date 2024/02/21
 * @Description
 */

@Slf4j
@Component
public class HeartBeatTask {

	@Scheduled(cron = "0/3 * * * * ? ")
	public void detection() {
		String date = DateUtil.now();
		log.info("♥ Heart Beat Detection ♥ ===> " + date);
	}

}
