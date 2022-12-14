package com.example.finalproject3.order.controller;

import com.example.finalproject3.cart.entity.CartItem;
import com.example.finalproject3.cart.service.CartService;
import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.member.service.MemberService;
import com.example.finalproject3.order.entity.Order;
import com.example.finalproject3.order.service.OrderService;
import com.example.finalproject3.product.entity.Product;
import com.example.finalproject3.product.service.ProductService;
import com.example.finalproject3.security.dto.SecurityMember;
import com.example.finalproject3.security.service.SecurityMemberService;
import com.example.finalproject3.util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final ProductService productService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final MemberService memberService;
    private final CartService cartService;

    @GetMapping("/charge")
    public String orderCharge(@AuthenticationPrincipal SecurityMember securityMember,
                              Model model){

        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        Order order = orderService.createByRestCash(member);

        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "/order/charge";
    }

    @GetMapping("/create/{productId}")  //?????? ??????
    public String createOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              @PathVariable long productId,
                              Model model){

        Product product = productService.findById(productId);
        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        Order order = orderService.createByProduct(member, product);

        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "/order/detail";
    }

    @GetMapping("/create")  //???????????? ??????
    public String createOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              Model model){

        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        List<CartItem> cartItems = cartService.findByBuyer(member);

        if(cartItems.size() == 0){
            return "redirect:/cart/list?errorMsg=" + Util.url.encode("??????????????? ?????? ????????? ????????????.");
        }

        Order order = orderService.createByCart(member);

        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "/order/detail";
    }

    @PostMapping("/{id}/payByRestCash")
    public String payByRestCashOnly(@AuthenticationPrincipal SecurityMember securityMember,
                                    @PathVariable long id) {
        Order order = orderService.findById(id);

        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        if (orderService.memberCanPayment(member, order) == false) {
            throw new RuntimeException("?????? ????????? ????????? ??? ??? ????????????.");
        }

        orderService.payByRestCash(order);

        return "redirect:/product/list?msg=%s".formatted(Util.url.encode("??????????????? ??????????????????."));
    }

    @GetMapping("/{orderId}/cancle")  //??????
    public String cancelOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              @PathVariable long orderId){

        Order order = orderService.findById(orderId);

        orderService.cancelOrder(order);

        return "redirect:/product/list?msg=" + Util.url.encode("%d??? ????????? ?????????????????????.".formatted(order.getId()));
    }

    @GetMapping("/{orderId}/refund")  //??????
    public String refundOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              @PathVariable long orderId){

        Order order = orderService.findById(orderId);

        if(!orderService.canRefund(order)){
            return "redirect:/member/mybook?errorMsg=" + Util.url.encode("%d??? ????????? ??????????????? ???????????? ????????? ??? ????????????.".formatted(order.getId()));
        }


        orderService.refund(order);

        return "redirect:/member/mybook?msg=" + Util.url.encode("%d??? ????????? ?????????????????????.".formatted(order.getId()));
    }

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    private final String SECRET_KEY = "test_sk_jZ61JOxRQVEgMjQY2bR8W0X9bAqw";

    @RequestMapping("/{id}/success")
    public String confirmPayment(@AuthenticationPrincipal SecurityMember securityMember,
                                 @PathVariable long id,
                                 @RequestParam String paymentKey,
                                 @RequestParam String orderId,
                                 @RequestParam Long amount,
                                 Model model) throws Exception {

        Order order = orderService.findById(id);
        Member member = securityMember.getMember();

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if ( id != orderIdInputed ) {
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        if (orderService.memberCanPayment(member, order) == false) {
            throw new RuntimeException("?????? ????????? ????????? ??? ??? ????????????.");
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 ?????? ???????????? ??????
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));


        long restCash = memberService.getRestCash(member);
        long payPriceRestCash = order.getPayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new RuntimeException("???????????? ???????????????.");
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // ??????????????? ?????? ?????? callback ????????? ????????? secret??? ??????????????? ?????????
            return "order/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "/order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "/order/fail";
    }

}
