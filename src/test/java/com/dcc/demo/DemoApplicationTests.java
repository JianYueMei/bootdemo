package com.dcc.demo;

import com.dcc.demo.model.User;
import com.dcc.demo.repository.UserRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    private UserRepository userRepository;
    @Test
    void contextLoads() {
    }
    @Test
    public void test(){
        Date data = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(data);

        userRepository.save(new User("代长春",11));
        userRepository.save(new User("真滴帅",22));
        userRepository.save(new User("啊",33));

        List<User> user = userRepository.findAll();
        userRepository.delete(userRepository.findByName("代长春"));
    }

    @Test
    public void testPagingAndSortingRepositoryPaging() {
        //Pageable:封装了分页的参数，当前页，煤业显示的条数。注意：它的当前页是从0开始
        //PageRequest(page,size):page表示当前页，size表示每页显示多少条
        int page = 1;
        int size = 2;
        Sort sort = Sort.by("id");
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<User> pageUser=this.userRepository.findAll(pageable);
        System.out.println("数据的总条数："+pageUser.getTotalElements());
        System.out.println("总页数："+pageUser.getTotalPages());
        List<User> list=pageUser.getContent();
        for (User users:list){
            System.out.println(users);
        }
    }

}
