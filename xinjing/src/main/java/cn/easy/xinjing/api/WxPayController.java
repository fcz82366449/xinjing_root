package cn.easy.xinjing.api;

import cn.easy.base.utils.DateTimeUtil;
import cn.easy.weixin.web.MpPayController;
import cn.easy.xinjing.aop.ApiAuth;
import cn.easy.xinjing.domain.CapitalFlow;
import cn.easy.xinjing.domain.Order;
import cn.easy.xinjing.domain.Prescription;
import cn.easy.xinjing.service.CapitalFlowService;
import cn.easy.xinjing.service.OrderService;
import cn.easy.xinjing.service.PrescriptionService;
import cn.easy.xinjing.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by caosk on 16/11/21.
 */
@Api(value = "user-api-controller", description = "微信支付相关API", position = 6)
@Controller
@RequestMapping("/api/v1/wxPay")
public class WxPayController extends MpPayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CapitalFlowService capitalFlowService;
    @Autowired
    private PrescriptionService prescriptionService;

    @ApiAuth
    @ApiOperation(value = "微信支付通知", notes = "微信支付通知", position = 17)
    @ResponseBody
    @RequestMapping(value = {"/payNotify"}, method = RequestMethod.POST)
    public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        payNotify(request, response);
    }

    @Override
    protected void dealPay(String outTradeNo, String transactionId, String totalFee, String timeEnd, String attach) {
        logger.warn("进入wxPay dealPay outTradeNo:" + outTradeNo);

        Order order = orderService.getByOutTradeNo(outTradeNo);
        order.setStatus(Constants.ORDER_STATUS_SUCCESS);
        order.setPaidAt(new Timestamp(DateTimeUtil.parse(timeEnd, "yyyyMMddHHmmss").getTime()));
        orderService.save(order);

        //更新具体业务状态
        if(Constants.ORDER_OBJECT_TYPE_PRESCRIPTION.equals(order.getObjectType())) {
            Prescription prescription = prescriptionService.getOne(order.getObjectId());
            prescription.setPayStatus(Constants.PRESCRIPTION_PAY_STATUS_PAID);
            prescription.setStatus(Constants.PRESCRIPTION_STATUS_FINISH);
            prescriptionService.save(prescription);
        }
        
        //增加患者支出记录
        CapitalFlow expandFlow = new CapitalFlow();
        expandFlow.setUserId(order.getFromUserId());
        expandFlow.setOrderId(order.getId());
        expandFlow.setAmount(order.getAmount());
        if(Constants.ORDER_OBJECT_TYPE_PRESCRIPTION.equals(order.getObjectType())){
            expandFlow.setFeeType(Constants.CAPITAL_FLOW_FEE_TYPE_PRESCRIPTION_FEE);
        }
        expandFlow.setHappenedTime(new Date());
        expandFlow.setStatus(Constants.CAPITAL_FLOW_STATUS_SUCCESS);
        expandFlow.setType(Constants.CAPITAL_FLOW_TYPE_EXPEND);
        capitalFlowService.save(expandFlow);

        //增加医生收入记录
        CapitalFlow incomeFlow = new CapitalFlow();
        incomeFlow.setUserId(order.getToUserId());
        incomeFlow.setOrderId(order.getId());
        incomeFlow.setAmount(order.getAmount());
        if(Constants.ORDER_OBJECT_TYPE_PRESCRIPTION.equals(order.getObjectType())){
            incomeFlow.setFeeType(Constants.CAPITAL_FLOW_FEE_TYPE_PRESCRIPTION_FEE);
        }
        incomeFlow.setHappenedTime(new Date());
        //因为医生要确认，流水才算成功，所以此处为处理中
        incomeFlow.setStatus(Constants.CAPITAL_FLOW_STATUS_PROCESSING);
        incomeFlow.setType(Constants.CAPITAL_FLOW_TYPE_INCOME);
        capitalFlowService.save(incomeFlow);


        //TODO 如果有抽成 记录抽成记录
        
        
        logger.warn("订单处理完成,订单ID" + order.getId());
    }
}
