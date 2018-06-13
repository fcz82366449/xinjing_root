package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.xinjing.domain.Order;
import cn.easy.xinjing.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService	orderService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.order.index");
        return "order/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Order> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.order.list");

        Page<Order> page = orderService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.order.formGet");
        if (isValidId(id)) {
            Order order = orderService.getOne(id);
            model.addAttribute("order", order);
        } else {
            model.addAttribute("order", new Order());
        }
        return "order/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Order order, HttpServletRequest request) {
        increment("web.order.formPost");
        orderService.save(order);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.order.delete");
        orderService.delete(id);
        return toSuccess("删除成功");
    }

}


