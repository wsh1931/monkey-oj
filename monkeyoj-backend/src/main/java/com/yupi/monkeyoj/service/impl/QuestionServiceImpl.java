package com.yupi.monkeyoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.monkeyoj.model.entity.Question;
import com.yupi.monkeyoj.service.QuestionService;
import com.yupi.monkeyoj.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴思豪
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-12-28 18:36:31
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




