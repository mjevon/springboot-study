package com.leon.axon.web;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.axon.applicaiton.cmd.CreateOrderCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class OrderController {
  @Autowired private CommandGateway commandGateway;

  @Autowired private HttpServletResponse response;

  @RequestMapping(method = RequestMethod.POST)
  public void create(@RequestBody(required = true) JSONObject input) {
    log.info(input.toJSONString());

    int responseCode = HttpServletResponse.SC_BAD_REQUEST;

    if (input.containsKey("username") && input.containsKey("products")) {
      String username = input.getString("username");
      JSONArray products = input.getJSONArray("products");
      if (!StringUtils.isEmpty(username) && products.size() > 0) {
        Map<String, Integer> map = new HashMap<>();
        CreateOrderCommand command = new CreateOrderCommand(username, map);
        for (Object each : products) {
          JSONObject o = (JSONObject) each;
          if (!o.containsKey("id") || !o.containsKey("number")) return;
          map.put(o.getString("id"), o.getInteger("number"));
        }
        commandGateway.sendAndWait(command);
        responseCode = HttpServletResponse.SC_CREATED;
      }
    }

    response.setStatus(responseCode);
  }
}
