package com.benefitj.springiotstarter;

import com.benefitj.spring.ServletUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SpringBootApplication
public class SpringIotStarterApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringIotStarterApplication.class, args);
  }

  @RestController
  @RequestMapping("/")
  public static class ApiController {

    @GetMapping
    public String test(HttpServletRequest request) {

      long start = System.currentTimeMillis();

      StringBuilder sb = new StringBuilder();
      Map<String, String> headers = ServletUtils.getHeaderMap(request);
      headers.forEach((name, value) -> sb.append(name).append(" ==>: ").append(value).append("\n"));
      sb.append("remoteAddress").append(" ==>: ").append(request.getRemoteHost()).append("\n");
      sb.append("spend: ").append(System.currentTimeMillis() - start).append("\n");
      sb.append("thread: ").append(Thread.currentThread().getName()).append("\n");
      sb.append("time: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
      return sb.toString();
    }

  }
}
